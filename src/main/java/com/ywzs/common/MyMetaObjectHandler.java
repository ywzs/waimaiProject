package com.ywzs.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.ywzs.utils.UserHolder;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 自定义的原对象处理器
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    /**
     * 每次插入对象时自动赋值
     * @param metaObject  11
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("createUser", UserHolder.getId());
        metaObject.setValue("updateUser",UserHolder.getId());
        metaObject.setValue("updateTime", LocalDateTime.now());
    }

    /**
     * 每次更新对象时自动赋值
     * @param metaObject 111
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateUser",UserHolder.getId());
        metaObject.setValue("updateTime", LocalDateTime.now());
    }
}
