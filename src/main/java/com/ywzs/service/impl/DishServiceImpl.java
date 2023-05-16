package com.ywzs.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ywzs.Dto.DishDto;
import com.ywzs.common.R;
import com.ywzs.entity.Category;
import com.ywzs.entity.Dish;
import com.ywzs.entity.DishFlavor;
import com.ywzs.service.CategoryService;
import com.ywzs.service.DishFlavorService;
import com.ywzs.service.DishService;
import com.ywzs.mapper.DishMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ASUS
 * @description 针对表【dish(菜品管理)】的数据库操作Service实现
 * @createDate 2023-05-05 16:41:10
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish>
        implements DishService {
    @Autowired
    DishFlavorService dishFlavorService;
    @Autowired
    CategoryService categoryService;

    /**
     * 分页查询
     *
     * @param page     当前页数
     * @param pageSize 页面大小
     * @param name     按名称查询
     * @return 一页数据
     */
    @Override
    public R<Page<DishDto>> getPage(int page, int pageSize, String name) {
        Page<Dish> dishPage = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(name!=null,Dish::getName, name).orderByAsc(Dish::getSort);
        page(dishPage,wrapper);
        //拷贝属性到dishDto
        BeanUtil.copyProperties(dishPage, dishDtoPage, "records");
        //根据分类id查询分类名称
        List<Dish> records = dishPage.getRecords();
        List<DishDto> end = records.stream().map((dish) -> {
            DishDto dishDto = new DishDto();
            BeanUtil.copyProperties(dish, dishDto);
            Category category = categoryService.getById(dish.getCategoryId());
            if (category == null) {
                //可能空指针,如果没查到就有可能空指针
                return dishDto;
            }
            dishDto.setCategoryName(category.getName());
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(end);
        return R.success(dishDtoPage);
    }

    /**
     * 新增加菜品
     *
     * @param dishDto 数据对象
     * @return 是否成功
     */
    @Override
    @Transactional   //涉及多张表的造作，开启事务
    public R<String> saveDish(DishDto dishDto) {
        //保存信息
        save(dishDto);
        Long dishId = dishDto.getId();
        //保存到口味表
//        dishDto.getFlavors().forEach(dishFlavor -> {
//            dishFlavor.setDishId(dishId);
//            dishFlavorService.save(dishFlavor);
//        });
        //使用stream流
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map(dishFlavor -> {
            dishFlavor.setDishId(dishId);
            return dishFlavor;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
        return R.success("保存成功");
    }

    /**
     * 单条修改回显
     *
     * @param id 请求id
     * @return 回显dish数据
     */
    @Override
    public R<DishDto> getOneDish(Long id) {
        Dish dish = getById(id);
        Category category = categoryService.getById(dish.getCategoryId());
        if (category == null) {
            //不存在该数据
            return R.error("不存在");
        }
        DishDto dishDto = new DishDto();
        BeanUtil.copyProperties(dish, dishDto);
        dishDto.setCategoryName(category.getName());
        List<DishFlavor> dishFlavors = dishFlavorService.query().eq("dish_id", id).list();
        if (dishFlavors == null) {
            //不存在数据
            return R.success(dishDto);
        }
        dishDto.setFlavors(dishFlavors);
        return R.success(dishDto);
    }

    @Override
    @Transactional
    public R<String> updateDish(DishDto dishDto) {
        //第1张表  菜品表
        updateById(dishDto);
        //第二张表 口味表
        //dishFlavorService.remove(dishFlavorService.query().eq("dish_id", dishDto.getId()));
        List<DishFlavor> flavors = dishDto.getFlavors();
        if (flavors != null) {
            //有口味  对于在另外一张表的口味，可以先删除后添加
            Long id = dishDto.getId();
            LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(DishFlavor::getDishId, id);
            dishFlavorService.remove(wrapper);
            flavors.forEach(dishFlavor -> dishFlavor.setDishId(id));
            dishFlavorService.saveBatch(flavors);
        }
        return R.success("修改成功");
    }

    @Override
    public R<List<DishDto>> listDish(Long categoryId) {
        List<Dish> dishes = query().eq("category_id", categoryId).eq("status",1) // 启售
                .orderByDesc("create_time").list();
        List<DishDto> dishDtos = new ArrayList<>();
        dishes.forEach(dish -> {
            DishDto dishDto = new DishDto();
            BeanUtil.copyProperties(dish,dishDto);
            List<DishFlavor> dishFlavors = dishFlavorService.query().eq("dish_id", dish.getId()).list();
            dishDto.setFlavors(dishFlavors);
            dishDtos.add(dishDto);
        });
        return R.success(dishDtos);
    }
}




