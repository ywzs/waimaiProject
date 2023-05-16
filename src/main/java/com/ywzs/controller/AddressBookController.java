package com.ywzs.controller;

import com.ywzs.common.R;
import com.ywzs.entity.AddressBook;
import com.ywzs.service.AddressBookService;
import com.ywzs.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {
    @Autowired
    AddressBookService addressBookService;

    //新增
    @PostMapping
    public R<AddressBook> save(@RequestBody AddressBook addressBook){
        addressBook.setUserId(UserHolder.getId());
        addressBookService.save(addressBook);
        return R.success(addressBook);
    }
    //设置默认地址
    @PutMapping("/default")
    public R<AddressBook> setDefault(@RequestBody AddressBook addressBook){
        addressBookService.update().eq("user_id", UserHolder.getId()).set("is_default",0).update();
        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);
        return R.success(addressBook);
    }
    //查询
    @GetMapping("/{id}")
    public R<AddressBook> get(@PathVariable Long id){
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook==null){
            return R.error("没有查到");
        }
        return R.success(addressBook);
    }
    //查询默认地址
    @GetMapping("/default")
    public R<AddressBook> getDefault(){
        AddressBook one = addressBookService.query().eq("user_id", UserHolder.getId()).eq("is_default", 1).one();
        if (null==one){
            return R.error("你还未设置默认地址");
        }
        return R.success(one);
    }
    //条件查询
    @GetMapping("/list")
    public R<List<AddressBook>> getList(AddressBook addressBook){
        List<AddressBook> addressBooks = addressBookService.query()
                .eq("user_id", UserHolder.getId()).orderByDesc("update_time").list();
        return R.success(addressBooks);
    }
    //修改地址
    @PutMapping
    public R<String> update(@RequestBody AddressBook addressBook){
        addressBookService.updateById(addressBook);
        return R.success("修改成功");
    }
}
