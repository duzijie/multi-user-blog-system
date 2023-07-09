package com.yilan.blog;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yilan.blog.dao.UserDao;
import com.yilan.blog.entity.*;
import com.yilan.blog.service.BlogService;
import com.yilan.blog.service.CommentService;
import com.yilan.blog.service.UserService;
import com.yilan.blog.service.UserToUserService;
import com.yilan.blog.utils.JwtTokenUtil;
import com.yilan.blog.utils.R;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
class BlogApplicationTests {

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private JavaMailSender mailSender;

    @Test
    void contextLoads() {
        UserEntity userEntity = userService.getOne(new QueryWrapper<UserEntity>().eq("username","duzijie"));
        userEntity.setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList(userEntity.getRoles()));
        System.out.println(userEntity);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodePwd = bCryptPasswordEncoder.encode("123456");
        System.out.println("encodePassword:" + encodePwd);
        boolean flag = bCryptPasswordEncoder.matches("123456",userEntity.getPassword());
        System.out.println(flag);
    }

    @Test
    void sendEmail(){
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        mailMessage.setFrom("source@qq.com");
//        mailMessage.setTo("target@qq.com");
//        mailMessage.setSubject("test email from code");
//        mailMessage.setText("Hello springboot");
//
//        mailSender.send(mailMessage);
    }

    @Test
    void saveAUser(){
//        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//        UserEntity userEntity = new UserEntity();
//        userEntity.setEmail("******@qq.com");
//        userEntity.setUsername("testUserName");
//        userEntity.setPassword(bCryptPasswordEncoder.encode("123456"));
//        userEntity.setIslock(0);
//        userEntity.setRoles("ROLE_USER");
//
//        userService.getBaseMapper().insert(userEntity);
//        userService.save(userEntity);

    }

    @Test
    void findIfUniqueUserByUsername(){
//        UserEntity user = userService.getById(id);
        String username = "testUserName";
        List<UserEntity> userEntityList = userService.list(new QueryWrapper<UserEntity>().eq("username",username));
        System.out.println(userEntityList);
        System.out.println(userEntityList.isEmpty());
    }

    @Test
    void findIfExistsUserByEmail(){
        String email = "****@qq.com";
        System.out.println(userService.getBaseMapper().selectOne(new QueryWrapper<UserEntity>().eq("email",email)));
//        baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("email",email));
    }


    /**
     * 管理员：查询用户
     */

//    @Autowired
//    private UserDao userDao;
    @Test
    void searchUsers() throws IOException {
        Date date = new Date();
        System.out.println(date);
    }


    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Test
    void testJWT(){
        System.out.println(jwtTokenUtil.getHeader());
        System.out.println(jwtTokenUtil.getExpiration());
        System.out.println(jwtTokenUtil.getSecret());
    }

    @Test
    void uploadAvatar(){
        //targetPath为头像放置的目标文件夹
        File targetPath = new File("src" + File.separator + "main" + File.separator + "resources" + File.separator + "static" + File.separator + "img" + File.separator + "avatar");
        if(!targetPath.exists()){
            targetPath.mkdirs();
        }
        System.out.println(targetPath.getAbsolutePath());
        System.out.println(targetPath.getPath());
    }

    @Test
    /**
     * 如：把
     * D:/img/avatar/A8EB4E79D5904946968BEAADE9C2D25D.png
     * 转换成
     * http://localhost:8083/img/avatar/A8EB4E79D5904946968BEAADE9C2D25D.png
     */
    void localAbsoluteTransferToHttpUrl(){
        String absolutePath = "D:/img/avatar/A8EB4E79D5904946968BEAADE9C2D25D.png";
        String httpUrl = "http://localhost:8083/" + (absolutePath.substring(absolutePath.indexOf("img")).replace("\\","/"));
        System.out.println(httpUrl);
    }

    @Test
    /**
     * 把
     * http://localhost:8083/img/avatar/A8EB4E79D5904946968BEAADE9C2D25D.png
     * 转换成
     * D:\img\avatar\A8EB4E79D5904946968BEAADE9C2D25D.png
     */
    void httpUrlTransferToLocalAbsolute(){
        String httpUrl = "http://localhost:8083/img/avatar/A8EB4E79D5904946968BEAADE9C2D25D.png";
        File file = new File("D:/");
        String localPath = (file.getAbsolutePath()+httpUrl.substring(httpUrl.indexOf("img"))).replace("\\","/");
        System.out.println(localPath);
    }



    @Test
    void testWhyNull(){
        String username = "testUserName";
        UserEntity userEntity = userService.getBaseMapper().selectOne(new QueryWrapper<UserEntity>().eq("username", username));
        System.out.println(userEntity);
        System.out.println("==================");
        System.out.println(userEntity.getAvatar());
//        System.out.println(userEntity!=null && ( ( !userEntity.getAvatar().isEmpty() ) || ( userEntity.getAvatar() != null ) ) );
        System.out.println(userEntity.getAvatar() != null);
    }

//    public List<CatalogEntity> listWithTree() {
//        //1、查出所有分类  因为这个类extends了ServiceImpl，所以baseMapper相当于CategoryDao 省去了以往需要注入才能使用的动作
//        List<CatalogEntity> entities = baseMapper.selectList(null);
//        //2、组装成父子的树形结构
//
//        //2.1)、找到所有的一级分类
//        List<CatalogEntity> level1Menus = entities.stream().filter(catalogEntity ->
//                catalogEntity.getParentId() == 0
//        ).map((menu) -> {
//            menu.setChildren(getChildrens(menu,entities));
//            return menu;
//        }).collect(Collectors.toList());
//        return level1Menus;
//    }
//    //递归查找所有菜单的子菜单
//    private List<CatalogEntity> getChildrens(CatalogEntity root, List<CatalogEntity> all){
//        List<CatalogEntity> children = all.stream().filter(catalogEntity -> {
//            return catalogEntity.getParentId() == root.getId();
//        }).map(catalogEntity -> {
//            //1、找到子菜单
//            catalogEntity.setChildren(getChildrens(catalogEntity,all));
//            return catalogEntity;
//        }).collect(Collectors.toList());
//
//        return children;
//    }

    @Test
    void testListCommentWithTree(){
        System.out.println(listCommentWithTree());
    }
    /**
     * 把某篇文章的评论转换成树形数据
     */
    private List<CommentEntity> listCommentWithTree(){

        int blog_id = 1;
        // 1.根据blog_id查找出所有comment记录，得到该博文下的所有评论，返回List<CommentEntity>
        List<CommentEntity> entities = commentService.getBaseMapper().selectList(new QueryWrapper<CommentEntity>().eq("blog_id",blog_id));
        // 2.找到所有一级评论
        List<CommentEntity> level1Menus = entities.stream().filter(commentEntity ->
                commentEntity.getParentId() == 0
        ).map((menu) -> {
            menu.setChildren(getChildrens(menu,entities));
            return menu;
        }).collect(Collectors.toList());
        return level1Menus;
    }

    private List<CommentEntity> getChildrens(CommentEntity root, List<CommentEntity> all) {
        List<CommentEntity> children = all.stream().filter(commentEntity -> {
            return commentEntity.getParentId() == root.getId();
        }).map(commentEntity -> {
            //1、找到子菜单
            commentEntity.setChildren(getChildrens(commentEntity,all));
            return commentEntity;
        }).collect(Collectors.toList());

        return children;
    }

    @Test
    void formPassword(){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encryptPassword = bCryptPasswordEncoder.encode("123456");
        System.out.println(encryptPassword);
    }

    @Autowired
    private UserToUserService userToUserService;
    @Test
    void profilePage(){
        int id = 3;
        UserEntity userEntity = userService.getBaseMapper().selectById(id);
        //查找粉丝数
        int fansNum = userToUserService.list(new QueryWrapper<UserToUserEntity>().eq("to_user_id", id)).size();
        System.out.println("fansNum===" + fansNum);

        int follow = userToUserService.list(new QueryWrapper<UserToUserEntity>().eq("from_user_id", id)).size();
        System.out.println(follow);
    }
    @Autowired
    private BlogService blogService;

    @Test
    void testCommentNum(){
        CommentEntity commentEntity = commentService.getBaseMapper().selectOne(new QueryWrapper<CommentEntity>().eq("id",24));
        //相应修改文章的评论量
        int blogCommentNum = commentService.getBaseMapper().selectCount(new QueryWrapper<CommentEntity>().eq("blog_id",commentEntity.getBlogId()));
        System.out.println("This is blogCommentNum=====");
        System.out.println(blogCommentNum);
        BlogEntity blogEntity = blogService.getById(commentEntity.getBlogId());
        blogEntity.setCommentSize(blogCommentNum);

    }

}
