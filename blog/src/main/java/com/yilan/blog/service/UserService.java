package com.yilan.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yilan.blog.utils.PageUtils;
import com.yilan.blog.entity.UserEntity;
import com.yilan.blog.utils.R;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 
 *
 * @author GroupOne
 * @email ${email}
 * @date 2020-09-11 11:08:28
 */
public interface UserService extends IService<UserEntity> {

    PageUtils queryPage(Map<String, Object> params);

    R findIfUniqueUserByUsername(String username);

    R findIfUniqueUserByEmail(String email);

    R registerProcessing(UserEntity userEntity);

    R verifying(String email, String verification);

    R submitInfo(UserEntity userEntity);

    R findIfExistsUserByEmail(UserEntity userEntity);

    R resetPwdVerification(UserEntity userEntity);

    R resetPwdVerifying(UserEntity userEntity);

    R resetPassword(UserEntity userEntity);

    R canUsernameChange(UserEntity userEntity);

    R searchUsersByParams(UserEntity userEntity);

    R searchAdminsByParams(UserEntity userEntity);

    R searchAllUsers();

    R searchInfo(HttpServletRequest request);

    R updateInfo(UserEntity userEntity, HttpServletRequest request);

    R uploadAvatar(MultipartFile file, HttpServletRequest request);

    R profilePage(int id);

    R followOne(HttpServletRequest request, int to_user_id);

    R updateUserStatus(int id,int islock);

    R changeRoles(UserEntity userEntity);

    R insertAdmin(UserEntity userEntity);

    R searchRolesByToken(HttpServletRequest request);

    R findUsersByPages(HttpServletRequest request, UserEntity userEntity);

    R searchToUserByUserId(int id);
}

