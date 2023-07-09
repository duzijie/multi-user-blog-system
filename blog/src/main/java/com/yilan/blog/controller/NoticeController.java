package com.yilan.blog.controller;


import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yilan.blog.entity.NoticeEntity;
import com.yilan.blog.service.NoticeService;
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
@RequestMapping("blogsystem/notice")
public class NoticeController {
    @Autowired
    private NoticeService noticeService;



    /**
     * 按id查
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Integer id){
		NoticeEntity notice = noticeService.getById(id);

        return R.ok().put("notice", notice);
    }

    /**
     * 新增公告
     */
    @RequestMapping("/save")
    public R save(@RequestBody NoticeEntity notice){
        noticeService.save(notice);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody NoticeEntity notice){
        noticeService.updateById(notice);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete/{id}")
    public R delete(@PathVariable("id") Integer id){
        noticeService.removeById(id);

        return R.ok();
    }


    /**
     * 返回所有状态为1，需要正常显示的公告
     */
    @RequestMapping("/noticeList")
    public R list(){
        List<NoticeEntity> entities = noticeService.noticeList();

        return R.ok().put("entities", entities);
    }

    /**
     * 返回所有公告
     */
    @RequestMapping("/getAllNotice")
    public R getAllNotice(){
        List<NoticeEntity> entities = noticeService.getAllNotice();

        return R.ok().put("entities", entities);
    }

    /**
     * 将发布文章取消展示，修改状态为0
     */
    @RequestMapping("/cancelNotice")
    public R cancelNotice(@RequestParam(value = "noticeId", required = true) Integer noticeId){
        NoticeEntity notice = noticeService.getById(noticeId);
        notice.setStatus(0);
        noticeService.updateById(notice);
        return R.ok();
    }

    /**
     * 将取消展示的文章发布，修改状态为1
     */
    @RequestMapping("/releaseNotice")
    public R releaseNotice(@RequestParam(value = "noticeId", required = true) Integer noticeId){
        NoticeEntity notice = noticeService.getById(noticeId);
        notice.setStatus(1);
        noticeService.updateById(notice);
        return R.ok();
    }

    
    
    
    
    
    
    /**
     * 列表
     */
//    @RequestMapping("/list")
//    public R list(@RequestParam Map<String, Object> params){
//        PageUtils page = noticeService.queryPage(params);
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
//		NoticeEntity notice = noticeService.getById(id);
//
//        return R.ok().put("notice", notice);
//    }
//
//    /**
//     * 保存
//     */
//    @RequestMapping("/save")
//    public R save(@RequestBody NoticeEntity notice){
//		noticeService.save(notice);
//
//        return R.ok();
//    }
//
//    /**
//     * 修改
//     */
//    @RequestMapping("/update")
//    public R update(@RequestBody NoticeEntity notice){
//		noticeService.updateById(notice);
//
//        return R.ok();
//    }
//
//    /**
//     * 删除
//     */
//    @RequestMapping("/delete")
//    public R delete(@RequestBody Integer[] ids){
//		noticeService.removeByIds(Arrays.asList(ids));
//
//        return R.ok();
//    }

}
