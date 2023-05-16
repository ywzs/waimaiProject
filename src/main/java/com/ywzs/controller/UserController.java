package com.ywzs.controller;


import com.ywzs.common.R;
import com.ywzs.entity.User;
import com.ywzs.service.UserService;
import com.ywzs.utils.SMSUtils;
import com.ywzs.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/sendMsg")
    public R<String> sendCode(@RequestBody User user, HttpSession session) {
        if (user.getPhone() != null) {
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("发送了手机验证码：{}", code);
            //调用阿里云api
//            SMSUtils.sendMessage("红豆外卖","ss", user.getPhone(), code);
            session.setAttribute(user.getPhone(), code);
            return R.success("发送验证码成功");
        }
        return R.error("服务异常");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map<String, String> map, HttpSession session) {
        if (map != null) {
            String phone = map.get("phone");
            Object code = session.getAttribute(phone);
            if (code == null || !code.toString().equals(map.get("code"))) {
                return R.error("验证码错误");
            }
            User user = userService.query().eq("phone", phone).one();
            if (user==null){
                //用户注册
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            //用户登录
            session.setAttribute("User",user.getId());
            return R.success(user);
        }
        return R.error("验证码错误");
    }
}
