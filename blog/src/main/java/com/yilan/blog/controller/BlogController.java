package com.yilan.blog.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yilan.blog.entity.BlogEntity;
import com.yilan.blog.entity.UserEntity;
import com.yilan.blog.service.BlogService;
import com.yilan.blog.service.UserService;
import com.yilan.blog.utils.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * 
 *
 * @author GroupOne
 * @email ${email}
 * @date 2020-09-11 11:08:28
 */
@RestController
@RequestMapping("blogsystem/blog")
public class BlogController {
    @Autowired
    private BlogService blogService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private HttpServletRequest request;
    
    /**
     * 在博文中上传图片，并返回一个图片http链接
     */
    @RequestMapping("/uploadBlogImg")
    public R uploadBlogImg(MultipartFile file){
        return blogService.uploadBlogImg(file);
    }

    

    /**
     * 主页列表
     */
    @RequestMapping("/homelist")
    public R homelist(){
        List<BlogEntity> entities = blogService.listToHomePage();

        return R.ok().put("entities", entities);
    }

//    /**
//     * 主页分页返回
//     */
//    @RequestMapping("/pagelist")
//    public PageInfo pagelist(Integer num,Integer size){
//
//        //1. 开启分页查询
//        PageHelper.startPage(num,size);
//        //2. 从数据库拉取信息
//        List<BlogEntity> blog =  blogService.getBaseMapper().selectList(new QueryWrapper<BlogEntity>()
//                .in("status",0,1,2).in("authority",0));
//        //3. 封装到 pageInfo 中
//        PageInfo pageInfo = new PageInfo(blog,5);
//        //4. 传给前端
//        return pageInfo;
//    }

    /**
     * 返回某用户已发布
     */
    @RequestMapping("/searchUserBlog")
    public R searchUserBlog(Integer userId){

        List<BlogEntity> entities = blogService.getBaseMapper().selectList(new QueryWrapper<BlogEntity>()
                .eq("user_id",userId).in("status",0,1,2).in("authority",0));

        return R.ok().put("entities", entities);
    }



    /**
     * 返回某分类下的文章
     */
    @RequestMapping("/cataloglist/{id}")
    public R cataloglist(@PathVariable("id") Integer id){
        List<BlogEntity> entities = blogService.listByCatalog(id);

        return R.ok().put("entities", entities);
    }

    /**
     * 按ID查
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id){
		BlogEntity blog = blogService.getById(id);
		Integer readSize = blog.getReadSize();
		blog.setReadSize(readSize+1);
        blogService.updateById(blog);
        UserEntity user = userService.getBaseMapper().selectById(blog.getUserId());//查找返回用户名
        return R.ok().put("blog", blog).put("userName",user.getUsername());
    }

    /**
     * 返回用户已发布
     */
    @RequestMapping("/userBlog")
    public R userBlogQuery(){
        String token = request.getHeader("Authorization");
        String username = jwtTokenUtil.getUsernameFromToken(token);
        UserEntity userEntity = userService.getOne(new QueryWrapper<UserEntity>().eq("username",username));

        List<BlogEntity> entities = blogService.listByUser(userEntity.getId());

        return R.ok().put("entities", entities);
    }

    /**
     * 返回用户草稿文章
     */
    @RequestMapping("/userDraftBlog")
    public R userDraftQuery(){
        String token = request.getHeader("Authorization");
        String username = jwtTokenUtil.getUsernameFromToken(token);
        UserEntity userEntity = userService.getOne(new QueryWrapper<UserEntity>().eq("username",username));

        List<BlogEntity> entities = blogService.listByDraft(userEntity.getId());

        return R.ok().put("entities", entities);
    }

    /**
     * 返回用户正在审核文章
     */
    @RequestMapping("/userCheckBlog")
    public R userCheckQuery(){
        String token = request.getHeader("Authorization");
        String username = jwtTokenUtil.getUsernameFromToken(token);
        UserEntity userEntity = userService.getOne(new QueryWrapper<UserEntity>().eq("username",username));

        List<BlogEntity> entities = blogService.listByCheck(userEntity.getId());

        return R.ok().put("entities", entities);
    }

    /**
     * 按阅读量返回10条文章
     */
    @RequestMapping("/readQuery")
    public R readQuery(){


        List<BlogEntity> entities = blogService.listByRead();

        return R.ok().put("entities", entities);
    }

    /**
     * 按点赞量返回10条文章
     */
    @RequestMapping("/voteQuery")
    public R voteQuery(){


        List<BlogEntity> entities = blogService.listByVote();

        return R.ok().put("entities", entities);
    }


    /**
     * 按评论量返回10条文章
     */
    @RequestMapping("/commentQuery")
    public R commentQuery(){


        List<BlogEntity> entities = blogService.listByComment();

        return R.ok().put("entities", entities);
    }


    /**
     * 模糊查询
     */
    @RequestMapping("/fuzzyQuery")
    public R fuzzyQuery(@RequestParam(value = "queryString", required = true) String queryString){
        List<BlogEntity> entities = blogService.fuzzyQuery(queryString);

        return R.ok().put("entities", entities);
    }

    /**
     * 新增文章
     */
    @RequestMapping("/save")
    public R save(@RequestBody BlogEntity blog){
        String token = request.getHeader("Authorization");
        String username = jwtTokenUtil.getUsernameFromToken(token);
        UserEntity userEntity = userService.getOne(new QueryWrapper<UserEntity>().eq("username",username));
        blog.setUserId(userEntity.getId());
        blog.setVoteSize(0);
        blog.setReadSize(0);
        blog.setCommentSize(0);
        blogService.save(blog);

        return R.ok();
    }


    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody BlogEntity blog){
        String token = request.getHeader("Authorization");
        String username = jwtTokenUtil.getUsernameFromToken(token);
        UserEntity userEntity = userService.getOne(new QueryWrapper<UserEntity>().eq("username",username));
        blog.setUserId(userEntity.getId());

		blogService.updateById(blog);
//		blogService.getBaseMapper().updateById(blog);
//      blogService.getBaseMapper().update(blog,new QueryWrapper<BlogEntity>().eq("id",blog.getId()));

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete/{id}")
    public R delete(@PathVariable("id") Integer id){
		blogService.removeById(id);

        return R.ok();
    }

    /**
     * 返回全部违规、被封文章
     */
    @RequestMapping("/illegalList")
    public R illegalList(){
        List<BlogEntity> entities = blogService.listIllgal();

        return R.ok().put("entities", entities);
    }

    /**
     * 违规文章的模糊查询
     */
    @RequestMapping("/illegalFuzzyQuery")
    public R illegalFuzzyQuery(Integer userId,String title,String status){
        List<BlogEntity> entities = blogService.illegalFuzzyQuery(userId,title,status);

        return R.ok().put("entities", entities);
    }

    /**
     * 将违规文章封禁，修改状态为3
     */
    @RequestMapping("/banBlog")
    public R ban(@RequestParam(value = "blogId", required = true) Integer blogId){
        BlogEntity blog = blogService.getById(blogId);
        blog.setStatus("3");
        blogService.updateById(blog);
        return R.ok();
    }

    /**
     * 将封禁文章解封，修改状态为2
     */
    @RequestMapping("/deblockBlog")
    public R deblock(@RequestParam(value = "blogId", required = true) Integer blogId){
        BlogEntity blog = blogService.getById(blogId);
        blog.setStatus("2");
        blogService.updateById(blog);
        return R.ok();
    }

    /**
     * 将被举报文章恢复正常，修改状态为0
     */
    @RequestMapping("/recoverBlog")
    public R recover(@RequestParam(value = "blogId", required = true) Integer blogId){
        BlogEntity blog = blogService.getById(blogId);
        blog.setStatus("0");
        blogService.updateById(blog);
        return R.ok();
    }

    /**
     * 用户举报文章，修改状态为2
     */
    @RequestMapping("/reportBlog")
    public R reportBlog(@RequestParam(value = "blogId", required = true) Integer blogId){
        BlogEntity blog = blogService.getById(blogId);
        blog.setStatus("2");
        blogService.updateById(blog);
        return R.ok();
    }
    
    

//    /**
//     * 列表
//     */
//    @RequestMapping("/list")
//    public R list(@RequestParam Map<String, Object> params){
//        PageUtils page = blogService.queryPage(params);
//
//        return R.ok().put("page", page);
//    }
//
//
//    /**
//     * 信息
//     */
//    @RequestMapping("/info/{id}")
//    public R info(@PathVariable("id") Integer id){
//		BlogEntity blog = blogService.getById(id);
//
//        return R.ok().put("blog", blog);
//    }
//
//    /**
//     * 保存
//     */
//    @RequestMapping("/save")
//    public R save(@RequestBody BlogEntity blog){
//		blogService.save(blog);
//
//        return R.ok();
//    }
//
//    /**
//     * 修改
//     */
//    @RequestMapping("/update")
//    public R update(@RequestBody BlogEntity blog){
//		blogService.updateById(blog);
//
//        return R.ok();
//    }
//
//    /**
//     * 删除
//     */
//    @RequestMapping("/delete")
//    public R delete(@RequestBody Integer[] ids){
//		blogService.removeByIds(Arrays.asList(ids));
//
//        return R.ok();
//    }

}
