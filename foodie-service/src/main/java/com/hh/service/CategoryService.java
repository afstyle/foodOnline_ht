package com.hh.service;

import com.hh.pojo.Carousel;
import com.hh.pojo.Category;
import com.hh.vo.CategoryVO;
import com.hh.vo.NewItemsVO;

import java.util.List;

/**
 * @author HuangHao
 * @date 2020/11/17 20:48
 */
public interface CategoryService {


    /**
     * 查询所有一级分类
     * @return
     */
    public List<Category> queryAllRootLevelCat();


    /**
     * 根据一级分类的id查询子分类
     * @param rootCatId
     * @return
     */
    public List<CategoryVO> getSubCatList(Integer rootCatId);


    /**
     * 查询首页每个一级分类下的六个商品
     * @param rootCatId
     * @return
     */
    public List<NewItemsVO> getSixNewItemsLazy(Integer rootCatId);


}
