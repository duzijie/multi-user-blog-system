package com.yilan.blog.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yilan.blog.entity.UserToUserEntity;
import com.yilan.blog.service.UserToUserService;
import com.yilan.blog.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.JdbcUtils;
import com.yilan.blog.dao.UserDao;
import com.yilan.blog.entity.UserEntity;
import com.yilan.blog.service.UserService;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Properties;

@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserEntity> page = this.page(
                new Query<UserEntity>().getPage(params),
                new QueryWrapper<UserEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public R findIfUniqueUserByUsername(String username) {
        List<UserEntity> userEntityList = baseMapper.selectList(new QueryWrapper<UserEntity>().eq("username", username));
        if (userEntityList.isEmpty()) {
            //如果数据库中没有该用户名，返回true
            return R.ok().put("code", true);
        } else {
            return R.ok().put("code", false);
        }
    }

    @Override
    public R findIfUniqueUserByEmail(String email) {
        List<UserEntity> userEntityList = baseMapper.selectList(new QueryWrapper<UserEntity>().eq("email", email));
        if (userEntityList.isEmpty()) {
            //如果用户名唯一，返回true
            return R.ok().put("code", true);
        } else {
            return R.ok().put("code", false);
        }
    }

    @Override
    public R registerProcessing(UserEntity userEntity) {
    	Properties props = new Properties();
    	try {
			props.load(JdbcUtils.class.getClassLoader().getResourceAsStream("application.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    	String SOURCE_EMAIL = props.getProperty("spring.mail.username");
    	
        //对email加密处理 作为邮箱发送的验证码
        String verification = DigestUtils.md5DigestAsHex(userEntity.getEmail().getBytes());
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(SOURCE_EMAIL);
        mailMessage.setTo(userEntity.getEmail());
        mailMessage.setSubject("易览博客系统验证码，请勿分享");
        mailMessage.setText("请保证该邮件的发送是本人亲自操作，若不知情请勿理会。若是本人操作，请复制下方验证码：" + verification);
        mailSender.send(mailMessage);
        return R.ok().put("code", true);
    }

    @Override
    public R verifying(String email, String verification) {
        //验证是否正确
        if (verification.equals(DigestUtils.md5DigestAsHex(email.getBytes()))) {
            //正确，返回code: true给前端，让前端接下来把注册用户的所有表单信息传给后台
            return R.ok().put("code", true);
        } else {
            return R.ok().put("code", false);
        }
    }

    @Override
    public R submitInfo(UserEntity userEntity) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        userEntity.setPassword(bCryptPasswordEncoder.encode(userEntity.getPassword()));
        userEntity.setIslock(0);
        userEntity.setRoles("ROLE_USER");
        baseMapper.insert(userEntity);
        return R.ok().put("code", true);
    }

    /**
     * 找回密码前，先检测邮箱存不存在
     *
     * @param userEntity email
     * @return
     */
    public R findIfExistsUserByEmail(UserEntity userEntity) {
        UserEntity user = baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("email", userEntity.getEmail()));
        if (user == null) {
            //如果邮箱不存在，返回false
            return R.ok().put("code", false);
        } else {
            return R.ok().put("code", true);
        }
    }

    @Override
    public R resetPwdVerification(UserEntity userEntity) {
    	Properties props = new Properties();
    	try {
			props.load(JdbcUtils.class.getClassLoader().getResourceAsStream("application.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    	String SOURCE_EMAIL = props.getProperty("spring.mail.username");
    	
        //对email和系统当前时间做加密处理 作为邮箱发送的验证码
        String rowVerification = userEntity.getEmail() + System.currentTimeMillis();
        String verification = DigestUtils.md5DigestAsHex(rowVerification.getBytes());
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(SOURCE_EMAIL);
        mailMessage.setTo(userEntity.getEmail());
        mailMessage.setSubject("易览博客系统验证码，请勿分享");
        mailMessage.setText("请保证该邮件的发送是本人亲自操作，若不知情请勿理会。若是本人操作，请复制下方验证码：" + verification);
        mailSender.send(mailMessage);
        //把相应的验证码数据存入数据库，方便以后验证
        userEntity.setVerification(verification);
        baseMapper.update(userEntity, new QueryWrapper<UserEntity>().eq("email", userEntity.getEmail()));
        return R.ok().put("code", true);
    }

    @Override
    public R resetPwdVerifying(UserEntity userEntity) {
        UserEntity databaseUserEntity = baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("email", userEntity.getEmail()));
        if (userEntity.getVerification().equals(databaseUserEntity.getVerification())) {
            //验证码输入正确
            return R.ok().put("code", true);
        } else {
            return R.ok().put("code", false);
        }
    }

    @Override
    public R resetPassword(UserEntity userEntity) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        UserEntity databaseUserEntity = baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("email", userEntity.getEmail()));
        if (userEntity.getVerification().equals(databaseUserEntity.getVerification())) {
            //二次验证验证码正确后，执行修改密码程序
            String encodePwd = bCryptPasswordEncoder.encode(userEntity.getPassword());
            userEntity.setPassword(encodePwd);
            userEntity.setVerification("");
            baseMapper.update(userEntity, new QueryWrapper<UserEntity>().eq("email", userEntity.getEmail()));
            return R.ok().put("code", true);
        } else {
            return R.ok().put("code", false);
        }
    }

    @Override
    public R canUsernameChange(UserEntity userEntity) {
        UserEntity dbUserEntity = baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("email", userEntity.getEmail()));
        if (dbUserEntity == null) {
            //忘记密码页面甚至都没有该邮箱
            return R.ok().put("code", false);
        } else if (dbUserEntity.getUsername().equals(userEntity.getUsername())) {
            //如果用户名还是以前的用户名  返回true 意味着可以更新到数据库
            return R.ok().put("code", true);
        } else {
            List<UserEntity> userEntityList = baseMapper.selectList(new QueryWrapper<UserEntity>().eq("username", userEntity.getUsername()));
            if (userEntityList.isEmpty()) {
                //如果数据库中没有该用户名，返回true
                return R.ok().put("code", true);
            } else {
                return R.ok().put("code", false);
            }
        }
    }

    @Override
    public R searchUsersByParams(UserEntity userEntity) {
        List<UserEntity> userEntityList = baseMapper.searchUsersByParams(userEntity);
        return R.ok().put("userEntityList", userEntityList);
    }

    @Override
    public R searchAdminsByParams(UserEntity userEntity) {
        List<UserEntity> userEntityList = baseMapper.searchAdminsByParams(userEntity);
        return R.ok().put("userEntityList", userEntityList);
    }

    @Override
    public R searchAllUsers() {
        List<UserEntity> userEntityList = baseMapper.selectList(new QueryWrapper<UserEntity>().like("roles", "ROLE_USER"));
        return R.ok().put("userEntityList", userEntityList);
    }

    @Override
    public R searchInfo(HttpServletRequest request) {
        String jwtToken = request.getHeader(jwtTokenUtil.getHeader());
        System.out.println("UserService jwtToken=====" + jwtToken);
        if (!StringUtils.isEmpty(jwtToken)) {
            String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            System.out.println("UserService username=====" + username);

            UserEntity userEntity = baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("username", username));
            return R.ok().put("userEntity", userEntity);
        }
        return R.ok().put("userEntity", null);
    }

    @Override
    public R updateInfo(UserEntity userEntity, HttpServletRequest request) {
        String jwtToken = request.getHeader(jwtTokenUtil.getHeader());
        if (!StringUtils.isEmpty(jwtToken)) {
            String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            baseMapper.update(userEntity, new QueryWrapper<UserEntity>().eq("username", username));
            return R.ok().put("code", true);
        }
        return R.ok().put("code", false);
    }

    @Override
    public R uploadAvatar(MultipartFile file, HttpServletRequest request) {

        //获取文件的内容
        try {
            //获取原始文件名
            String originalFilename = file.getOriginalFilename();                   //photo.png
            System.out.println("originalFilename:======" + originalFilename);
            //生成一个uuid名称出来
            String uuidFilename = UploadUtils.getUUIDName(originalFilename);        //A8EB4E79D5904946968BEAADE9C2D25D.png
            System.out.println("uuidFilename:" + uuidFilename);

//            File targetPath = new File("D:/img/avatar/");      //Windows test
            File targetPath = new File("/opt/img/avatar/");      //Linux run
            if (!targetPath.exists()) {
                targetPath.mkdirs();
            }

            //先把用户上传的图像先存储到服务器
            File localUpdatedAvatar = new File(targetPath.getAbsolutePath(), uuidFilename);     //D:\img\avatar\A8EB4E79D5904946968BEAADE9C2D25D.png
            System.out.println("localUpdatedAvatar======" + localUpdatedAvatar);
            file.transferTo(localUpdatedAvatar);

            //服务器存储好新图片后，先删除服务器磁盘原有图像  再把新图像转换成url地址  然后存入数据库
            UrlTransferUtils urlTransferUtils = new UrlTransferUtils();
            String jwtToken = request.getHeader(jwtTokenUtil.getHeader());
            if (!StringUtils.isEmpty(jwtToken)) {
                String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
                UserEntity userEntity = baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("username", username));
                //开始删除服务器磁盘原有图像  如果有原有图像则删除  没有则跳过
                if (userEntity.getAvatar() != null) {
                    String httpUrl = userEntity.getAvatar();
                    System.out.println("httpUrl/userEntity.getAvatar():" + userEntity.getAvatar());
                    String localAbsolute = urlTransferUtils.httpUrlTransferToLocalAbsolute(httpUrl);
                    System.out.println("localAbsolute:" + localAbsolute);
                    try{
                        File deleteFile = new File(localAbsolute);
                        deleteFile.delete();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
                //把新图像转换成url地址 并存入数据库
                String httpUrlToDB = urlTransferUtils.localAbsoluteTransferToHttpUrl(localUpdatedAvatar.getAbsolutePath());
                userEntity.setAvatar(httpUrlToDB);
                baseMapper.updateById(userEntity);
                return R.ok().put("code",true).put("avatar",httpUrlToDB);
            }
            return R.ok().put("code",false).put("avatar",null);

        } catch (IOException e) {
            e.printStackTrace();
            return R.ok().put("code",false).put("avatar",null);
        }
    }

    @Autowired
    private UserToUserService userToUserService;
    @Override
    public R profilePage(int id) {
//        UserEntity userEntity = baseMapper.selectById(id);
        //查找粉丝数
        int fansNum = userToUserService.list(new QueryWrapper<UserToUserEntity>().eq("to_user_id", id)).size();

        //查找关注数
        int follow = userToUserService.list(new QueryWrapper<UserToUserEntity>().eq("from_user_id", id)).size();

        //查找个性签名
        UserEntity userEntity = baseMapper.selectById(id);
        String motto = userEntity.getMotto();

        //查找个人简介
        String introduction = userEntity.getIntroduction();

        //查找用户名
        String username = userEntity.getUsername();

        //查找用户头像
        String avatar = userEntity.getAvatar();

        return R.ok().put("fansNum",fansNum).put("follow",follow).put("motto",motto).put("introduction",introduction).put("username",username).put("avatar",avatar);
    }

    @Override
    public R followOne(HttpServletRequest request, int to_user_id) {
        String jwtToken = request.getHeader(jwtTokenUtil.getHeader());
        if (!StringUtils.isEmpty(jwtToken)) {
            String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            UserEntity fromUserEntity = baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("username",username));
            //要先查询是否已经存在关注状态
            if(userToUserService.getOne(new QueryWrapper<UserToUserEntity>().eq("from_user_id",fromUserEntity.getId()).eq("to_user_id",to_user_id)) != null){
                //如果查询到已经存在关注的记录
                return R.ok().put("res","早已关注（包括被屏蔽状态）");
            }
            int from_user_id = fromUserEntity.getId();
            UserToUserEntity userToUserEntity = new UserToUserEntity();
            userToUserEntity.setFromUserId(from_user_id);
            userToUserEntity.setToUserId(to_user_id);
            userToUserEntity.setStatus(0);
            userToUserService.save(userToUserEntity);
            return R.ok().put("res","关注成功");
        }
        return R.ok().put("res","请先登录");
    }

    @Override
    public R updateUserStatus(int id, int islock) {
        UserEntity dbUserEntity = baseMapper.selectById(id);
        dbUserEntity.setIslock(islock);
        baseMapper.updateById(dbUserEntity);
        return R.ok().put("code",true);
    }

    /**
     * 管理员和普通用户角色之间升降级
     * @param id,roles
     * @return
     */
    @Override
    public R changeRoles(UserEntity userEntity) {
        String roles = userEntity.getRoles();
        userEntity = baseMapper.selectById(userEntity.getId());
        userEntity.setRoles(roles);
        baseMapper.updateById(userEntity);
        return R.ok().put("code",true);
    }

    /**
     * 手动创建一个管理员
     * @param name（可null）,email,username,password,avatar（可null）,phone（可null）,sex（可null）,birthday（可null）,address（可null）,introduction（可null）,motto（可null）,style_id（可null）,
     *            islock请默认为0 roles默认为ROLE_ADMIN
     */
    @Override
    public R insertAdmin(UserEntity userEntity) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodePassword = bCryptPasswordEncoder.encode(userEntity.getPassword());
        userEntity.setPassword(encodePassword);
        userEntity.setIslock(0);
        userEntity.setRoles("ROLE_ADMIN");
        baseMapper.insert(userEntity);
        return R.ok().put("code",true);
    }

    @Override
    public R searchRolesByToken(HttpServletRequest request) {
        String jwtToken = request.getHeader(jwtTokenUtil.getHeader());
        if (!StringUtils.isEmpty(jwtToken)) {
            String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            UserEntity userEntity = baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("username", username));
            return R.ok().put("roles",userEntity.getRoles());
        }
        return R.ok().put("roles", "");
    }

    @Override
    public R findUsersByPages(HttpServletRequest request, UserEntity userEntity) {
        int pageCount = Integer.valueOf(request.getParameter("pageCount"));
        int pageSize = Integer.valueOf(request.getParameter("pageSize"));
        PageHelper.startPage(pageCount,pageSize);
//        List<UserEntity> userEntityList = baseMapper.findUsersByPages(userEntity,pageCount,pageSize);
//        List<UserEntity> userEntityList = baseMapper.searchUsersByParams(userEntity);
        List<UserEntity> userEntityList = baseMapper.searchUsersByParams(null);
        PageInfo<UserEntity> pageInfo = new PageInfo<UserEntity>(userEntityList);
        return R.ok().put("pageInfo", pageInfo);
    }

    @Autowired
    private UserService userService;
    @Override
    public R searchToUserByUserId(int id) {
        List<UserToUserEntity> userToUserEntityList = userToUserService.list(new QueryWrapper<UserToUserEntity>().eq("from_user_id", id).select("to_user_id"));
        List<Integer> ids = new ArrayList<>();

        for (UserToUserEntity userToUserEntity: userToUserEntityList) {
            ids.add(userToUserEntity.getToUserId());
        }

        List<UserEntity> userEntityList = userService.listByIds(ids);
        return R.ok().put("userEntityList",userEntityList);
    }

}