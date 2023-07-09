package com.yilan.blog.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yilan.blog.utils.JwtTokenUtil;
import com.yilan.blog.utils.UploadUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.DigestUtils;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.yilan.blog.entity.UserEntity;
import com.yilan.blog.service.UserService;
import com.yilan.blog.utils.PageUtils;
import com.yilan.blog.utils.R;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;


/**
 * 
 *
 * @author GroupOne
 * @email ${email}
 * @date 2020-09-11 11:08:28
 */
@RestController
@RequestMapping("blogsystem/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 用户注册页在填写username时实时获取所填的username在数据库中是否唯一，
     * 唯一则可注册 返回true  不可注册返回false
     */
    @RequestMapping("/register/info/username{username}")
    public R findIfUniqueUserByUsername(@RequestParam(value = "username") String username){
        return userService.findIfUniqueUserByUsername(username);
    }

    /**
     * 逻辑同上函数findIfUniqueUserByUsername
     * 用户注册页在填写email时实时获取所填的email在数据库中是否唯一，
     * 唯一则可注册 返回true  不可注册返回false
     */
    @RequestMapping("/register/info/email{email}")
    public R findIfUniqueUserByEmail(@RequestParam(value = "email") String email){
        return userService.findIfUniqueUserByEmail(email);
    }
    /**
     * 这个函数发生在注册页面点击”获取“验证码时
     * 接收用户注册页面提交来的表单并通过邮箱发送验证码
     * 验证码 = MD5（电子邮箱）
     * @Param 只需要UserEntity里的email即可
     */
    @RequestMapping("/register/verification")
    public R registerProcessing(@RequestBody UserEntity userEntity) {
        return userService.registerProcessing(userEntity);
    }

    /**
     * 该函数用于前台使用Ajax实时请求验证码是否填写正确，以验证邮箱地址是否正确，返回true或false
     * @param email
     * @param verification
     * @return true or false
     */
    @RequestMapping("/register/verifying/{email}/{verification}")
    public R verifying(@PathVariable("email") String email, @PathVariable("verification") String verification) {
        return userService.verifying(email,verification);
    }

    /**
     * 正式把用户数据id email username password islock create_time（这个由handler自动生成） roles 录入user表
     * @param userEntity
     * @return
     */
    @RequestMapping("/register/submitInfo")
    public R submitInfo(@RequestBody UserEntity userEntity) {
        return userService.submitInfo(userEntity);
    }

    @RequestMapping("/findIfExistsUserByEmail")
    public R findIfExistsUserByEmail(@RequestBody UserEntity userEntity){
        return userService.findIfExistsUserByEmail(userEntity);
    }

    /**
     * 这个函数发生在用户在忘记密码页面点击“获取”验证码时
     * 系统给电子邮件发送验证码
     * 验证码 = MD5(电子邮箱 + 系统当前时间)
     * @param userEntity 只用到email
     * @return
     */
    @RequestMapping("/resetPwd/verification")
    public R resetPwdVerification(@RequestBody UserEntity userEntity) {
        return userService.resetPwdVerification(userEntity);
    }


    /**
     * 该函数用于前台使用Ajax实时请求验证码是否填写正确,返回true或false
     * @param userEntity，只需要里面的email verification
     * @return true or false
     */
    @RequestMapping("/resetPwd/verifying")
    public R resetPwdVerifying(@RequestBody UserEntity userEntity) {
        return userService.resetPwdVerifying(userEntity);
    }

    /**
     * 忘记密码页面检验username是否跟以前的一样，或者能否进行改变
     * @param userEntity，只需要email和username
     * @return
     */
    @RequestMapping("/resetPwd/canUsernameChange")
    public R canUsernameChange(@RequestBody UserEntity userEntity) {
        return userService.canUsernameChange(userEntity);
    }
    /**
     * 忘记密码页面所有表单信息提交后存入数据库
     * 记得把数据库中的verification字段清楚
     * @param userEntity，只需要 email username password verification
     * @return
     */
    @RequestMapping("/resetPassword")
    public R resetPassword(@RequestBody UserEntity userEntity){
        return userService.resetPassword(userEntity);
    }

    /**
     * 根据token返回用户的roles
     */
    @RequestMapping("/searchRolesByToken")
    public R searchRolesByToken(HttpServletRequest request){
        return userService.searchRolesByToken(request);
    }

    /**
     * 要拥有管理员权限：根据多个参数动态查询用户 其中 name 字段和 address 字段是模糊查询%name% %address%
     * 只需要接收：id name email username phone sex birthday address islock create_time introduction motto
     * 不需要接收：password avatar style_id roles verification
     */
    @RequestMapping("/admin/searchUsersByParams")
    public R searchUsersByParams(@RequestBody UserEntity userEntity){
        return userService.searchUsersByParams(userEntity);
    }


    /**
     * 要拥有超级权限：根据多个参数动态查询管理员 其中 name 字段和 address 字段是模糊查询%name% %address%
     * 只需要接收：id name email username phone sex birthday address islock create_time introduction motto
     * 不需要接收：password avatar style_id roles verification
     */
    @RequestMapping("/super/searchAdminsByParams")
    public R searchAdminsByParams(@RequestBody UserEntity userEntity){
        return userService.searchAdminsByParams(userEntity);
    }

    @RequestMapping("/admin/searchAllUsers")
    public R searchAllUsers(){
        return userService.searchAllUsers();
    }

    @RequestMapping("/admin/updateUserStatus")
    public R updateUserStatus(@RequestParam int id, @RequestParam int islock){
        return userService.updateUserStatus(id,islock);
    }

    /**
     * 管理员、超级管理员封锁普通用户的账号
     * @param id
     * @return islock: 0/1
     */
//    @RequestMapping("/admin/switchIslockToUser")
//    public R lockUser(@RequestBody UserEntity userEntity){
//        userEntity = userService.getById(userEntity.getId());
//        userEntity.setIslock(Math.abs(userEntity.getIslock() - 1));
//        userService.updateById(userEntity);
//        return R.ok().put("islock",userEntity.getIslock());
//    }

    //所有角色都可以查看自己个人页面的个人资料
    @RequestMapping("/searchInfo")
    public R searchInfo(HttpServletRequest request){
        return userService.searchInfo(request);
    }

    //所有角色都可以在自己个人页面修改个人资料 不包含上传头像功能
    @RequestMapping("/updateInfo")
    public R updateInfo(@RequestBody UserEntity userEntity, HttpServletRequest request) {
        return userService.updateInfo(userEntity,request);
    }

    /**
     * 个人页面上传头像，并把路径存入数据库
     * @return 路径
     */
    @PostMapping("/uploadAvatar")
    public R uploadAvatar(MultipartFile file, HttpServletRequest request){
        return userService.uploadAvatar(file,request);
    }


    /**
     * 根据user表id，返回用户个人页面所需的基本信息
     * @param id
     * @return
     */
    @RequestMapping("/profilePage")
    public R profilePage(@RequestParam int id){
        return userService.profilePage(id);
    }

    /**
     * 在他人博客主页点击“关注”按钮
     * 首先要处于“登录“状态才能关注。
     * @param token，to_user_id,
     */
    @RequestMapping("/followOne")
    public R followOne(HttpServletRequest request, @RequestParam("to_user_id") int to_user_id){
        return userService.followOne(request,to_user_id);
    }

    /**
     * 管理员和普通用户角色之间升降级
     * @param id,roles
     * @return
     */
    @RequestMapping("/super/changeRoles")
    public R changeRoles(@RequestBody UserEntity userEntity){
        return userService.changeRoles(userEntity);
    }

    /**
     * 超级管理员封锁管理员的账号
     * @param id
     * @return islock: 0/1
     */
    @RequestMapping("/super/switchIslockToAdmin")
    public R lockUser(@RequestBody UserEntity userEntity){
        userEntity = userService.getById(userEntity.getId());
        userEntity.setIslock(Math.abs(userEntity.getIslock() - 1));
        userService.updateById(userEntity);
        return R.ok().put("islock",userEntity.getIslock());
    }

    /**
     * 手动创建一个管理员
     * @param name（可null）,email,username,password,avatar（可null）,phone（可null）,sex（可null）,birthday（可null）,address（可null）,introduction（可null）,motto（可null）,style_id（可null）,
     *            islock请默认为0 roles默认为ROLE_ADMIN
     */
    @RequestMapping("/super/insertAdmin")
    public R insertAdmin(@RequestBody UserEntity userEntity){
        return userService.insertAdmin(userEntity);
    }


    /**
     * 后台管理系统将查询出来的用户列表分页  该功能有bug，勿用
     */
    @RequestMapping("/findUsersByPages")
    public R findUsersByPages(HttpServletRequest request,@RequestBody UserEntity userEntity){
        return userService.findUsersByPages(request,userEntity);
    }

    /**
     * 根据用户id查找出该id关注的user
     * @param id
     */
    @RequestMapping("/searchToUserByUserId")
    public R searchToUserByUserId(@RequestParam("id") int id){
        return userService.searchToUserByUserId(id);
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = userService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id){
		UserEntity user = userService.getById(id);

        return R.ok().put("user", user);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody UserEntity user){
		userService.save(user);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody UserEntity user){
		userService.updateById(user);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids){
		userService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
