package com.hh.vo;

import com.hh.pojo.Items;
import com.hh.pojo.ItemsImg;
import com.hh.pojo.ItemsParam;
import com.hh.pojo.ItemsSpec;

import java.util.List;

/**
 * 商品详情VO
 * @date 2020/11/23 21:12
 */
public class CommentLevelCountsVO {


    private Integer totalCounts;
    private Integer goodCounts;
    private Integer normalCounts;
    private Integer badCounts;

    public Integer getTotalCounts() {
        return totalCounts;
    }

    public void setTotalCounts(Integer totalCounts) {
        this.totalCounts = totalCounts;
    }

    public Integer getGoodCounts() {
        return goodCounts;
    }

    public void setGoodCounts(Integer goodCounts) {
        this.goodCounts = goodCounts;
    }

    public Integer getNormalCounts() {
        return normalCounts;
    }

    public void setNormalCounts(Integer normalCounts) {
        this.normalCounts = normalCounts;
    }

    public Integer getBadCounts() {
        return badCounts;
    }

    public void setBadCounts(Integer badCounts) {
        this.badCounts = badCounts;
    }
}
