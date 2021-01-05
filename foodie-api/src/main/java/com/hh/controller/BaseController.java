package com.hh.controller;

import org.springframework.stereotype.Controller;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author HuangHao
 * @date 2020/10/12 0:10
 */
@ApiIgnore
@Controller
public class BaseController {

    public static final String SHOPCART = "shopcart";


    public static final Integer COMMENT_PAGE_SIZE = 10;
    public static final Integer PAGE_SIZE = 20;


}
