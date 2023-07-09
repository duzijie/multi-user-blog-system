package com.yilan.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Qualifier("userDetailServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/test")
    public String login(@RequestParam String username, @RequestParam String password){

        System.out.println("明文密码：" + password);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodePassword = bCryptPasswordEncoder.encode(password);
        System.out.println("密文密码：" + encodePassword);
        String databaseEncodePassword = userDetailsService.loadUserByUsername("duzijie").getPassword();
        System.out.println("数据库中存储的密文密码：" + databaseEncodePassword);
        boolean flag = bCryptPasswordEncoder.matches(password,encodePassword);
        System.out.println(flag);
        return "success";
    }

}
