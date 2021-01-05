package com.hh.service;

import com.hh.pojo.Carousel;

import java.util.List;

/**
 * @author HuangHao
 * @date 2020/11/17 20:48
 */
public interface CarouselService {


    /**
     * 查询所有轮播图列表
     * @param isShow
     * @return
     */
    public List<Carousel> queryAll(Integer isShow);




}
