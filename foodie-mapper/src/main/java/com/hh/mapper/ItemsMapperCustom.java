package com.hh.mapper;

import com.hh.bo.ShopcartBO;
import com.hh.vo.ItemCommentVO;
import com.hh.vo.SearchItemsVO;
import com.hh.vo.ShopcartVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsMapperCustom {



    public List<ItemCommentVO> queryItemsComments(@Param("map") Map<String, Object> map);

    public List<SearchItemsVO> searchItems(@Param("map") Map<String, Object> map);

    public List<SearchItemsVO> searchItemsByThirdCat(@Param("map") Map<String, Object> map);


    public List<ShopcartVO> queryItemsBySpecIds(@Param("list") List specIdsList);

    public int decreaseItemSpecStock(@Param("specId") String specId,
                                     @Param("pendingCounts") int pendingCounts);




}