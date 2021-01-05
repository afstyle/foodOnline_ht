package com.hh.mapper;

import com.hh.vo.CategoryVO;
import com.hh.vo.NewItemsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CategoryMapperCustom {

    public List<CategoryVO> getSubCatList(Integer rootCatId);


    public List<NewItemsVO> getSixNewItemsLazy(@Param("paramsMap") Map<String, Object> map);


}