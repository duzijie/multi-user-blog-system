package com.yilan.blog.service.impl;

import com.yilan.blog.entity.UserEntity;
import com.yilan.blog.utils.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.yilan.blog.dao.BlogDao;
import com.yilan.blog.entity.BlogEntity;
import com.yilan.blog.service.BlogService;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


@Service("blogService")
public class BlogServiceImpl extends ServiceImpl<BlogDao, BlogEntity> implements BlogService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<BlogEntity> page = this.page(
                new Query<BlogEntity>().getPage(params),
                new QueryWrapper<BlogEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public R uploadBlogImg(MultipartFile file) {
        //获取文件的内容
        try {
            //获取原始文件名
            String originalFilename = file.getOriginalFilename();                   //photo.png
            //生成一个uuid名称出来
            String uuidFilename = UploadUtils.getUUIDName(originalFilename);        //A8EB4E79D5904946968BEAADE9C2D25D.png

//            File targetPath = new File("D:/img/blog/");      //Windows test
            File targetPath = new File("/opt/img/blog/");      //Linux run
            if (!targetPath.exists()) {
                targetPath.mkdirs();
            }

            //先把用户上传的图像先存储到服务器
            File localUpdatedAvatar = new File(targetPath.getAbsolutePath(), uuidFilename);     //D:\img\blog\A8EB4E79D5904946968BEAADE9C2D25D.png
            file.transferTo(localUpdatedAvatar);

            UrlTransferUtils urlTransferUtils = new UrlTransferUtils();
            //把新图像转换成url地址 并返回
            String httpUrlToDB = urlTransferUtils.localAbsoluteTransferToHttpUrl(localUpdatedAvatar.getAbsolutePath());

            return R.ok().put("code",true).put("imgUrl",httpUrlToDB);

        } catch (IOException e) {
            e.printStackTrace();
            return R.ok().put("code",false).put("imgUrl","");
        }
    }
    

    //返回最新的20条文章,不够20条就返回全部
    public List<BlogEntity> listToHomePage() {
        List<BlogEntity> entities = baseMapper.selectList(new QueryWrapper<BlogEntity>()
                .in("status",0,1,2).in("authority",0));

        if (entities.size() >= 20) {//判断list长度
            List newList = entities.subList(entities.size()-20, entities.size());//取最新的20条数据
            Collections.reverse(newList);  //倒叙排序
            return newList;//返回新的list
        } else {
            Collections.reverse(entities);
            return entities;
        }
    }

//    public IPage<BlogEntity> pagelist(Integer num,Integer size){
//
//        //1. 开启分页查询
//        PageHelper.startPage(num,size);
//        //2. 从数据库拉取信息
//        List<BlogEntity> blog =  .getAllFullAlgorithms(keyWord);
//        //3. 封装到 pageInfo 中
//        PageInfo pageInfo = new PageInfo(algorithmResponseVos,5);
//        //4. 传给前端
//        return CommonResult.success().add("pageInfo",pageInfo);
//    }

    //按分类返回
    public List<BlogEntity> listByCatalog(Integer catalogId) {
        List<BlogEntity> entities = baseMapper.selectList(new QueryWrapper<BlogEntity>()
                .eq("catalog_id",catalogId).in("status",0,1,2).in("authority",0));

        return entities;
    }

    //按阅读量返回10条
    public List<BlogEntity> listByRead() {
        List<BlogEntity> entities = baseMapper.selectList(new QueryWrapper<BlogEntity>()
                .in("status",0,1,2).in("authority",0).orderByDesc("read_size"));

        if (entities.size() >= 10) {
            List newList = entities.subList(0, 10);
            return newList;
        } else {
            return entities;
        }
    }

    //按点赞量返回10条
    public List<BlogEntity> listByVote() {
        List<BlogEntity> entities = baseMapper.selectList(new QueryWrapper<BlogEntity>()
                .in("status",0,1,2).in("authority",0).orderByDesc("vote_size"));

        if (entities.size() >= 10) {
            List newList = entities.subList(0,10);
            return newList;
        } else {
            return entities;
        }
    }

    //按评论量返回10条
    public List<BlogEntity> listByComment() {
        List<BlogEntity> entities = baseMapper.selectList(new QueryWrapper<BlogEntity>()
                .in("status",0,1,2).in("authority",0).orderByDesc("comment_size"));

        if (entities.size() >= 10) {
            List newList = entities.subList(0,10);
            return newList;
        } else {
            return entities;
        }
    }

    //模糊查询
    public List<BlogEntity> fuzzyQuery(String queryString) {
        List<BlogEntity> entities = baseMapper.selectList(new QueryWrapper<BlogEntity>()
                .like("title",queryString).or().like("summary",queryString));

        return entities;
    }

    //返回用户正常发布文章
    public List<BlogEntity> listByUser(Integer userId) {
        List<BlogEntity> entities = baseMapper.selectList(new QueryWrapper<BlogEntity>().eq("user_id",userId)
                .in("status",0));

        return entities;
    }

    //返回用户草稿文章
    public List<BlogEntity> listByDraft(Integer userId) {
        List<BlogEntity> entities = baseMapper.selectList(new QueryWrapper<BlogEntity>().eq("user_id",userId)
                .in("status",1));

        return entities;
    }

    //返回用户正在审核文章
    public List<BlogEntity> listByCheck(Integer userId) {
        List<BlogEntity> entities = baseMapper.selectList(new QueryWrapper<BlogEntity>().eq("user_id",userId)
                .in("status",2));

        return entities;
    }

    //返回全部违规文章
    public List<BlogEntity> listIllgal() {
        List<BlogEntity> entities = baseMapper.selectList(new QueryWrapper<BlogEntity>().in("status",2,3));

        return entities;
    }

    //模糊查询违规文章,每个参数都可以为空或不为空
    public List<BlogEntity> illegalFuzzyQuery(Integer userId,String title,String status) {
        List<BlogEntity> entities = new ArrayList<>();
            if(userId==null&&title==""&&status==""){
                entities = baseMapper.selectList(new QueryWrapper<BlogEntity>().in("status",2,3));

            }else if(userId==null&&title==""&&status!=""){
                entities = baseMapper.selectList(new QueryWrapper<BlogEntity>().eq("status",status));

            }else if(userId==null&&title!=""&&status==""){
                entities = baseMapper.selectList(new QueryWrapper<BlogEntity>().like("title", title)
                        .in("status",2,3));

            }else if(userId==null&&title!=""&&status!=""){
                entities = baseMapper.selectList(new QueryWrapper<BlogEntity>().like("title", title)
                        .eq("status",status));

            }else if(userId!=null&&title==""&&status==""){
                entities = baseMapper.selectList(new QueryWrapper<BlogEntity>().eq("user_id",userId)
                        .in("status",2,3));

            }else if(userId!=null&&title==""&&status!=""){
                entities = baseMapper.selectList(new QueryWrapper<BlogEntity>().eq("user_id",userId)
                        .eq("status",status));

            }else if(userId!=null&&title!=""&&status==""){
                entities = baseMapper.selectList(new QueryWrapper<BlogEntity>().eq("user_id",userId)
                        .like("title",title).in("status",2,3));

            }else{//ok
                entities = baseMapper.selectList(new QueryWrapper<BlogEntity>().eq("user_id",userId)
                        .like("title", title).eq("status",status));
            }
        return entities;
    }



}