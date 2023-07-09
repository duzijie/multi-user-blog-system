package com.yilan.blog.controller;

import java.util.Arrays;
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
import com.yilan.blog.entity.VoteEntity;
import com.yilan.blog.service.BlogService;
import com.yilan.blog.service.UserService;
import com.yilan.blog.service.VoteService;
import com.yilan.blog.utils.JwtTokenUtil;
import com.yilan.blog.utils.PageUtils;
import com.yilan.blog.utils.R;



/**
 * 
 *
 * @author GroupOne
 * @email ${email}
 * @date 2020-09-11 11:08:28
 */
@RestController
@RequestMapping("blogsystem/vote")
public class VoteController {
    @Autowired
    private VoteService voteService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private BlogService blogService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = voteService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id){
		VoteEntity vote = voteService.getById(id);

        return R.ok().put("vote", vote);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestParam(value = "blogId", required = true) Integer blogId){
        VoteEntity vote = new VoteEntity();
        String token = request.getHeader("Authorization");
        String username = jwtTokenUtil.getUsernameFromToken(token);
        UserEntity userEntity = userService.getOne(new QueryWrapper<UserEntity>().eq("username",username));
        //设置点赞记录的点赞用户和点赞文章
        vote.setUserId(userEntity.getId());
        vote.setBlogId(blogId);
        //查询点赞记录是否存在
        VoteEntity vote0 = voteService.getBaseMapper().selectOne(new QueryWrapper<VoteEntity>()
                .eq("user_id",vote.getUserId()).and(i -> i.eq("blog_id", vote.getBlogId())));
        //执行不同分支
        if(vote0 == null){
            voteService.save(vote);
            BlogEntity blog = blogService.getBaseMapper().selectById(blogId);
            blog.setVoteSize(blog.getVoteSize()+1);
            blogService.getBaseMapper().updateById(blog);
            return R.ok();
        }else{
            return R.error(-1,"该用户已点过赞！");
        }
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody VoteEntity vote){
		voteService.updateById(vote);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Integer[] ids){
		voteService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }
//    /**
//     * 列表
//     */
//    @RequestMapping("/list")
//    public R list(@RequestParam Map<String, Object> params){
//        PageUtils page = voteService.queryPage(params);
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
//		VoteEntity vote = voteService.getById(id);
//
//        return R.ok().put("vote", vote);
//    }
//
//    /**
//     * 保存
//     */
//    @RequestMapping("/save")
//    public R save(@RequestBody VoteEntity vote){
//		voteService.save(vote);
//
//        return R.ok();
//    }
//
//    /**
//     * 修改
//     */
//    @RequestMapping("/update")
//    public R update(@RequestBody VoteEntity vote){
//		voteService.updateById(vote);
//
//        return R.ok();
//    }
//
//    /**
//     * 删除
//     */
//    @RequestMapping("/delete")
//    public R delete(@RequestBody Integer[] ids){
//		voteService.removeByIds(Arrays.asList(ids));
//
//        return R.ok();
//    }

}
