package com.ywzs.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ywzs.common.R;
import com.ywzs.entity.ShoppingCart;
import com.ywzs.service.ShoppingCartService;
import com.ywzs.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    ShoppingCartService shoppingCartService;

    @GetMapping("/list")
    public R<List<ShoppingCart>> list() {
        Long userId = UserHolder.getId();
        List<ShoppingCart> shoppingCarts = shoppingCartService.query().eq("user_id", userId).orderByAsc("create_time").list();
        return R.success(shoppingCarts);
    }

    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        Long userId = UserHolder.getId();
        shoppingCart.setUserId(userId);
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        Long dishId = shoppingCart.getDishId();
        if (dishId != null) {  //菜品口味是否一致
            String dishFlavor = shoppingCart.getDishFlavor();
            wrapper.eq(ShoppingCart::getDishId, dishId).eq(!StrUtil.isBlank(dishFlavor), ShoppingCart::getDishFlavor, dishFlavor);
        } else {
            wrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        wrapper.eq(ShoppingCart::getUserId, userId);
        ShoppingCart one = shoppingCartService.getOne(wrapper);
        if (one != null) {
            one.setNumber(one.getNumber() + 1);
            shoppingCartService.updateById(one);
        } else {
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            one = shoppingCart;
        }
        return R.success(one);
//        if (shoppingCart.getDishId()!=null){
//            //添加菜品
//            ShoppingCart one = shoppingCartService.query().eq("user_id", userId).eq("dish_id", shoppingCart.getDishId()).one();
//            if (one!=null){
//                one.setNumber(one.getNumber()+1);
//                shoppingCartService.updateById(one);
//            }else {
//                shoppingCart.setNumber(1);
//                shoppingCartService.save(shoppingCart);
//            }
//        }else {
//            //添加套餐
//            ShoppingCart one = shoppingCartService.query().eq("user_id", userId).eq("setmeal_id", shoppingCart.getSetmealId()).one();
//            if (one!=null){
//                one.setNumber(one.getNumber()+1);
//                shoppingCartService.updateById(one);
//            }else {
//                shoppingCart.setNumber(1);
//                shoppingCartService.save(shoppingCart);
//            }
//        }

    }

    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart) {
        Long userId = UserHolder.getId();
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        if (dishId != null) {
            String dishFlavor = shoppingCart.getDishFlavor();
            wrapper.eq(ShoppingCart::getDishId, dishId).eq(!StrUtil.isBlank(dishFlavor), ShoppingCart::getDishFlavor, dishFlavor);
        } else {
            wrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        wrapper.eq(ShoppingCart::getUserId, userId);
        ShoppingCart one = shoppingCartService.getOne(wrapper);
        Integer number = one.getNumber();
        if (number > 1) {
            one.setNumber(number - 1);
            shoppingCartService.updateById(one);
        } else {
            one.setNumber(0);
            shoppingCartService.removeById(one);
        }
        return R.success(one);
    }

    @DeleteMapping("/clean")
    public R<String> clean(){
        Long id = UserHolder.getId();
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,id);
        shoppingCartService.remove(wrapper);
        return R.success("清空购物车成功");
    }

}