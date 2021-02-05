package com.hh.controller.center;

import com.hh.controller.BaseController;
import com.hh.pojo.Orders;
import com.hh.service.OrderService;
import com.hh.service.center.MyOrdersService;
import com.hh.utils.PagedGridResult;
import com.hh.utils.Result;
import com.hh.vo.OrderStatusCountsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Api(value = "用户中心我的订单", tags = {"用户中心我的订单接口"})
@RestController
@RequestMapping("myorders")
public class MyOrdersController extends BaseController {

    @Autowired
    private MyOrdersService myOrdersService;


    @ApiOperation(value = "查询订单列表", notes = "查询订单列表", httpMethod = "POST")
    @PostMapping("/query")
    public Result query(@RequestParam String userId, @RequestParam Integer orderStatus,
                           @RequestParam Integer page, @RequestParam Integer pageSize) {
        if (StringUtils.isBlank(userId)) {
            return Result.errorMsg("");
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = COMMENT_PAGE_SIZE;
        }

        PagedGridResult result = myOrdersService.queryMyOrders(userId, orderStatus, page, pageSize);

        return Result.ok(result);
    }


    @ApiOperation(value = "发货", notes = "发货", httpMethod = "GET")
    @GetMapping("/deliver")
    public Result deliver(@RequestParam String orderId) {
        if (StringUtils.isBlank(orderId)) {
            return Result.errorMsg("");
        }
        myOrdersService.updateDeliverOrderStatus(orderId);

        return Result.ok();
    }


    @ApiOperation(value = "确认收货", notes = "确认收货", httpMethod = "POST")
    @PostMapping("/confirmReceive")
    public Result confirmReceive(@RequestParam String userId, @RequestParam String orderId) {
        if (StringUtils.isBlank(orderId) || StringUtils.isBlank(userId)) {
            return Result.errorMsg("");
        }

        Result checkResult = checkUserOrder(userId, orderId);
        if (checkResult.getStatus() != HttpStatus.OK.value()) {
            return checkResult;
        }

        boolean result = myOrdersService.updateReceiveOrderStatus(orderId);
        if (!result) {
            return Result.errorMsg("失败");
        }

        return Result.ok();
    }

    @ApiOperation(value = "删除订单", notes = "删除订单", httpMethod = "POST")
    @PostMapping("/delete")
    public Result delete(@RequestParam String userId, @RequestParam String orderId) {
        if (StringUtils.isBlank(orderId) || StringUtils.isBlank(userId)) {
            return Result.errorMsg("");
        }

        Result checkResult = checkUserOrder(userId, orderId);
        if (checkResult.getStatus() != HttpStatus.OK.value()) {
            return checkResult;
        }

        boolean result = myOrdersService.deleteOrder(userId, orderId);
        if (!result) {
            return Result.errorMsg("失败");
        }

        return Result.ok();
    }

    @ApiOperation(value = "订单状态总数统计", notes = "订单状态总数统计", httpMethod = "POST")
    @PostMapping("/statusCounts")
    public Result statusCounts(@RequestParam String userId) {
        if (StringUtils.isBlank(userId)) {
            return Result.errorMsg("");
        }

        OrderStatusCountsVO orderStatusCounts = myOrdersService.getOrderStatusCounts(userId);

        return Result.ok(orderStatusCounts);
    }

    @ApiOperation(value = "trend", notes = "trend", httpMethod = "POST")
    @PostMapping("/trend")
    public Result trend(@RequestParam String userId,
                        @RequestParam Integer page, @RequestParam Integer pageSize) {
        if (StringUtils.isBlank(userId)) {
            return Result.errorMsg("");
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = COMMENT_PAGE_SIZE;
        }

        PagedGridResult result = myOrdersService.getOrdersTrend(userId, page, pageSize);

        return Result.ok(result);
    }




}
