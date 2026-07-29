package com.example.order.mapper;

import com.example.common.entity.OutBoxMessageEntity;
import com.example.common.imp.EasyBaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OutBoxMessageMapper extends EasyBaseMapper<OutBoxMessageEntity> {
}
