package com.ywzs.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ywzs.Dto.DishDto;
import com.ywzs.common.CustomException;
import com.ywzs.common.R;
import com.ywzs.entity.Dish;
import com.ywzs.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    DishService dishService;

    /**
     * 分页查询
     *
     * @return 一页数据
     */
    @GetMapping("/page")
    public R<Page<DishDto>> getPage(int page, int pageSize, String name) {
        return dishService.getPage(page, pageSize, name);
    }

    /**
     * 新增数据
     *
     * @param dishDto 数据
     * @return 成功
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.debug(dishDto.toString());
        return dishService.saveDish(dishDto);
    }

    /**
     * 查询单条数据
     */
    @GetMapping("/{id}")
    public R<DishDto> getOne(@PathVariable Long id) {
        return dishService.getOneDish(id);
    }

    /**
     * 修改数据
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        return dishService.updateDish(dishDto);
    }

    /**
     * 批量删除
     */
    @DeleteMapping
    public R<String> delDish(Long[] ids) {
//        log.warn(Arrays.toString(ids));
        List<Long> dishIds = new ArrayList<>();
        Collections.addAll(dishIds,ids);
        int count = dishService.query().in("id", dishIds).eq("status", 1).count().intValue();
        if (count>0){
            throw new CustomException("不能删除正在售卖的商品");
        }
        dishService.removeBatchByIds(dishIds);
        return R.success("删除成功");
    }
    /**
     * 批量启用或禁用
     */
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable("status") int status,@RequestParam("ids") Long[] ids){
        List<Long> dishIds = new ArrayList<>();
        Collections.addAll(dishIds,ids);
        dishService.update().in("id",dishIds).set("status",status).update();
//        dishIds.forEach(id->{
//            dishService.update().eq("id",id).set("status",status).update();
//        });
        return R.success("修改成功");
    }

    /**
     * 按类别展示菜品
     */
    @GetMapping("/list")
    public R<List<DishDto>>getDishByCategory(Long categoryId){
        return dishService.listDish(categoryId);
    }
}
