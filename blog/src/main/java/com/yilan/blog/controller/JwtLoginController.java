package com.yilan.blog.controller;

import com.yilan.blog.entity.UserEntity;
import com.yilan.blog.service.impl.JwtAuthService;
import com.yilan.blog.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtLoginController {
    @Autowired
    JwtAuthService jwtAuthService;

    /**
     * 登录方法
     *
     * @param username 用户名
     * @param password 密码
     * @return 结果
     */
    @PostMapping({"/login","/"})
    public R login(@RequestBody UserEntity userEntity) {
        String token = jwtAuthService.login(userEntity.getUsername(),userEntity.getPassword());
        return R.ok().put("token",token);
    }

    @PostMapping("/adminLogin")
    public R adminLogin(@RequestBody UserEntity userEntity) {
        String token = jwtAuthService.adminLogin(userEntity.getUsername(),userEntity.getPassword());
        return R.ok().put("token",token);
    }
}
