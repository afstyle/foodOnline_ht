package com.hh.config;

import com.hh.service.OrderService;
import com.hh.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author HuangHao
 * @date 2021/1/8 14:16
 */
@Component
public class OrderJob {

    @Autowired
    private OrderService orderService;

    @Scheduled(cron = "0 0 0/1 * * ? ")
    public void autoCloseOrder() {
        System.out.println("定时任务：now = " + DateUtil.getCurrentDateString(DateUtil.DATETIME_PATTERN));
        orderService.closeOrder();
    }


}
