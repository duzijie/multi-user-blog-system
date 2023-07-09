package com.yilan.blog.config.security;

import com.yilan.blog.config.security.handler.MyAuthenticationFailureHandler;
import com.yilan.blog.config.security.handler.MyAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Qualifier("userDetailServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtAuthTokenFilter jwtAuthTokenFilter;

    @Resource
    MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Resource
    MyAuthenticationFailureHandler myAuthenticationFailureHandler;

//    private String LOGINPAGE = "/login.html";                   //这是自定义的登录页面
//    private String LOGINPROCESSINGURL = "/login";               //这是security自带的登录处理请求，只需要在自定义的登录页面的form action指定路径即可
//    private String DEFAULTSUCCESSURL = "/index";
//    private String REGISTERPAGE = "/register.html";
//    private String LOGOUTURL = "/logout";

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
        //认证失败处理类
        //.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
        //基于token，所以不需要session
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            //配置权限
            .authorizeRequests()
        //对于登录login 验证码captchaImage 允许匿名访问
            .antMatchers(
                    "/login",
                    "/adminLogin"
            ).anonymous()
            .antMatchers(
                    HttpMethod.GET,
                    "/*.html",
                    "/**/*.html",
                    "/**/*.css",
                    "/**/*.js",
                    "/img/avatar/**",
                    "/img/blog/**",
                    "/refreshToken"
            ).permitAll()
            .antMatchers("/blogsystem/catalog/list/tree")
            .permitAll()        //三级分类数据 允许所有人访问
            .antMatchers(
                    "/blogsystem/user/register/info/username",
                    "/blogsystem/user/register/info/email",
                    "/blogsystem/user/register/verification",
                    "/blogsystem/user/register/verifying/**/**",
                    "/blogsystem/user/register/submitInfo",
                    
                    "/blogsystem/blog/homelist",  //博客主页允许所有人访问
                    "/blogsystem/blog/cataloglist/{id}",
                    "/blogsystem/blog/fuzzyQuery",
                    "/blogsystem/blog/info/{id}",

                    "/blogsystem/notice/noticeList" //前端不登录也可获取公告
            ).permitAll()           //用户注册相关路径允许所有人访问
            .antMatchers(
                    "/blogsystem/user/findIfExistsUserByEmail",
                    "/blogsystem/user/resetPwd/verification",
                    "/blogsystem/user/resetPwd/verifying",
                    "/blogsystem/user/resetPassword",
                    "/blogsystem/user/resetPwd/canUsernameChange"
                    )
            .permitAll()        // 用户找回密码相关路径允许所有人访问
            .antMatchers(
                    "/blogsystem/user/searchInfo",          //用户在个人页面显示个人资料允许所有角色访问
                    "/blogsystem/user/updateInfo",                       //允许用户在个人页面修改自己的资料
                    "/blogsystem/user/uploadAvatar"                      //允许用户在个人页面上传头像
            )
            .permitAll()
            .antMatchers(
                    "/blogsystem/user/profilePage",          //允许所有人查看其它博主的个人页面
                    "/blogsystem/user/searchToUserByUserId"                 //根据用户id查找出该id关注的user
            )
            .permitAll()
            .antMatchers(
                    "/blogsystem/user/followOne"            //允许已登录用户点击其它博主个人页面的关注按钮
            )
            .authenticated()
            .antMatchers(
                    "/blogsystem/user/admin/searchUsersByParams",   //根据条件动态查找用户
                    "/blogsystem/user/admin/searchAllUsers",                    //查找所有普通用户
                    "/blogsystem/user/admin/updateUserStatus"                   //冻结和恢复账户
            )
            .hasAnyRole("ADMIN","SUPER")    //管理员或超级管理员权限才能访问
             .antMatchers(
                     "/blogsystem/user/super/searchAdminsByParams",      //根据条件动态查找管理员
                     "/blogsystem/user/super/changeRoles",                               //管理员和普通用户之间的升降级
                     "/blogsystem/user/super/switchIslockToAdmin",                       //超级管理员封锁管理员的账号
                     "/blogsystem/user/super/insertAdmin"                               //手动增加管理员账号
             )
             .hasAnyRole("SUPER")
//            .antMatchers("/blogsystem/user/admin/searchUsers")
//            .permitAll()        //这里为了测试方便，暂时把接口允许所有人访问，后续要修改
             .antMatchers(
                     "/blogsystem/comment/listWithTree",          //评论模块：文章下面的评论允许所有人访问
                     "/blogsystem/comment/searchUserById",                       //评论模块：根据评论id查找出对应用户的id和role
                     "/blogsystem/comment/searchUsernameAndAvatarByUserId"       //评论模块：根据评论id找出用户username和password
             )
             .permitAll()
             .antMatchers(
                     "/blogsystem/comment/saveCommentByBlogId",      //评论模块：文章底部父级留言允许用户登录访问
                     "/blogsystem/comment/deleteComment"                          //评论模块：删除评论 用户本人、管理员身份都可以删除
             )
             .authenticated()
             .antMatchers(
                     "/blogsystem/usertouser/isFollow",              //关注模块：判断是否已构成关注关系
                     "/blogsystem/usertouser/deleteFollow"                        //关注模块：删除关注
             )
             .authenticated()
             .antMatchers(
                     "/blogsystem/blog/uploadBlogImg"                //文章模块：博文中上传图片
             )
             .authenticated()
             .antMatchers(
                     "/blogsystem/letter/searchUsersByToken",        //留言模块：展示首页需要的信息
                     "/blogsystem/letter/showLettersByUserId",                     //留言模块：展示留言内容
                     "/blogsystem/letter/addLetterToUserId"                        //留言模块：添加留言
             )
             .authenticated()
             .antMatchers(
                     "/blogsystem/user/searchRolesByToken"          //根据token查找用户角色
             )
             .authenticated()


            .antMatchers("/order")   //需要对外暴露的资源路径
            .hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")     //user角色和admin角色都可以访问
            .antMatchers(
                    "/blogsystem/blog/illegalFuzzyQuery"

            )
            .hasAnyAuthority( "ROLE_ADMIN","ROLE_SUPER")        //admin,super角色可以访问
            .antMatchers()
            .hasAnyRole("ADMIN")        //admin角色可以访问
            //除上面外的所有请求全部需要鉴权验证
            .anyRequest().authenticated().and()     //authenticated()要求在执行该请求时，必须已经登录了应用
            //CRSF禁用，因为不使用session
            .csrf().disable();      //禁用跨站csrf攻击防御，否则无法登录成功
        //登出功能
        httpSecurity.logout().logoutUrl("/logout");
        //添加JWT filter
        httpSecurity.addFilterBefore(jwtAuthTokenFilter, UsernamePasswordAuthenticationFilter.class);

//        httpSecurity
//                .formLogin()    //开启formLogin模式
////                    .loginPage("/login.html")        //用户未登录时，访问任何资源都跳转到该路径，即登录页面
//                    .loginProcessingUrl("/login")     //登录表单form中action的地址，也就是处理认证请求的路径
//                    .usernameParameter("username")              //默认是username
//                    .passwordParameter("password")              //默认是password
//                    .successHandler(myAuthenticationSuccessHandler)
//                    .failureHandler(myAuthenticationFailureHandler)
//                    .permitAll()
//                .and()
//                .authorizeRequests()                            //配置权限
//                    .antMatchers("/login")
//                    .permitAll()                                 //所有用户都可以访问登录处理请求
//                    .antMatchers("/blogsystem/user/register/info/username","/blogsystem/user/register/info/email",
//                            "/blogsystem/user/register/verification","/blogsystem/user/register/verifying",
//                            "/blogsystem/user/register/submitInfo")             //用户注册页面对所有人开放
//                    .permitAll()
//                    //除上面外的所有请求全部需要鉴权认证
////                    .anyRequest().authenticated()               //authenticated()要求在执行该请求时，必须已经登录了应用
//                .and()
//                .logout().logoutUrl("/logout")
//                .and()
//                .csrf().disable()
        ;


    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Bean
   public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
   }

   @Bean
   public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
   }
}
