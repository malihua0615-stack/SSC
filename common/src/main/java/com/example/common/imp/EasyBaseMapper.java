package com.example.common.imp;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Collection;

public interface EasyBaseMapper<T> extends BaseMapper<T> {

    // ★ 声明批量插入方法
    // 参数是实体类的集合，返回受影响的行数
    Integer insertBatchSomeColumn(Collection<T> entityList);
}
