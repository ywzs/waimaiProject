package com.ywzs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ywzs.entity.OrderDetail;
import com.ywzs.service.OrderDetailService;
import com.ywzs.mapper.OrderDetailMapper;
import org.springframework.stereotype.Service;

/**
* @author ASUS
* @description 针对表【order_detail(订单明细表)】的数据库操作Service实现
* @createDate 2023-05-05 16:41:20
*/
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail>
    implements OrderDetailService{

}




