package com.yilan.blog.service.impl;

import com.yilan.blog.entity.UserEntity;
import com.yilan.blog.service.UserService;
import com.yilan.blog.utils.JwtTokenUtil;
import com.yilan.blog.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yilan.blog.utils.PageUtils;
import com.yilan.blog.utils.Query;

import com.yilan.blog.dao.LetterDao;
import com.yilan.blog.entity.LetterEntity;
import com.yilan.blog.service.LetterService;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;


@Service("letterService")
public class LetterServiceImpl extends ServiceImpl<LetterDao, LetterEntity> implements LetterService {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<LetterEntity> page = this.page(
                new Query<LetterEntity>().getPage(params),
                new QueryWrapper<LetterEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public R searchUsersByToken(HttpServletRequest request) {
        String jwtToken = request.getHeader(jwtTokenUtil.getHeader());
        if (!StringUtils.isEmpty(jwtToken)) {
            String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            UserEntity myUserEntity = userService.getOne(new QueryWrapper<UserEntity>().eq("username",username));
            //按时间降序找出跟我留过言的letterEntity
            List<LetterEntity> letterEntityList = baseMapper.selectList(new QueryWrapper<LetterEntity>().eq("to_user_id", myUserEntity.getId()).orderByDesc("create_time"));

            //ids是跟我聊过天的用户的id
            List<Integer> ids = baseMapper.searchIdsLetterToMe(myUserEntity.getId());

            //找出跟我聊天的用户
            List<UserEntity> talkToMeUserEntityList = userService.listByIds(ids);
            Collections.reverse(talkToMeUserEntityList);
            return R.ok().put("res",talkToMeUserEntityList);
        }
        return R.ok().put("res","请重新登录");
    }

    @Override
    public R showLettersByUserId(HttpServletRequest request, int userId) {
        String jwtToken = request.getHeader(jwtTokenUtil.getHeader());
        if (!StringUtils.isEmpty(jwtToken)) {
            String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            UserEntity myUserEntity = userService.getOne(new QueryWrapper<UserEntity>().eq("username", username));
            UserEntity otherUserEntity = userService.getById(userId);     //otherUserEntity是左侧用户栏的某个用户

            //下面对留言内容按照时间降序进行处理
            List<LetterEntity> meToOtherLetterList = baseMapper.selectList(new QueryWrapper<LetterEntity>().eq("from_user_id",myUserEntity.getId()).eq("to_user_id",userId));
            System.out.println("======meToOtherLetterList=======");
            System.out.println(meToOtherLetterList);
            List<LetterEntity> otherToMeLetterList = baseMapper.selectList(new QueryWrapper<LetterEntity>().eq("to_user_id",myUserEntity.getId()).eq("from_user_id",userId));
            System.out.println("======otherToMeLetterList=======");
            System.out.println(otherToMeLetterList);
            List<LetterEntity> combineLetterList = new ArrayList<>();
            combineLetterList.addAll(meToOtherLetterList);
            combineLetterList.addAll(otherToMeLetterList);

            //开始按照时间降序进行排序
            if(combineLetterList.isEmpty())
                return R.ok().put("res","查询不到留言（应该走不到这一步，显示出这条信息请跟后端人员联系");
            for(int i=0; i<combineLetterList.size(); i++) {
                for(int j=0; j<combineLetterList.size()-1-i; j++) {
                    if(combineLetterList.get(j+1).getCreateTime().before(combineLetterList.get(j).getCreateTime())) {
                        LetterEntity letterEntity = new LetterEntity();
                        letterEntity = combineLetterList.get(j + 1);
                        combineLetterList.remove(j+1);combineLetterList.add(j+1,combineLetterList.get(j));
                        combineLetterList.remove(j);combineLetterList.add(j,letterEntity);
                        letterEntity = null;        //防止元素重复
                    }
                }
            }
            return R.ok().put("res",combineLetterList);
        }
        return R.ok().put("res","请重新登录");
    }

    @Override
    public R addLetterToUserId(HttpServletRequest request, LetterEntity letterEntity) {
        String jwtToken = request.getHeader(jwtTokenUtil.getHeader());
        if (!StringUtils.isEmpty(jwtToken)) {
            String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
            UserEntity myUserEntity = userService.getOne(new QueryWrapper<UserEntity>().eq("username", username));
            letterEntity.setFromUserId(myUserEntity.getId());
            letterEntity.setStatus(2);
            baseMapper.insert(letterEntity);
            return R.ok().put("code",true);
        }
        return R.ok().put("code",false);
    }


}