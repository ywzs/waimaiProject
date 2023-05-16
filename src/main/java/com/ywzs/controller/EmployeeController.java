package com.ywzs.controller;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ywzs.common.R;
import com.ywzs.entity.Employee;
import com.ywzs.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @PostMapping("/login")
    R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        R<Employee> login = employeeService.login(employee);
        if (login.getCode() == 0) {
            return login;
        }
        HttpSession session = request.getSession();
        session.setAttribute("Employee", login.getData().getId());
        return login;
    }

    @PostMapping("/logout")
    R<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("Employee");
        return R.success("退出成功");
    }

    @PostMapping
    R<String> addEmployee(HttpServletRequest request, @RequestBody Employee employee) {
//        //因为用户名唯一.先进行判断   （每一次判断都要查询数据库，加大数据库压力）
//        String username = employee.getUsername();
//        Employee one = employeeService.query().eq("username", username).one();
//        if (one!=null){
//            return R.error("用户名已经存在");
//        }
        //设置初始密码,记得md5加密
        employee.setPassword(DigestUtil.md5Hex("123456"));
        //添加一些值 （  用mp的值自动填充  ）
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        Long userId = (Long) request.getSession().getAttribute("Employee");
//        employee.setCreateUser(userId);
//        employee.setUpdateUser(userId);

//        boolean success = employeeService.save(employee);
//        if (!success){
//            return R.error("服务异常");
//        }
        employeeService.save(employee);
        return R.success("添加成功");
    }

    @GetMapping("/page")
    public R<Page<Employee>> page(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize,
                                  @RequestParam(value = "name", defaultValue = "null") String name) {
        return employeeService.getPage(page, pageSize, name);
    }

    /**
     * 启用或者禁用员工
     *
     * @param request  请求
     * @param employee 员工
     * @return R
     */
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
//        employeeService.update().eq("id",employee.getId())
//                .set("status",employee.getStatus()==1 ? 1:0)
//                .update();    //基于与前端的约定
        employee.setStatus(employee.getStatus() == 1 ? 1 : 0);
//        Long Id = (Long) request.getSession().getAttribute("Employee");
//        employee.setUpdateUser(Id);
//        employee.setUpdateTime(LocalDateTime.now());
        employeeService.updateById(employee);  //修改
        return R.success("修改成功");
    }


    @GetMapping("/{id}")
    public R<Employee> getInfo(@PathVariable("id") Long id) {
//        Employee one = employeeService.query().eq("id", id).one();
        Employee one = employeeService.getById(id);
        if (one == null) {
            return R.error("用户不存在");
        }
        return R.success(one);
    }
}
