package com.hh.controller;

import com.hh.pojo.Items;
import com.hh.pojo.ItemsImg;
import com.hh.pojo.ItemsParam;
import com.hh.pojo.ItemsSpec;
import com.hh.service.ItemService;
import com.hh.utils.PagedGridResult;
import com.hh.utils.Result;
import com.hh.vo.CommentLevelCountsVO;
import com.hh.vo.ItemInfoVO;
import com.hh.vo.ShopcartVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author HuangHao
 * @date 2020/11/23 21:42
 */
@Api(value = "商品详情", tags = {"用于商品详情展示的相关接口"})
@RestController
@RequestMapping("items")
public class ItemController extends BaseController {

    @Autowired
    private ItemService itemService;


    @ApiOperation(value = "查询商品详情", notes = "查询商品详情", httpMethod = "GET")
    @GetMapping("/info/{itemId}")
    public Result info(
            @ApiParam(name = "itemId", value = "商品id", required = true)
            @PathVariable String itemId) {
        if (StringUtils.isBlank(itemId)) {
            return Result.errorMsg("");
        }
        Items item = itemService.queryItemById(itemId);
        List<ItemsImg> itemsImgList = itemService.queryItemImgList(itemId);
        List<ItemsSpec> itemsSpecList = itemService.queryItemSpecList(itemId);
        ItemsParam itemsParam = itemService.queryItemParam(itemId);

        ItemInfoVO itemInfoVO = new ItemInfoVO();
        itemInfoVO.setItem(item);
        itemInfoVO.setItemImgList(itemsImgList);
        itemInfoVO.setItemSpecList(itemsSpecList);
        itemInfoVO.setItemParams(itemsParam);

        return Result.ok(itemInfoVO);
    }


    @ApiOperation(value = "查询商品评价等级", notes = "查询商品评价等级", httpMethod = "GET")
    @GetMapping("/commentLevel")
    public Result commentLevel(
            @ApiParam(name = "itemId", value = "商品id", required = true)
            @RequestParam String itemId) {
        if (StringUtils.isBlank(itemId)) {
            return Result.errorMsg("");
        }

        CommentLevelCountsVO result = itemService.queryCommentCounts(itemId);

        return Result.ok(result);
    }

    @ApiOperation(value = "查询商品评价等级", notes = "查询商品评价等级", httpMethod = "GET")
    @GetMapping("/comments")
    public Result comments(@RequestParam String itemId, @RequestParam Integer level,
                           @RequestParam Integer page, @RequestParam Integer pageSize) {
        if (StringUtils.isBlank(itemId)) {
            return Result.errorMsg("");
        }

        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = COMMENT_PAGE_SIZE;
        }

        PagedGridResult result = itemService.queryPagedComments(itemId, level, page, pageSize);

        return Result.ok(result);
    }

    @ApiOperation(value = "查询商品结果", notes = "查询商品结果", httpMethod = "GET")
    @GetMapping("/search")
    public Result search(@RequestParam String keywords, @RequestParam String sort,
                         @RequestParam Integer page, @RequestParam Integer pageSize) {
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }

        PagedGridResult result = itemService.searchItems(keywords, sort, page, pageSize);

        return Result.ok(result);
    }

    @ApiOperation(value = "查询商品结果ByCatId", notes = "查询商品结果ByCatId", httpMethod = "GET")
    @GetMapping("/catItems")
    public Result catItems(@RequestParam Integer catId, @RequestParam String sort,
                           @RequestParam Integer page, @RequestParam Integer pageSize) {
        if (catId == null) {
            return Result.errorMsg("");
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }

        PagedGridResult result = itemService.searchItemsByThirdCat(catId, sort, page, pageSize);

        return Result.ok(result);
    }

    // 用于用户长时间未登录网站，刷新购物车中的数据（主要是商品价格），类似京东淘宝
    @ApiOperation(value = "根据商品规格ids查找最新的商品数据", notes = "根据商品规格ids查找最新的商品数据", httpMethod = "GET")
    @GetMapping("/refresh")
    public Result refresh(
            @ApiParam(name = "itemSpecIds", value = "拼接的规格ids", required = true, example = "1001,1003,1005")
            @RequestParam String itemSpecIds) {

        if (StringUtils.isBlank(itemSpecIds)) {
            return Result.ok();
        }

        List<ShopcartVO> list = itemService.queryItemsBySpecIds(itemSpecIds);

        return Result.ok(list);
    }

}
