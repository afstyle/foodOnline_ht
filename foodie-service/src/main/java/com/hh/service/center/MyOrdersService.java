package com.hh.service.center;

import com.hh.pojo.Orders;
import com.hh.utils.PagedGridResult;

/**
 * @author HuangHao
 * @date 2020/11/5 22:44
 */
public interface MyOrdersService {

    /**
     * 查询我的订单列表
     * @param userId
     * @param orderStatus
     * @param page
     * @param pageSize
     * @return
     */
    public PagedGridResult queryMyOrders(String userId, Integer orderStatus, Integer page, Integer pageSize);

    public void updateDeliverOrderStatus(String orderId);

    public Orders queryMyOrder(String userId, String orderId);

    public boolean updateReceiveOrderStatus(String orderId);

    public boolean deleteOrder(String userId, String orderId);
}
