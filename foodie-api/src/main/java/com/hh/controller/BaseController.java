package com.hh.controller;

import org.springframework.stereotype.Controller;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author HuangHao
 * @date 2020/10/12 0:10
 */
@ApiIgnore
@Controller
public class BaseController {

    public static final String SHOPCART = "shopcart";

    public static final String PAY_RETURN_URL = "http://localhost:8088/orders/notifyMerchantOrderPaid";
    public static final String PAYMENT_URL = "http://payment.t.mukewang.com/foodie-payment/payment/createMerchantOrder";


    public static final Integer COMMENT_PAGE_SIZE = 10;
    public static final Integer PAGE_SIZE = 20;

    // 用户上传头像地址
    public static final String IMAGE_USER_FACE_LOCATION = "D:\\test";

}
