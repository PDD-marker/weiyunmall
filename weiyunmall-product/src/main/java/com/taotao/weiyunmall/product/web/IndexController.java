package com.taotao.weiyunmall.product.web;

import com.taotao.weiyunmall.product.entity.CategoryEntity;
import com.taotao.weiyunmall.product.service.CategoryService;
import com.taotao.weiyunmall.product.vo.Catelog2Vo;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @version 1.0
 * @quther 孤郁
 */
@Controller
public class IndexController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    RedissonClient redisson;

    @GetMapping({"/","/index.html"})
    public String indexPage(Model model){

        //TODO 1.查出所有一级分类
        List<CategoryEntity> categoryEntities = categoryService.getLevel1Categorys();


        //这里会进行视图解析器进行拼串
        //classpath:/templates/ + 返回值 + .html
        model.addAttribute("categorys",categoryEntities);
        return "index";
    }

    @ResponseBody //将页面返回的数值以JSON方式写出
    @GetMapping("/index/catalog.json")
    public Map<String, List<Catelog2Vo>> getCatalogJson() {
        Map<String, List<Catelog2Vo>> map = categoryService.getCatalogJson();
        return map;
    }

    @ResponseBody
    @GetMapping("/hello")
    public String hello() {
        //1.获取同一把锁，只要锁的名字一样，就是同一把锁
        RLock lock = redisson.getLock("my-lock");

        //2.加锁
        //1）、锁的自动续期：如果执行业务超长，锁会自动续期
        //2）、如果线程结束了，就不会续期了，到了30s之后就会被释放。如果业务时间很长，看门狗发现还在运行，才会续期
        lock.lock(); //阻塞式等待
        try{
            System.out.println("加锁成功，执行业务....." + Thread.currentThread().getId());
            Thread.sleep(60000);
        } catch (Exception e) {

        }finally {
            //3.解锁 假设解锁代码未运行 是否会出现解锁
            System.out.println("释放锁...." + Thread.currentThread().getId());
            lock.unlock();
        }

        return "hello";
    }
}
