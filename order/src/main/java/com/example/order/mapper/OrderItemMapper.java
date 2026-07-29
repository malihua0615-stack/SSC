package com.example.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.common.entity.OrderItemEntity;
import com.example.common.imp.EasyBaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderItemMapper extends EasyBaseMapper<OrderItemEntity> {
}
