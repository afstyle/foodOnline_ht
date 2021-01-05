package com.hh.service;

import com.hh.pojo.*;
import com.hh.utils.PagedGridResult;
import com.hh.vo.CommentLevelCountsVO;
import com.hh.vo.ShopcartVO;

import java.util.List;

/**
 * @author HuangHao
 * @date 2020/11/5 22:44
 */
public interface ItemService {


    /**
     * 根据商品ID查询详情
     * @param itemId
     * @return
     */
    public Items queryItemById(String itemId);

    /**
     * 根据商品id查询商品图片列表
     * @param itemId
     * @return
     */
    public List<ItemsImg> queryItemImgList(String itemId);


    /**
     * 根据商品id查询商品规格列表
     * @param itemId
     * @return
     */
    public List<ItemsSpec> queryItemSpecList(String itemId);

    /**
     * 根据商品id查询商品属性
     * @param itemId
     * @return
     */
    public ItemsParam queryItemParam(String itemId);



    public CommentLevelCountsVO queryCommentCounts(String itemId);

    public PagedGridResult queryPagedComments(String itemId, Integer level, Integer page, Integer pageSize);


    public PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize);

    public PagedGridResult searchItemsByThirdCat(Integer catId, String sort, Integer page, Integer pageSize);

    public List<ShopcartVO> queryItemsBySpecIds(String specIds);

    public ItemsSpec queryItemsSpecById(String specId);


    public String queryItemMainImgById(String itemId);

    public void decreaseItemSpecStock(String specId, int buyCounts);

}
