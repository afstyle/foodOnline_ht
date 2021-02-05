package com.hh.mapper;

import com.hh.pojo.OrderStatus;
import com.hh.vo.MyOrdersVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrdersMapperCustom {

    public List<MyOrdersVO> queryMyOrders(@Param("paramsMap") Map<String, Object> map);

    public int getMyOrderStatusCounts(@Param("map") Map<String, Object> map);

    public List<OrderStatus> getMyOrderTrend(@Param("map") Map<String, Object> map);
}