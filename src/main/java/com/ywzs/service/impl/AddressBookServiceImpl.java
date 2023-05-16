package com.ywzs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ywzs.entity.AddressBook;
import com.ywzs.service.AddressBookService;
import com.ywzs.mapper.AddressBookMapper;
import org.springframework.stereotype.Service;

/**
* @author ASUS
* @description 针对表【address_book(地址管理)】的数据库操作Service实现
* @createDate 2023-05-05 16:36:30
*/
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook>
    implements AddressBookService{

}




