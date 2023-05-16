package com.ywzs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ywzs.entity.User;
import com.ywzs.service.UserService;
import com.ywzs.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author ASUS
* @description 针对表【user(用户信息)】的数据库操作Service实现
* @createDate 2023-05-05 16:41:34
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




