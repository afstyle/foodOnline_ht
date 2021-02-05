package com.hh.mapper;

import com.hh.vo.MyCommentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsCommentsMapperCustom {

    public void saveComments(Map<String, Object> map);

    public List<MyCommentVO> queryMyComments(@Param("map") Map<String, Object> map);

}