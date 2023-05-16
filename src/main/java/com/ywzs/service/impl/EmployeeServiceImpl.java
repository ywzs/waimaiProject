package com.ywzs.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ywzs.common.R;
import com.ywzs.entity.Employee;
import com.ywzs.service.EmployeeService;
import com.ywzs.mapper.EmployeeMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
* @author ASUS
* @description 针对表【employee(员工信息)】的数据库操作Service实现
* @createDate 2023-05-05 16:41:17
*/
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService{

    @Override
    public R<Employee> login(Employee employee) {
        // TODO 首先用md5对有户密码进行加密
        employee.setPassword(DigestUtil.md5Hex(employee.getPassword()));
        Employee one = query().eq("username", employee.getUsername()).one();
        if (one == null){
            return R.error("用户名不存在!");
        }
        if (!one.getPassword().equals(employee.getPassword())){
            return R.error("密码错误!");
        }
        //接下来要判断该员工是否被禁用
        if (one.getStatus()==0){
            return R.error("用户被禁用");
        }
        return R.success(one);
    }

    /**
     * 分页查询
     * @param page  页面
     * @param pageSize 页面大小
     * @param name  查询字段
     * @return  数据
     */
    @Override
    public R<Page<Employee>> getPage(int page, int pageSize, String name) {
        Page<Employee>  pageInfo = new Page<>(page,pageSize);
        //构造查询条件
        LambdaQueryWrapper<Employee> wrapper= new LambdaQueryWrapper<>();
        wrapper.like(!"null".equals(name),Employee::getName,name);  //只有当前面的表达式成立才会添加
        wrapper.orderByDesc(Employee::getCreateTime);  //添加排序条件
        page(pageInfo,wrapper);
        return R.success(pageInfo);
    }
}




