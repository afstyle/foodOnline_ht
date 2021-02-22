package com.hh.service;

import com.hh.bo.ShopcartBO;
import com.hh.bo.SubmitOrderBO;
import com.hh.pojo.OrderStatus;
import com.hh.vo.OrderVO;

import java.util.List;

/**
 * @author HuangHao
 * @date 2020/11/17 20:48
 */
public interface OrderService {


    public OrderVO createOrder(List<ShopcartBO> shopcartList, SubmitOrderBO submitOrderBO);


    public void updateOrderStatus(String orderId, Integer orderStatus);


    public OrderStatus queryOrderStatusInfo(String orderId);

    /**
     * 关闭超时未支付的订单
     */
    public void closeOrder();




}
