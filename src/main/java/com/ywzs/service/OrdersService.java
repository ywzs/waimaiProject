package com.ywzs.service;

import com.ywzs.common.R;
import com.ywzs.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author ASUS
* @description 针对表【orders(订单表)】的数据库操作Service
* @createDate 2023-05-05 16:41:23
*/
public interface OrdersService extends IService<Orders> {

    R<Orders> submit(Orders orders);

}
