package com.hh.controller.center;

import com.hh.bo.center.OrderItemsCommentBO;
import com.hh.controller.BaseController;
import com.hh.enums.YesOrNo;
import com.hh.pojo.OrderItems;
import com.hh.pojo.Orders;
import com.hh.service.OrderService;
import com.hh.service.center.MyCommentsService;
import com.hh.service.center.MyOrdersService;
import com.hh.utils.PagedGridResult;
import com.hh.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "我的评论", tags = {"用户中心我的评论接口"})
@RestController
@RequestMapping("mycomments")
public class MyCommentsController extends BaseController {


    @Autowired
    public MyCommentsService myCommentsService;


    @ApiOperation(value = "查询订单列表", notes = "查询订单列表", httpMethod = "POST")
    @PostMapping("/pending")
    public Result pending(@RequestParam String userId, @RequestParam String orderId) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(orderId)) {
            return Result.errorMsg("");
        }

        Result checkResult = checkUserOrder(userId, orderId);
        if (checkResult.getStatus() != HttpStatus.OK.value()) {
            return checkResult;
        }

        Orders myOrder = (Orders) checkResult.getData();
        if (myOrder.getIsComment() == YesOrNo.YES.type) {
            return Result.errorMsg("该订单已评价过");
        }


        List<OrderItems> result = myCommentsService.queryPendingComment(orderId);


        return Result.ok(result);
    }


    @ApiOperation(value = "保存评论列表", notes = "保存评论列表", httpMethod = "POST")
    @PostMapping("/saveList")
    public Result saveList(@RequestParam String userId, @RequestParam String orderId,
                           @RequestBody List<OrderItemsCommentBO> commentList) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(orderId)) {
            return Result.errorMsg("");
        }
//        System.out.println(commentList);

        Result checkResult = checkUserOrder(userId, orderId);
        if (checkResult.getStatus() != HttpStatus.OK.value()) {
            return checkResult;
        }

        if (CollectionUtils.isEmpty(commentList)) {
            return Result.errorMsg("评论内容不能为空");
        }


        myCommentsService.saveComments(orderId, userId, commentList);

        return Result.ok();
    }

    @ApiOperation(value = "查询评价列表", notes = "查询评价列表", httpMethod = "POST")
    @PostMapping("/query")
    public Result query(@RequestParam String userId,
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

        PagedGridResult result = myCommentsService.queryMyComments(userId, page, pageSize);

        return Result.ok(result);
    }

}
