package com.ywzs.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ywzs.Dto.SetMealDto;
import com.ywzs.common.CustomException;
import com.ywzs.common.R;
import com.ywzs.entity.Category;
import com.ywzs.entity.Setmeal;
import com.ywzs.entity.SetmealDish;
import com.ywzs.service.CategoryService;
import com.ywzs.service.SetmealDishService;
import com.ywzs.service.SetmealService;
import com.ywzs.mapper.SetmealMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ASUS
 * @description 针对表【setmeal(套餐)】的数据库操作Service实现
 * @createDate 2023-05-05 16:41:25
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal>
        implements SetmealService {
    @Lazy
    @Autowired
    CategoryService categoryService;
    @Autowired
    SetmealDishService setmealDishService;

    @Override
    public R<Page<SetMealDto>> getPage(int page, int pageSize, String name) {
        Page<Setmeal> myPage = new Page<>(page, pageSize);
        Page<SetMealDto> end = new Page<>(page, pageSize);
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Setmeal::getCreateTime);
        //添加姓名字段
        wrapper.like(!StrUtil.isBlank(name), Setmeal::getName, name);
        page(myPage, wrapper);
        BeanUtil.copyProperties(myPage, end, "records");
        List<SetMealDto> records;
        List<Setmeal> list = myPage.getRecords();
        if (list == null || list.isEmpty()) {
            return R.success(null);
        }
        records = list.stream().map(setmeal -> {
            SetMealDto setMealDto = new SetMealDto();
            BeanUtil.copyProperties(setmeal, setMealDto);
            Category category = categoryService.query().eq("id", setMealDto.getCategoryId()).one();
            if (category != null) {
                setMealDto.setCategoryName(category.getName());
            }
            return setMealDto;
        }).collect(Collectors.toList());
        end.setRecords(records);
        return R.success(end);
    }

    @Override
    public R<String> saveSetMeal(SetMealDto setMealDto) {
        save(setMealDto);
        List<SetmealDish> setmealDishes = setMealDto.getSetmealDishes().stream()
                .peek(item -> item.setSetmealId(setMealDto.getId())).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
        return R.success("修改成功");
    }

    @Override
    public R<SetMealDto> getSetmeal(Long setmealId) {
        Setmeal setmeal = getById(setmealId);
        SetMealDto setMealDto = new SetMealDto();
        BeanUtil.copyProperties(setmeal, setMealDto);
        List<SetmealDish> setmealDishes = setmealDishService.query().eq("setmeal_id", setmealId).list();
        setMealDto.setSetmealDishes(setmealDishes);
        return R.success(setMealDto);
    }

    @Override
    public R<String> updateSetmeal(SetMealDto setMealDto) {
        updateById(setMealDto);
        //另外一张表需要先删除后添加
        Long setmealId = setMealDto.getId();
        QueryWrapper<SetmealDish> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("setmeal_id", setmealId);
        setmealDishService.remove(queryWrapper);
        List<SetmealDish> setmealDishes = setMealDto.getSetmealDishes()
                .stream().peek(setmealDish -> setmealDish.setSetmealId(setMealDto.getId()))
                .collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
        return R.success("修改成功");
    }
    //批量删除
    @Override
    @Transactional
    public R<String> delSetmeal(List<Long> ids) {
        int count = query().in("id", ids).eq("status", 1).count().intValue();
        if (count>0){
            throw new CustomException("套餐正在售卖中，不能删除");
        }
        removeByIds(ids);
        QueryWrapper<SetmealDish> wrapper = new QueryWrapper<>();
        wrapper.in("setmeal_id",ids);
        setmealDishService.remove(wrapper);
        return R.success("删除成功");
    }

    @Override
    public R<List<SetMealDto>> listSetmeal(Setmeal setmeal1) {
        Long categoryId = setmeal1.getCategoryId();
        List<Setmeal> setmeals = query().eq("category_id", categoryId).eq("status", 1).list();
        List<SetMealDto> setMealDtos = new ArrayList<>();
        setmeals.forEach(setmeal -> {
            SetMealDto mealDto = new SetMealDto();
            BeanUtil.copyProperties(setmeal,mealDto);
            Long setmealId = setmeal.getId();
            List<SetmealDish> dishes = setmealDishService.query().eq("setmeal_id", setmealId).list();
            mealDto.setSetmealDishes(dishes);
            setMealDtos.add(mealDto);
        });
        return R.success(setMealDtos);
    }
}




