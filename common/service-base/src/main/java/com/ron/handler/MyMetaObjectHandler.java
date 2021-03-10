package com.ron.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MyMetaObjectHandler  implements MetaObjectHandler {

    //执行添加方法,会执行此方法
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("gmtCreate",new Date(),metaObject);//填充创建时间
        this.setFieldValByName("gmtModified",new Date(),metaObject);//填充更新时间

    }

    //执行更新方法,会执行此方法
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("gmtModified",new Date(),metaObject);
    }
}
