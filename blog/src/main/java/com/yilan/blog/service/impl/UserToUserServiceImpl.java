package com.yilan.blog.service.impl;

import com.yilan.blog.entity.UserEntity;
import com.yilan.blog.service.UserService;
import com.yilan.blog.utils.JwtTokenUtil;
import com.yilan.blog.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yilan.blog.utils.PageUtils;
import com.yilan.blog.utils.Query;

import com.yilan.blog.dao.UserToUserDao;
import com.yilan.blog.entity.UserToUserEntity;
import com.yilan.blog.service.UserToUserService;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;


@Service("userToUserService")
public class UserToUserServiceImpl extends ServiceImpl<UserToUserDao, UserToUserEntity> implements UserToUserService {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserToUserEntity> page = this.page(
                new Query<UserToUserEntity>().getPage(params),
                new QueryWrapper<UserToUserEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 判断用户之间是否已关注，返回用户之间的关系状态值，0-好友，1-申请中，2-fromUser屏蔽toUser
     * @param to_user_id, token
     */
    @Override
    public R isFollow(HttpServletRequest request, int to_user_id) {
        String jwtToken = request.getHeader(jwtTokenUtil.getHeader());
        if (!StringUtils.isEmpty(jwtToken)) {
            String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            UserEntity userEntity = userService.getBaseMapper().selectOne(new QueryWrapper<UserEntity>().eq("username", username));
            UserToUserEntity userToUserEntity = baseMapper.selectOne(new QueryWrapper<UserToUserEntity>().eq("from_user_id", userEntity.getId()).eq("to_user_id", to_user_id));
            if(userToUserEntity == null){
                return R.ok().put("res", "未关注");
            }
            else{
                if(userToUserEntity.getStatus() == 0){
                    return R.ok().put("res","已关注");
                }
                else if(userToUserEntity.getStatus() == 1){
                    return R.ok().put("res","申请中");
                }
                else if(userToUserEntity.getStatus() == 2){
                    return R.ok().put("res","已被屏蔽");
                }
            }
        }
        return R.ok().put("res","出现未知异常，请重新登录");
    }

    @Override
    public R deleteFollow(HttpServletRequest request, int to_user_id) {
        String jwtToken = request.getHeader(jwtTokenUtil.getHeader());
        if(!StringUtils.isEmpty(jwtToken)){
            String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            UserEntity fromUserEntity = userService.getBaseMapper().selectOne(new QueryWrapper<UserEntity>().eq("username", username));
            baseMapper.delete(new QueryWrapper<UserToUserEntity>().eq("from_user_id",fromUserEntity.getId()).eq("to_user_id",to_user_id));
            return R.ok().put("code",true);
        }
        else
            return R.ok().put("code",false);
    }

}