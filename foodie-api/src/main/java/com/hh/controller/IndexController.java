package com.hh.controller;

import com.hh.enums.YesOrNo;
import com.hh.pojo.Carousel;
import com.hh.pojo.Category;
import com.hh.service.CarouselService;
import com.hh.service.CategoryService;
import com.hh.utils.Result;
import com.hh.vo.CategoryVO;
import com.hh.vo.NewItemsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @author HuangHao
 * @date 2020/10/12 0:10
 */
@Api(value = "首页", tags = {"用于首页展示的相关接口"})
@RestController
@RequestMapping("index")
public class IndexController {

    private static final Logger LOG = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private CarouselService carouselService;

    @Autowired
    private CategoryService categoryService;


    @ApiOperation(value = "获取首页轮播图列表", notes = "获取首页轮播图列表", httpMethod = "GET")
    @GetMapping("/carousel")
    public Result carousel() {
        List<Carousel> carouselList = carouselService.queryAll(YesOrNo.YES.type);
        return Result.ok(carouselList);
    }


    @ApiOperation(value = "获取商品一级分类", notes = "获取商品一级分类", httpMethod = "GET")
    @GetMapping("/cats")
    public Result cats() {
        List<Category> result = categoryService.queryAllRootLevelCat();
        return Result.ok(result);
    }


    @ApiOperation(value = "获取商品子分类", notes = "获取商品子分类", httpMethod = "GET")
    @GetMapping("/subCat/{rootCatId}")
    public Result subCat(
            @ApiParam(name = "rootCatId", value = "一级分类id", required = true)
            @PathVariable Integer rootCatId) {
        if (rootCatId == null) {
            return Result.errorMsg("");
        }
        List<CategoryVO> result = categoryService.getSubCatList(rootCatId);
        return Result.ok(result);
    }

    @ApiOperation(value = "查询每个一级分类下六条商品数据", notes = "查询每个一级分类下六条商品数据", httpMethod = "GET")
    @GetMapping("/sixNewItems/{rootCatId}")
    public Result sixNewItems(
            @ApiParam(name = "rootCatId", value = "一级分类id", required = true)
            @PathVariable Integer rootCatId) {
        if (rootCatId == null) {
            return Result.errorMsg("");
        }
        List<NewItemsVO> result = categoryService.getSixNewItemsLazy(rootCatId);
        return Result.ok(result);
    }

}
