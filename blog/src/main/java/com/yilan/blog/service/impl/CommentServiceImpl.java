package com.yilan.blog.service.impl;

import com.yilan.blog.entity.BlogEntity;
import com.yilan.blog.entity.UserEntity;
import com.yilan.blog.service.BlogService;
import com.yilan.blog.service.UserService;
import com.yilan.blog.utils.JwtTokenUtil;
import com.yilan.blog.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yilan.blog.utils.PageUtils;
import com.yilan.blog.utils.Query;

import com.yilan.blog.dao.CommentDao;
import com.yilan.blog.entity.CommentEntity;
import com.yilan.blog.service.CommentService;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;


@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentDao, CommentEntity> implements CommentService {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CommentEntity> page = this.page(
                new Query<CommentEntity>().getPage(params),
                new QueryWrapper<CommentEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public R listWithTree(int blogId) {
        List<CommentEntity> commentEntityList = listCommentWithTree(blogId);
        return R.ok().put("commentEntityList",commentEntityList);
    }

    @Override
    /**
     * 数据库comment表中有 id content user_id create_time parent_id blog_id
     * 1.前端应该给后端传回 content token  parent_id blog_id
     * 其中缺少id user_id create_time。其中id数据库自动生成，create_time自动生成。
     * 只需要根据token解析出user_id即可
     */
    public R saveCommentByBlogId(CommentEntity commentEntity, HttpServletRequest request) {

        String jwtToken = request.getHeader(jwtTokenUtil.getHeader());
        if (!StringUtils.isEmpty(jwtToken)) {
            String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            //根据Token解析出UserEntity 从而获取user_id
            UserEntity userEntity = userService.getOne(new QueryWrapper<UserEntity>().eq("username", username));
            commentEntity.setUserId(userEntity.getId());
            baseMapper.insert(commentEntity);

            //相应修改文章的评论量
            int blogCommentNum = baseMapper.selectCount(new QueryWrapper<CommentEntity>().eq("blog_id",commentEntity.getBlogId()));
            BlogEntity blogEntity = blogService.getById(commentEntity.getBlogId());
            blogEntity.setCommentSize(blogCommentNum);
            blogService.updateById(blogEntity);

            return R.ok().put("code",true).put("commentEntity",commentEntity);
        }
        return R.ok().put("code",false).put("commentEntity", "");
    }

    @Override
    public R deleteComment(CommentEntity commentEntity, HttpServletRequest request) {
        commentEntity = baseMapper.selectOne(new QueryWrapper<CommentEntity>().eq("id",commentEntity.getId()));
        String jwtToken = request.getHeader(jwtTokenUtil.getHeader());
        if (!StringUtils.isEmpty(jwtToken)) {
            String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            UserEntity userEntity = userService.getOne(new QueryWrapper<UserEntity>().eq("username",username));
            if(userEntity.getId() == commentEntity.getUserId() || userEntity.getRoles().equals("ROLE_ADMIN") || userEntity.getRoles().equals("ROLE_SUPER")){
                //如果该评论是用户本人发送的  或者拥有管理员权限的人可以删除
                //删除时要把该评论和该评论下的所有子评论都删除
                baseMapper.delete(new QueryWrapper<CommentEntity>().eq("id",commentEntity.getId()).or().eq("parent_id",commentEntity.getId()));

                //相应修改文章的评论量
                int blogCommentNum = baseMapper.selectCount(new QueryWrapper<CommentEntity>().eq("blog_id",commentEntity.getBlogId()));
                BlogEntity blogEntity = blogService.getById(commentEntity.getBlogId());
                blogEntity.setCommentSize(blogCommentNum);
                blogService.updateById(blogEntity);

                return R.ok().put("code",true);
            }
        }
        return R.ok().put("code",false);
    }

    @Override
    public R searchUsernameAndAvatarByUserId(CommentEntity commentEntity) {
        commentEntity = baseMapper.selectById(commentEntity.getId());
        UserEntity userEntity = userService.getById(commentEntity.getUserId());
        return R.ok().put("username", userEntity.getUsername()).put("avatar",userEntity.getAvatar());
    }

    @Override
    public R searchUserById(CommentEntity commentEntity) {
        commentEntity = baseMapper.selectById(commentEntity.getId());
        UserEntity userEntity = userService.getOne(new QueryWrapper<UserEntity>().eq("id", commentEntity.getUserId()));
        return R.ok().put("userId",userEntity.getId()).put("role",userEntity.getRoles());
    }

    /**
     * 把某篇文章的评论转换成树形数据
     */
    private List<CommentEntity> listCommentWithTree(int blogId){

//        int blog_id = 1;
        // 1.根据blog_id查找出所有comment记录，得到该博文下的所有评论，返回List<CommentEntity>
        List<CommentEntity> entities = baseMapper.selectList(new QueryWrapper<CommentEntity>().eq("blog_id",blogId));
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



}