package com.yilan.blog.config.mybatisplus.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    //insert操作时要填充的字段
    @Override
    public void insertFill(MetaObject metaObject) {
        //根据属性名字设置要填充的值
        this.strictInsertFill(metaObject,"createTime", Date.class, new Date());
    }

    //update操作时要填充的字段
    @Override
    public void updateFill(MetaObject metaObject) {
    	this.strictInsertFill(metaObject,"createTime", Date.class, new Date());
        this.strictUpdateFill(metaObject, "updateTime", Date.class, new Date()); // 起始版本 3.3.0(推荐)
    }
}
