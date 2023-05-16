package com.ywzs.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ywzs.Dto.DishDto;
import com.ywzs.common.R;
import com.ywzs.entity.Dish;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author ASUS
* @description 针对表【dish(菜品管理)】的数据库操作Service
* @createDate 2023-05-05 16:41:10
*/
public interface DishService extends IService<Dish> {

    R<Page<DishDto>> getPage(int page, int pageSize, String name);

    R<String> saveDish(DishDto dishDto);

    R<DishDto> getOneDish(Long id);

    R<String> updateDish(DishDto dishDto);

    R<List<DishDto>> listDish(Long categoryId);
}
