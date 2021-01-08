package com.hh.controller;

import com.hh.bo.ShopcartBO;
import com.hh.bo.SubmitOrderBO;
import com.hh.enums.OrderStatusEnum;
import com.hh.enums.PayMethod;
import com.hh.pojo.OrderStatus;
import com.hh.service.OrderService;
import com.hh.utils.CookieUtils;
import com.hh.utils.Result;
import com.hh.vo.MerchantOrdersVO;
import com.hh.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.ResultSet;

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

    @Autowired
    private RestTemplate restTemplate;

    @ApiOperation(value = "用户下单", notes = "用户下单", httpMethod = "POST")
    @PostMapping("/create")
    public Result create(@RequestBody SubmitOrderBO submitOrderBO,
                         HttpServletRequest request,
                         HttpServletResponse response) {

        if (submitOrderBO.getPayMethod() != PayMethod.WEIXIN.type
                && submitOrderBO.getPayMethod() != PayMethod.ALIPAY.type) {
            return Result.errorMsg("支付方式不支持！");
        }
//        LOG.info(submitOrderBO.toString());

        // 1. 创建订单
        OrderVO orderVO = orderService.createOrder(submitOrderBO);
        String orderId = orderVO.getOrderId();


        // 2. 创建订单后，移除购物车中已结算的商品
        // TODO 整合redis后完善购物车中的已结算商品清除，并同步到前端的cookie
//        CookieUtils.setCookie(request, response, SHOPCART, "", true);

        // 3. 向支付中心发送当前订单，用于保存支付中心的订单数据
        MerchantOrdersVO merchantOrdersVO = orderVO.getMerchantOrdersVO();
        merchantOrdersVO.setReturnUrl(PAY_RETURN_URL);
        merchantOrdersVO.setAmount(1); // test

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("imoocUserId", "imooc");
        headers.add("password", "imooc");

        HttpEntity<MerchantOrdersVO> entity = new HttpEntity<>(merchantOrdersVO, headers);

        ResponseEntity<Result> resultResponseEntity = restTemplate.postForEntity(PAYMENT_URL, entity, Result.class);
        Result paymentResult = resultResponseEntity.getBody();
        if (paymentResult.getStatus() != 200) {
            return Result.errorMsg("支付失败:" + paymentResult.getMsg());
        }

        /*test用，付款回调的先跳过，直接付款成功即可*/
        this.notifyMerchantOrderPaid(orderId);

        return Result.ok(orderId);
    }


    @ApiOperation(value = "支付回调", notes = "支付回调", httpMethod = "POST")
    @PostMapping("/notifyMerchantOrderPaid")
    public Integer notifyMerchantOrderPaid(String merchantOrderId) {
        orderService.updateOrderStatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER.type);
        return HttpStatus.OK.value();
    }

    @ApiOperation(value = "轮询获取订单信息", notes = "轮询获取订单信息", httpMethod = "POST")
    @PostMapping("/getPaidOrderInfo")
    public Result getPaidOrderInfo(@RequestParam String orderId) {
        OrderStatus orderStatus = orderService.queryOrderStatusInfo(orderId);
        return Result.ok(orderStatus);
    }

}
