package com.ywzs.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ywzs.Dto.SetMealDto;
import com.ywzs.common.R;
import com.ywzs.entity.Category;
import com.ywzs.entity.Setmeal;
import com.ywzs.service.SetmealService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class setmealController {
    @Resource
    SetmealService setmealService;
    //分页查询
    @GetMapping("/page")
    public R<Page<SetMealDto>> getPage(int page, int pageSize,String name){
        return setmealService.getPage(page,pageSize,name);
    }
    //保存
    @PostMapping
    public R<String> save(@RequestBody SetMealDto setMealDto){
        return setmealService.saveSetMeal(setMealDto);
    }
    //修改时回显一个
    @GetMapping("/{setmealId}")
    public R<SetMealDto> getOne(@PathVariable("setmealId") Long setmealId){
        return setmealService.getSetmeal(setmealId);
    }
    //更新
    @PutMapping
    public R<String> update(@RequestBody SetMealDto setMealDto){
        return setmealService.updateSetmeal(setMealDto);
    }
    //批量删除
    @DeleteMapping
    public R<String> delSetmeal(@RequestParam List<Long> ids){
        return setmealService.delSetmeal(ids);
    }
    //批量起售停售
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable Long status,@RequestParam List<Long> ids){
        ids.forEach(id-> setmealService.update().eq("id",id).set("status",status).update());
        return R.success("修改成功");
    }
    @GetMapping("/list")
    public R<List<SetMealDto>> list(Setmeal setmeal){
        return setmealService.listSetmeal(setmeal);
    }
}
