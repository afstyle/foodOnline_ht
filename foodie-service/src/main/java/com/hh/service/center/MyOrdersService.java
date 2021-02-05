package com.hh.service.center;

import com.hh.pojo.Orders;
import com.hh.utils.PagedGridResult;
import com.hh.vo.OrderStatusCountsVO;

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

    /**
     * 查询用户订单数
     * @param userId
     */
    public OrderStatusCountsVO getOrderStatusCounts(String userId);

    /**
     * 获得分页的订单动向
     * @param userId
     * @param page
     * @param pageSize
     * @return
     */
    public PagedGridResult getOrdersTrend(String userId, Integer page, Integer pageSize);
}
