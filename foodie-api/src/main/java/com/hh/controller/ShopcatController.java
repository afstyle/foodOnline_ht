package com.hh.controller;

import com.hh.bo.ShopcartBO;
import com.hh.utils.JsonUtils;
import com.hh.utils.RedisOperator;
import com.hh.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author HuangHao
 * @date 2020/10/12 0:10
 */
@Api(value = "购物车", tags = {"用于购物车的相关接口"})
@RestController
@RequestMapping("shopcart")
public class ShopcatController extends BaseController {

    @Autowired
    private RedisOperator redisOperator;

    @ApiOperation(value = "添加商品到购物车", notes = "添加商品到购物车", httpMethod = "POST")
    @PostMapping("/add")
    public Result add(@RequestParam String userId, @RequestBody ShopcartBO shopcartBO,
                      HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(userId)) {
            return Result.errorMsg("");
        }

        System.out.println(shopcartBO);

        // 前端用户在登录时，添加商品到购物车，会同时在后端同步购物车到redis缓存
        String redisKeyName = SHOPCART + ":" + userId;
        String shopcartJson = redisOperator.get(redisKeyName);
        List<ShopcartBO> shopcartList = new ArrayList<>();
        if (StringUtils.isNotBlank(shopcartJson)) {
            shopcartList = JsonUtils.jsonToList(shopcartJson, ShopcartBO.class);
            boolean isHavingGood = false;
            for (ShopcartBO bo : shopcartList) {
                String tempSpecId = bo.getSpecId();
                if (tempSpecId.equals(shopcartBO.getSpecId())) {
                    bo.setBuyCounts(bo.getBuyCounts() + shopcartBO.getBuyCounts());
                    isHavingGood = true;
                }
            }
            if (!isHavingGood) {
                shopcartList.add(shopcartBO);
            }
        } else {
            shopcartList = new ArrayList<>();
            shopcartList.add(shopcartBO);
        }
        redisOperator.set(redisKeyName, JsonUtils.objectToJson(shopcartList));

        return Result.ok();
    }


    @ApiOperation(value = "从购物车中删除商品", notes = "从购物车中删除商品", httpMethod = "POST")
    @PostMapping("/del")
    public Result del(@RequestParam String userId, @RequestParam String itemSpecId,
                      HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(itemSpecId)) {
            return Result.errorMsg("参数不能为空");
        }

        // 用户在页面删除购物车中的商品数据，如果此时用户已经登录，则需要同步删除redis购物车中的商品
        String redisKeyName = SHOPCART + ":" + userId;
        String shopcartJson = redisOperator.get(redisKeyName);
        if (StringUtils.isNotBlank(shopcartJson)) {
            List<ShopcartBO> shopcartList = JsonUtils.jsonToList(shopcartJson, ShopcartBO.class);
            for (ShopcartBO bo : shopcartList) {
                String tempSpecId = bo.getSpecId();
                if (tempSpecId.equals(itemSpecId)) {
                    shopcartList.remove(bo);
                    break;
                }
            }
            redisOperator.set(redisKeyName, JsonUtils.objectToJson(shopcartList));
        }

        return Result.ok();
    }


}
