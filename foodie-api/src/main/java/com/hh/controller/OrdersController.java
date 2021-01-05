package com.hh.controller;

import com.hh.bo.ShopcartBO;
import com.hh.bo.SubmitOrderBO;
import com.hh.enums.PayMethod;
import com.hh.service.OrderService;
import com.hh.utils.CookieUtils;
import com.hh.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author HuangHao
 * @date 2020/10/12 0:10
 */
@Api(value = "订单", tags = {"用于订单的相关接口"})
@RestController
@RequestMapping("orders")
public class OrdersController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(HelloController.class);

    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "用户下单", notes = "用户下单", httpMethod = "POST")
    @PostMapping("/create")
    public Result create(@RequestBody SubmitOrderBO submitOrderBO,
                         HttpServletRequest request,
                         HttpServletResponse response) {

        if (submitOrderBO.getPayMethod() != PayMethod.WEIXIN.type
                && submitOrderBO.getPayMethod() != PayMethod.ALIPAY.type) {
            return Result.errorMsg("支付方式不支持！");
        }
        LOG.info(submitOrderBO.toString());

        // 1. 创建订单
        String orderId = orderService.createOrder(submitOrderBO);

        // 2. 创建订单后，移除购物车中已结算的商品
        // TODO 整合redis后完善购物车中的已结算商品清除，并同步到前端的cookie
//        CookieUtils.setCookie(request, response, SHOPCART, "", true);

        // 3. 向支付中心发送当前订单，用于保存支付中心的订单数据

        return Result.ok(orderId);
    }


}
