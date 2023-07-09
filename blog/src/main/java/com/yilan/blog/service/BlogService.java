package com.yilan.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yilan.blog.utils.PageUtils;
import com.yilan.blog.entity.BlogEntity;
import com.yilan.blog.utils.R;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author GroupOne
 * @email ${email}
 * @date 2020-09-11 11:08:28
 */
public interface BlogService extends IService<BlogEntity> {

    PageUtils queryPage(Map<String, Object> params);

    R uploadBlogImg(MultipartFile file);
    
    List<BlogEntity> listToHomePage();
    List<BlogEntity> listByCatalog(Integer catalogId);
    List<BlogEntity> fuzzyQuery(String queryString);
    List<BlogEntity> listByUser(Integer userId);
    List<BlogEntity> listByCheck(Integer userId);
    List<BlogEntity> listByDraft(Integer userId);
    List<BlogEntity> listIllgal();
    List<BlogEntity> illegalFuzzyQuery(Integer userId,String title,String status);
    List<BlogEntity> listByRead();
    List<BlogEntity> listByVote();
    List<BlogEntity> listByComment();
}

