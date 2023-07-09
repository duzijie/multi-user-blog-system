package com.yilan.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yilan.blog.entity.UserEntity;
import com.yilan.blog.service.UserService;
import com.yilan.blog.utils.JwtTokenUtil;
import com.yilan.blog.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class GlobalController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @RequestMapping("/refreshToken")
    public R refreshToken(HttpServletRequest request){
        String jwtToken = request.getHeader(jwtTokenUtil.getHeader());
        if (!StringUtils.isEmpty(jwtToken)) {
            String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            //获取不出username 说明令牌已过期或无效，则返回空串
            if(username == null) {
                return R.ok().put("code", false).put("newToken", "");
            }
            UserEntity userEntity = userService.getOne(new QueryWrapper<UserEntity>().eq("username",username));
            //如果令牌依旧有效，则刷新令牌
            if(jwtTokenUtil.validateToken(jwtToken,userEntity)){
                String newToken = jwtTokenUtil.refreshToken(jwtToken);
                return R.ok().put("code",true).put("newToken",newToken);
            }
        }
        //如果令牌已过期或无效，则返回空串
        return R.ok().put("code",false).put("newToken","");
    }
}
