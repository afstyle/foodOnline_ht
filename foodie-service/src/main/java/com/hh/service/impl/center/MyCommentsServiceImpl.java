package com.hh.service.impl.center;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hh.bo.center.OrderItemsCommentBO;
import com.hh.enums.YesOrNo;
import com.hh.mapper.*;
import com.hh.pojo.OrderItems;
import com.hh.pojo.OrderStatus;
import com.hh.pojo.Orders;
import com.hh.service.center.MyCommentsService;
import com.hh.utils.PagedGridResult;
import com.hh.vo.MyCommentVO;
import com.hh.vo.MyOrdersVO;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MyCommentsServiceImpl extends BaseService implements MyCommentsService {

    @Autowired
    private OrderItemsMapper orderItemsMapper;
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderStatusMapper orderStatusMapper;
    @Autowired
    private ItemsCommentsMapperCustom itemsCommentsMapperCustom;

    @Autowired
    private Sid sid;


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<OrderItems> queryPendingComment(String orderId) {
        OrderItems orderItems = new OrderItems();
        orderItems.setOrderId(orderId);

        return orderItemsMapper.select(orderItems);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveComments(String orderId, String userId,
                             List<OrderItemsCommentBO> commentList) {
        for (OrderItemsCommentBO oic : commentList) {
            oic.setCommentId(sid.nextShort());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("commentList", commentList);
        itemsCommentsMapperCustom.saveComments(map);

        Orders orders = new Orders();
        orders.setId(orderId);
        orders.setIsComment(YesOrNo.YES.type);
        ordersMapper.updateByPrimaryKeySelective(orders);

        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setCommentTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(orderStatus);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryMyComments(String userId, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        PageHelper.startPage(page, pageSize);
        List<MyCommentVO> result = itemsCommentsMapperCustom.queryMyComments(map);

        return setterPagedGrid(result, page);
    }

}
