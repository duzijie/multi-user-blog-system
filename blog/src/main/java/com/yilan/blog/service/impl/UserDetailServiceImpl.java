package com.yilan.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yilan.blog.entity.UserEntity;
import com.yilan.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userService.getOne(new QueryWrapper<UserEntity>().eq("username",username));
        if (userEntity == null){
            throw new UsernameNotFoundException("登录用户:" + username + " 不存在");
        }
        //将数据库的roles解析为UserDetails的权限集
        //AuthorityUtils.commaSeparatedStringToAuthorityList将逗号分隔的字符串转成权限对象列表
        userEntity.setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList(userEntity.getRoles()));
        return userEntity;
    }
}
