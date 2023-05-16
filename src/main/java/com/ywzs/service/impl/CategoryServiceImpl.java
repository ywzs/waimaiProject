package com.ywzs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ywzs.common.CustomException;
import com.ywzs.common.R;
import com.ywzs.entity.Category;
import com.ywzs.entity.Dish;
import com.ywzs.entity.Setmeal;
import com.ywzs.service.CategoryService;
import com.ywzs.mapper.CategoryMapper;
import com.ywzs.service.DishService;
import com.ywzs.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
* @author ASUS
* @description 针对表【category(菜品及套餐分类)】的数据库操作Service实现
* @createDate 2023-05-05 16:41:03
 * Spring构造器注入循环依赖的解决方案是@Lazy，其基本思路是：对于强依赖的对象，一开始并不注入对象本身，而是注入其代理对象，以便顺利完成实例的构造，形成一个完整的对象，这样与其它应用层对象就不会形成互相依赖的关系；当需要调用真实对象的方法时，通过TargetSource去拿到真实的对象[DefaultListableBeanFactory#doResolveDependency]，然后通过反射完成调用
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{
    @Lazy
    @Autowired
    DishService dishService;
    @Autowired
    SetmealService setmealService;

    @Override
    public R<Page<Category>> getPage(int page, int pageSize) {
        Page<Category> myPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Category::getSort);   //构建排序条件
        page(myPage, wrapper);
        return R.success(myPage);
    }

    @Override
    public R<String> removeCategory(Long id) {
        Category category = query().eq("id", id).one();
        if (category==null){
            return R.error("没有此分类");
        }
        if (category.getType()==1){
            //是菜品，查菜品
            Dish one = dishService.query().eq("category_id", id).one();
            if (one==null){
                //没有查到
                removeById(id);
                return R.success("删除成功");
            }
            throw new CustomException("不能删除一个有菜品的分类");
        }else {
            //是套餐,查套餐
            Setmeal one = setmealService.query().eq("category_id", id).one();
            if (one==null){
                removeById(id);
                return R.success("删除成功");
            }
            throw new CustomException("不能删除一个有套餐的分类");
        }
    }
}




