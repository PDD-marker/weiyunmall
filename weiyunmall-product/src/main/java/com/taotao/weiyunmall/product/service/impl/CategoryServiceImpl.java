package com.taotao.weiyunmall.product.service.impl;

import com.taotao.weiyunmall.product.service.CategoryBrandRelationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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
        }).map(menu->{
            menu.setChildren(getChildrens(menu,categoryEntities));
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

    @Override
    public void updateDetail(CategoryEntity category) {
        this.updateById(category);
        if(!StringUtils.isEmpty(category.getName())) {
            //同步更新其他关联表中的数据
            categoryBrandRelationService.updateCategory(category.getCatId(),category.getName());
        }
    }

    private List<Long> findParentPath(Long attrGroupId1,List<Long> paths) {
        //1.收集当前节点ID
        paths.add(attrGroupId1);
        CategoryEntity byId = this.getById(attrGroupId1);
        if(byId.getParentCid()!= 0) {
            findParentPath(byId.getParentCid(),paths);
        }
        return paths;
    }


    private List<CategoryEntity> getChildrens(CategoryEntity entity, List<CategoryEntity> all) {
        List<CategoryEntity> collect = all.stream().filter(categoryEntity -> {
            return categoryEntity.getParentCid() == entity.getCatId();
        }).map(menu ->{
            //1.找到子菜单
            menu.setChildren(getChildrens(menu,all));
            return menu;
        }).collect(Collectors.toList());
        return collect;
    }

}