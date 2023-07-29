package com.taotao.weiyunmall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.taotao.weiyunmall.product.service.CategoryBrandRelationService;
import com.taotao.weiyunmall.product.vo.Catelog2Vo;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.common.utils.PageUtils;
import com.taotao.common.utils.Query;

import com.taotao.weiyunmall.product.dao.CategoryDao;
import com.taotao.weiyunmall.product.entity.CategoryEntity;
import com.taotao.weiyunmall.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedissonClient redisson;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    //注意：这里的BaseMapper是来自继承的ServiceImpl里的M extends BaseMapper<T>，即CategoryDao
    @Override
    public List<CategoryEntity> listWithTree() {
        //1.查出所有分类
        List<CategoryEntity> categoryEntities = baseMapper.selectList(null);
        //2.组装成父子的子结构
        //2.1 找到父类
        List<CategoryEntity> collect = categoryEntities.stream().filter(categoryEntity -> {
            return categoryEntity.getParentCid() == 0;
        }).map(menu -> {
            menu.setChildren(getChildrens(menu, categoryEntities));
            return menu;
        }).collect(Collectors.toList());
        return categoryEntities;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        //TODO //1.查询当前删除菜单，是否被别的引用

        //逻辑删除，即用某个字段表示是否删除
        baseMapper.deleteBatchIds(asList);
    }

    /**
     * 找到CatelogID的完整路径
     * 【父/子】
     *
     * @param attrGroupId1
     * @return
     */
    @Override
    public Long[] findCatelogPath(Long attrGroupId1) {
        ArrayList<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(attrGroupId1, paths);
        Collections.reverse(parentPath);
        return parentPath.toArray(new Long[parentPath.size()]);
    }

    /**
     * 级联更新所有关联数据
     * @CacheEvict:失效模式
     * @param category
     */
    @Caching(evict ={
            @CacheEvict(value = "category",key = "'level1Categorys'"),
            @CacheEvict(value = "category",key = "'getCatalogJson'")
    })
    @CacheEvict(value = "category",key = "'level1Categorys'")
    @Override
    public void updateDetail(CategoryEntity category) {
        this.updateById(category);
        if (!StringUtils.isEmpty(category.getName())) {
            //同步更新其他关联表中的数据
            categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());
        }
    }

    @Cacheable(value = "category",key = "'level1Categorys'", sync = true) //这个命名类似于Nacos的内存空间，缓存存放在它之下，key的命名是随机生成的
    @Override
    public List<CategoryEntity> getLevel1Categorys() {
        List<CategoryEntity> categoryEntities = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
        return categoryEntities;
    }

    @Cacheable(value = "category",key = "#root.methodName")
    @Override
    public Map<String, List<Catelog2Vo>> getCatalogJson() {
        System.out.println("查询数据库....");
        List<CategoryEntity> selectList = baseMapper.selectList(null);

        //1.查出所有1级分类
        List<CategoryEntity> level1Categorys = getParent_cid(selectList, 0L);
        //List<CategoryEntity> level1Categorys = getLevel1Categorys();
        Map<String, List<Catelog2Vo>> parent_cid = level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            //1.遍历每一个1级分类下的二级分类
            List<CategoryEntity> categoryEntities = getParent_cid(selectList, v.getCatId());
            //List<CategoryEntity> categoryEntities = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_id",v.getParentCid()));
            List<Catelog2Vo> catelog2Vos = null;
            if (categoryEntities != null) {
                catelog2Vos = categoryEntities.stream().map(l2 -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());
                    //找二级分类下的三级分类
                    List<CategoryEntity> categoryEntities1 = getParent_cid(selectList, l2.getCatId());
                    if (categoryEntities1 != null) {
                        List<Catelog2Vo.Catelog3Vo> catelog3Vos = categoryEntities1.stream().map(l3 -> {
                            //封装成指定格式
                            Catelog2Vo.Catelog3Vo catelog3Vo = new Catelog2Vo.Catelog3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());
                            return catelog3Vo;
                        }).collect(Collectors.toList());
                        catelog2Vo.setCatalog3List(catelog3Vos);
                    }
                    return catelog2Vo;
                }).collect(Collectors.toList());
            }
            return catelog2Vos;
        }));
        System.out.println(parent_cid.toString());
        return parent_cid;
    }


    //TODO 堆外内存异常 OutOfDirectMemoryError
    public Map<String, List<Catelog2Vo>> getCatalogJson2() {
        //注意：序列化与反序列化的过程

        /**
         * 1.空结果缓存，防止缓存穿透
         * 2.设置随机值的过期时间，解决缓存雪崩
         * 3.设置锁，解决缓存击穿
         */

        //1.加入缓存逻辑，缓存中存储的数据应该为JSON字符串
        //JSON跨语言、跨平台
        String catalogJson = stringRedisTemplate.opsForValue().get("catalogJson");
        if (StringUtils.isEmpty(catalogJson)) {
            System.out.println("缓存未命中....");
            //2.缓存未能命中,则查询数据库，并将查询到的对象作为JSON放入缓存中
            Map<String, List<Catelog2Vo>> catalogJsonDb = getCatalogJsonDbWithRedisLock();
            return catalogJsonDb;
        }
        //转为我们对应的对象
        System.out.println("缓存命中....直接返回....");
        Map<String, List<Catelog2Vo>> result = JSON.parseObject(catalogJson, new TypeReference<Map<String, List<Catelog2Vo>>>() {
        });

        return result;
    }

    /**
     * 使用Redisson作为分布式锁
     * 考虑缓存一致性
     * 1.）双写模式
     * 2.）失效模式
     * @return
     */
    public Map<String, List<Catelog2Vo>> getCatalogJsonDbWithRedissonLock() {

        //1.锁的名字：锁的粒度越细越快
        //锁的力度：
        RLock lock = redisson.getLock("CatalogJson-lock");
        lock.lock();

        Map<String, List<Catelog2Vo>> dataFromDb = null;
        //加锁成功...执行业务
        try {
            dataFromDb = getDataFromDb();
        } finally {
            lock.unlock();
        }
        return dataFromDb;
    }


    /**
     * 使用分布式锁
     *
     * @return
     */
    public Map<String, List<Catelog2Vo>> getCatalogJsonDbWithRedisLock() {

        //1.占分布式锁，同时设置过期时间（保证原子性）：
        String uuid = UUID.randomUUID().toString();
        Boolean aBoolean = stringRedisTemplate.opsForValue().setIfAbsent("lock", uuid, 30, TimeUnit.SECONDS);
        if (aBoolean) {
            System.out.println("获取分布式锁成功....");
            Map<String, List<Catelog2Vo>> dataFromDb = null;
            //加锁成功...执行业务
            try {
                dataFromDb = getDataFromDb();
            } finally {
                //获取值对比+对比成功删除=原子操作 Lua脚本
                String script = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";

                //原子删锁
                Long lock = stringRedisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class)
                        , Arrays.asList("lock"), uuid);
            }

            //删除锁
            //stringRedisTemplate.delete("lock");
            return dataFromDb;
        } else {
            //加锁失败...重试
            //休眠一段后重试
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("获取分布式锁失败，等待重试....");
            return getCatalogJsonDbWithRedisLock(); //自旋方式
        }
    }


    /**
     * 从数据库查询并封装分类数据,使用本地锁
     *
     * @return
     */
    public Map<String, List<Catelog2Vo>> getCatalogJsonFromDb() {

        /**
         * 1.将数据库多次查询变为一次查询
         */
        //synchronized会一直监听 只要释放 当别人需要的时候就会立刻又调用 自旋锁
        synchronized (this) {
            String catalogJson = stringRedisTemplate.opsForValue().get("catalogJson");
            if (!StringUtils.isEmpty(catalogJson)) {
                Map<String, List<Catelog2Vo>> result = JSON.parseObject(catalogJson, new TypeReference<Map<String, List<Catelog2Vo>>>() {
                });
                return result;
            }
            //转为我们对应的对象
            System.out.println("查询数据库....");
            List<CategoryEntity> selectList = baseMapper.selectList(null);


            //1.查出所有1级分类
            List<CategoryEntity> level1Categorys = getParent_cid(selectList, 0L);
            Map<String, List<Catelog2Vo>> parent_cid = level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
                //1.遍历每一个1级分类下的二级分类
                List<CategoryEntity> categoryEntities = getParent_cid(selectList, v.getParentCid());
                List<Catelog2Vo> catelog2Vos = null;
                if (categoryEntities != null) {
                    catelog2Vos = categoryEntities.stream().map(item -> {
                        Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, item.getCatId().toString(), item.getName());
                        //找二级分类下的三级分类
                        List<CategoryEntity> categoryEntities1 = getParent_cid(selectList, item.getCatId());
                        if (categoryEntities1 != null) {
                            List<Catelog2Vo.Catelog3Vo> catelog3Vos = categoryEntities1.stream().map(l3 -> {
                                //封装成指定格式
                                Catelog2Vo.Catelog3Vo catelog3Vo = new Catelog2Vo.Catelog3Vo(item.getCatId().toString(), l3.getCatId().toString(), l3.getName());
                                return catelog3Vo;
                            }).collect(Collectors.toList());
                            catelog2Vo.setCatalog3List(catelog3Vos);
                        }
                        return catelog2Vo;
                    }).collect(Collectors.toList());
                }

                return catelog2Vos;
            }));
            String s = JSON.toJSONString(parent_cid);
            stringRedisTemplate.opsForValue().set("catalogJson", s, 1, TimeUnit.DAYS);
            return parent_cid;
        }
    }

    private Map<String, List<Catelog2Vo>> getDataFromDb() {
        String catalogJson = stringRedisTemplate.opsForValue().get("catalogJson");
        if (!StringUtils.isEmpty(catalogJson)) {
            Map<String, List<Catelog2Vo>> result = JSON.parseObject(catalogJson, new TypeReference<Map<String, List<Catelog2Vo>>>() {
            });
            return result;
        }
        //转为我们对应的对象
        System.out.println("查询数据库....");
        List<CategoryEntity> selectList = baseMapper.selectList(null);


        //1.查出所有1级分类
        List<CategoryEntity> level1Categorys = getParent_cid(selectList, 0L);
        Map<String, List<Catelog2Vo>> parent_cid = level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            //1.遍历每一个1级分类下的二级分类
            List<CategoryEntity> categoryEntities = getParent_cid(selectList, v.getParentCid());
            List<Catelog2Vo> catelog2Vos = null;
            if (categoryEntities != null) {
                catelog2Vos = categoryEntities.stream().map(item -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, item.getCatId().toString(), item.getName());
                    //找二级分类下的三级分类
                    List<CategoryEntity> categoryEntities1 = getParent_cid(selectList, item.getCatId());
                    if (categoryEntities1 != null) {
                        List<Catelog2Vo.Catelog3Vo> catelog3Vos = categoryEntities1.stream().map(l3 -> {
                            //封装成指定格式
                            Catelog2Vo.Catelog3Vo catelog3Vo = new Catelog2Vo.Catelog3Vo(item.getCatId().toString(), l3.getCatId().toString(), l3.getName());
                            return catelog3Vo;
                        }).collect(Collectors.toList());
                        catelog2Vo.setCatalog3List(catelog3Vos);
                    }
                    return catelog2Vo;
                }).collect(Collectors.toList());
            }
            return catelog2Vos;
        }));

        String s = JSON.toJSONString(parent_cid);
        stringRedisTemplate.opsForValue().set("catalogJson", s, 1, TimeUnit.DAYS);
        return parent_cid;
    }


    private List<CategoryEntity> getParent_cid(List<CategoryEntity> selectList, Long parent_cid) {
        List<CategoryEntity> collect = selectList.stream().filter(item -> {
            return item.getParentCid().equals(parent_cid);
        }).collect(Collectors.toList());

        return collect;
        //return baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", v.getCatId()));

    }

    private List<Long> findParentPath(Long attrGroupId1, List<Long> paths) {
        //1.收集当前节点ID
        paths.add(attrGroupId1);
        CategoryEntity byId = this.getById(attrGroupId1);
        if (byId.getParentCid() != 0) {
            findParentPath(byId.getParentCid(), paths);
        }
        return paths;
    }


    private List<CategoryEntity> getChildrens(CategoryEntity entity, List<CategoryEntity> all) {
        List<CategoryEntity> collect = all.stream().filter(categoryEntity -> {
            return categoryEntity.getParentCid() == entity.getCatId();
        }).map(menu -> {
            //1.找到子菜单
            menu.setChildren(getChildrens(menu, all));
            return menu;
        }).collect(Collectors.toList());
        return collect;
    }

}