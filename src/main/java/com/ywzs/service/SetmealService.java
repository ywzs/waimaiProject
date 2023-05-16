package com.ywzs.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ywzs.Dto.SetMealDto;
import com.ywzs.common.R;
import com.ywzs.entity.Category;
import com.ywzs.entity.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author ASUS
* @description 针对表【setmeal(套餐)】的数据库操作Service
* @createDate 2023-05-05 16:41:25
*/
public interface SetmealService extends IService<Setmeal> {


    R<Page<SetMealDto>> getPage(int page, int pageSize,String name);

    R<String> saveSetMeal(SetMealDto setMealDto);

    R<SetMealDto> getSetmeal(Long setmealId);

    R<String> updateSetmeal(SetMealDto setMealDto);

    R<String> delSetmeal(List<Long> ids);

    R<List<SetMealDto>> listSetmeal(Setmeal setmeal);
}
