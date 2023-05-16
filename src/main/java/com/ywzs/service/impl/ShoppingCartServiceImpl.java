package com.ywzs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ywzs.entity.ShoppingCart;
import com.ywzs.service.ShoppingCartService;
import com.ywzs.mapper.ShoppingCartMapper;
import org.springframework.stereotype.Service;

/**
* @author ASUS
* @description 针对表【shopping_cart(购物车)】的数据库操作Service实现
* @createDate 2023-05-05 16:41:31
*/
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
    implements ShoppingCartService{

}




