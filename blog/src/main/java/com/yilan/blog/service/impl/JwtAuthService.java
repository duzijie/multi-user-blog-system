package com.yilan.blog.service.impl;

import com.yilan.blog.entity.UserEntity;
import com.yilan.blog.utils.JwtTokenUtil;
import com.yilan.blog.utils.R;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class JwtAuthService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    /**
     * 登录认证换取JWT令牌
     *
     * @param username 用户名
     * @param password 密码
     * @return 结果
     */
    public String login(String username, String password) {
        //用户验证
        Authentication authentication = null;
        try {
            //该方法会去调用UserDetailServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
            System.out.println("authentication====" + authentication);
        } catch (Exception e) {
            throw new RuntimeException("用户名或密码错误");
        }
        UserEntity loginUser = (UserEntity) authentication.getPrincipal();
        //生成token
        return jwtTokenUtil.generateToken(loginUser);
    }

    public String adminLogin(String username, String password) {
        //用户验证
        Authentication authentication = null;
        try{
            //该方法会去调用UserDetailServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
        } catch (Exception e){
            throw new RuntimeException("用户名或密码错误");
        }
        UserEntity loginUser = (UserEntity) authentication.getPrincipal();
        if(loginUser.getRoles().equals("ROLE_USER")) {
            throw new RuntimeException("该用户不具有管理员权限");
        }
        //生成token
        return jwtTokenUtil.generateToken(loginUser);
    }
}
