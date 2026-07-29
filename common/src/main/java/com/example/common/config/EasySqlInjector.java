package com.example.common.config;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;

import java.util.List;

public class EasySqlInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        // 1. 调用父类，保留 MP 自带的所有方法（如 insert、selectById 等）
        List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);

        // 2. ★ 添加我们的批量插入方法
        //    参数说明：i -> i.getFieldFill() != FieldFill.UPDATE
        //    表示排除那些被标记为“更新时自动填充”的字段（比如更新时间），不插入这些字段
        methodList.add(new InsertBatchSomeColumn(i ->i.getFieldFill() != FieldFill.UPDATE));
        return methodList;
    }
}
