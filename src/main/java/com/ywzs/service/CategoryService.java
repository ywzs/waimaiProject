package com.ywzs.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ywzs.common.R;
import com.ywzs.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author ASUS
* @description 针对表【category(菜品及套餐分类)】的数据库操作Service
* @createDate 2023-05-05 16:41:03
*/
public interface CategoryService extends IService<Category> {

    R<Page<Category>> getPage(int page, int pageSize);

    R<String> removeCategory(Long id);
}
