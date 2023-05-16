package com.ywzs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ywzs.entity.DishFlavor;
import com.ywzs.service.DishFlavorService;
import com.ywzs.mapper.DishFlavorMapper;
import org.springframework.stereotype.Service;

/**
* @author ASUS
* @description 针对表【dish_flavor(菜品口味关系表)】的数据库操作Service实现
* @createDate 2023-05-05 16:41:14
*/
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor>
    implements DishFlavorService{

}




