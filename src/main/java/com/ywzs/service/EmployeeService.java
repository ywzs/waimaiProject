package com.ywzs.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ywzs.common.R;
import com.ywzs.entity.Employee;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author ASUS
* @description 针对表【employee(员工信息)】的数据库操作Service
* @createDate 2023-05-05 16:41:17
*/
public interface EmployeeService extends IService<Employee> {

    R<Employee> login(Employee employee);

    R<Page<Employee>> getPage(int page, int pageSize, String name);
}
