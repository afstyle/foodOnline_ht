package com.hh.controller;

import com.hh.pojo.Orders;
import com.hh.pojo.Users;
import com.hh.service.center.MyOrdersService;
import com.hh.utils.RedisOperator;
import com.hh.utils.Result;
import com.hh.vo.UsersVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import springfox.documentation.annotations.ApiIgnore;

import java.util.UUID;

/**
 * @author HuangHao
 * @date 2020/10/12 0:10
 */
@ApiIgnore
@Controller
public class BaseController {

    public static final String SHOPCART = "shopcart";
    public static final String REDIS_USER_TOKEN = "redis_user_token";

    // 支付相关
    public static final String PAY_RETURN_URL = "http://localhost:8088/orders/notifyMerchantOrderPaid";
    public static final String PAYMENT_URL = "http://payment.t.mukewang.com/foodie-payment/payment/createMerchantOrder";


    public static final Integer COMMENT_PAGE_SIZE = 10;
    public static final Integer PAGE_SIZE = 20;

    // 用户上传头像地址
    public static final String IMAGE_USER_FACE_LOCATION = "D:\\test";


    @Autowired
    private MyOrdersService myOrdersService;
    @Autowired
    private RedisOperator redisOperator;

    public Result checkUserOrder(String userId, String orderId) {
        Orders orders = myOrdersService.queryMyOrder(userId, orderId);
        if (orders == null) {
            Result.errorMsg("订单不存在");
        }
        return Result.ok(orders);
    }

    public UsersVO conventUserVO(Users users) {
        // redis会话
        String token = UUID.randomUUID().toString().trim();
        redisOperator.set(REDIS_USER_TOKEN + ":" + users.getId(), token);

        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(users, usersVO);
        usersVO.setUserUniqueToken(token);

        return usersVO;
    }
}
