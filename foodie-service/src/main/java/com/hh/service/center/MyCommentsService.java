package com.hh.service.center;

import com.hh.bo.center.OrderItemsCommentBO;
import com.hh.pojo.OrderItems;
import com.hh.utils.PagedGridResult;

import java.util.List;

/**
 * @author HuangHao
 * @date 2020/11/5 22:44
 */
public interface MyCommentsService {

    public List<OrderItems> queryPendingComment(String orderId);

    public void saveComments(String orderId, String userId, List<OrderItemsCommentBO> commentList);

    public PagedGridResult queryMyComments(String userId, Integer page, Integer pageSize);

}
