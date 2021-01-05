package com.hh.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author HuangHao
 * @date 2020/10/12 0:10
 */
@ApiIgnore
@RestController
public class HelloController {

    private static final Logger LOG = LoggerFactory.getLogger(HelloController.class);
    

    @GetMapping("/hello")
    public Object hello() {

        LOG.debug("debug: hello");
        LOG.info("info: hello");
        LOG.warn("warn: hello");
        LOG.error("error: hello");


        return "Hello World~";
    }


}
