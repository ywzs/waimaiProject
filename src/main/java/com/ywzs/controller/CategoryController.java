package com.ywzs.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ywzs.common.R;
import com.ywzs.entity.Category;
import com.ywzs.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @PostMapping
    public R<String> addCategory(@RequestBody Category category){
        categoryService.save(category);
        return R.success("新增成功");
    }
    @GetMapping("/page")    //默认参数就是在Request中获取，可以不加@ 但是一定要保证参数名称一致
    public R<Page<Category>> getPage(@RequestParam("page") int page,@RequestParam("pageSize") int pageSize){
        return categoryService.getPage(page,pageSize);
    }
    @DeleteMapping
    public R<String> delete(Long ids){
//        categoryService.removeById(id);
//        return R.success("删除成功");
        //由于我们要保证逻辑一致性，不能主动加外键就逻辑维护外键
        return categoryService.removeCategory(ids);
    }
    @PutMapping
    public R<String> update(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("修改成功");
    }

    /**
     * 列出菜品分类
     * @param type  是菜品还是套餐
     * @return 分类
     */
    @GetMapping("/list")
    public R<List<Category>> list(@RequestParam(value = "type",required = false) String type){
        List<Category> categoryList;
        if (type==null||"".equals(type)){
             categoryList= categoryService.query().orderByAsc("sort").orderByDesc("update_time").list();
        }else {
            categoryList = categoryService.query().eq("type", type)
                    .orderByAsc("sort").orderByDesc("update_time").list();
        }
        return R.success(categoryList);
    }
}
