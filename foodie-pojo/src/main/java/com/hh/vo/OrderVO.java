package com.hh.vo;


import com.hh.bo.ShopcartBO;

import java.util.List;

public class OrderVO {


    private String orderId;
    private MerchantOrdersVO merchantOrdersVO;
    private List<ShopcartBO> toRemovedShopcartList;

    public List<ShopcartBO> getToRemovedShopcartList() {
        return toRemovedShopcartList;
    }

    public void setToRemovedShopcartList(List<ShopcartBO> toRemovedShopcartList) {
        this.toRemovedShopcartList = toRemovedShopcartList;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public MerchantOrdersVO getMerchantOrdersVO() {
        return merchantOrdersVO;
    }

    public void setMerchantOrdersVO(MerchantOrdersVO merchantOrdersVO) {
        this.merchantOrdersVO = merchantOrdersVO;
    }
}
