package com.hh.service.impl;

import com.hh.bo.SubmitOrderBO;
import com.hh.enums.OrderStatusEnum;
import com.hh.enums.YesOrNo;
import com.hh.mapper.CarouselMapper;
import com.hh.mapper.OrderItemsMapper;
import com.hh.mapper.OrderStatusMapper;
import com.hh.mapper.OrdersMapper;
import com.hh.pojo.*;
import com.hh.service.AddressService;
import com.hh.service.CarouselService;
import com.hh.service.ItemService;
import com.hh.service.OrderService;
import com.hh.utils.DateUtil;
import com.hh.vo.MerchantOrdersVO;
import com.hh.vo.OrderVO;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @author HuangHao
 * @date 2020/11/17 20:50
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderItemsMapper orderItemsMapper;
    @Autowired
    private OrderStatusMapper orderStatusMapper;
    @Autowired
    private AddressService addressService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private Sid sid;


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public OrderVO createOrder(SubmitOrderBO submitOrderBO) {

        String userId = submitOrderBO.getUserId();
        String addressId = submitOrderBO.getAddressId();
        String itemSpecIds = submitOrderBO.getItemSpecIds();
        Integer payMethod = submitOrderBO.getPayMethod();
        String leftMsg = submitOrderBO.getLeftMsg();
        // 包邮费用
        Integer postAmount = 0;

        String orderId = sid.nextShort();

        UserAddress address = addressService.queryUserAddress(userId, addressId);

        Orders newOrder = new Orders();
        newOrder.setId(orderId);
        newOrder.setUserId(userId);
        newOrder.setReceiverName(address.getReceiver());
        newOrder.setReceiverMobile(address.getMobile());
        newOrder.setReceiverAddress(address.getProvince() + " "
                + address.getCity() + " "
                + address.getDistrict() + " "
                + address.getDetail());
        newOrder.setPostAmount(postAmount);
        newOrder.setLeftMsg(leftMsg);
        newOrder.setPayMethod(payMethod);
        newOrder.setIsComment(YesOrNo.NO.type);
        newOrder.setIsDelete(YesOrNo.NO.type);
        newOrder.setCreatedTime(new Date());
        newOrder.setUpdatedTime(new Date());



        String[] itemSpecIdArr = itemSpecIds.split(",");
        Integer totalAmount = 0;
        Integer realPayamount = 0;
        for (String itemSpecId : itemSpecIdArr) {
            int buyCounts = 1; // TODO 整合redis后，数量从redis中的购物车里获取

            ItemsSpec itemsSpec = itemService.queryItemsSpecById(itemSpecId);
            totalAmount += itemsSpec.getPriceNormal() * buyCounts;
            realPayamount += itemsSpec.getPriceDiscount() * buyCounts;

            String itemId = itemsSpec.getItemId();
            Items item = itemService.queryItemById(itemId);
            String imgUrl = itemService.queryItemMainImgById(itemId);

            OrderItems orderItems = new OrderItems();
            orderItems.setId(sid.nextShort());
            orderItems.setItemId(itemId);
            orderItems.setOrderId(orderId);
            orderItems.setItemImg(imgUrl);
            orderItems.setItemName(item.getItemName());
            orderItems.setBuyCounts(buyCounts);
            orderItems.setItemSpecId(itemSpecId);
            orderItems.setItemSpecName(itemsSpec.getName());
            orderItems.setPrice(itemsSpec.getPriceDiscount());

            orderItemsMapper.insert(orderItems);

            // 扣库存
            itemService.decreaseItemSpecStock(itemSpecId, buyCounts);
        }

        newOrder.setTotalAmount(totalAmount);
        newOrder.setRealPayAmount(realPayamount);

        ordersMapper.insert(newOrder);

        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setCreatedTime(new Date());
        orderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        orderStatusMapper.insert(orderStatus);


        MerchantOrdersVO merchantOrdersVO = new MerchantOrdersVO();
        merchantOrdersVO.setMerchantOrderId(orderId);
        merchantOrdersVO.setMerchantUserId(userId);
        merchantOrdersVO.setAmount(realPayamount + postAmount);
        merchantOrdersVO.setPayMethod(payMethod);

        OrderVO orderVO = new OrderVO();
        orderVO.setOrderId(orderId);
        orderVO.setMerchantOrdersVO(merchantOrdersVO);

        return orderVO;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateOrderStatus(String orderId, Integer orderStatus) {
        OrderStatus paidStatus = new OrderStatus();
        paidStatus.setOrderId(orderId);
        paidStatus.setOrderStatus(orderStatus);
        paidStatus.setPayTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(paidStatus);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public OrderStatus queryOrderStatusInfo(String orderId) {
        return orderStatusMapper.selectByPrimaryKey(orderId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void closeOrder() {
        // 查询所有未付款订单，并判断时间是否超时(1天)
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        List<OrderStatus> list = orderStatusMapper.select(orderStatus);

        for (OrderStatus os : list) {
            Date createdTime = os.getCreatedTime();
            int days = DateUtil.daysBetween(createdTime, new Date());
            if (days >= 1) {
                doCloseOrder(os.getOrderId());
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    void doCloseOrder(String orderId) {
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setOrderStatus(OrderStatusEnum.CLOSE.type);
        orderStatus.setCloseTime(new Date());

        orderStatusMapper.updateByPrimaryKeySelective(orderStatus);
    }
}
