package com.hh.controller;

import com.hh.bo.ShopcartBO;
import com.hh.bo.UserBO;
import com.hh.pojo.Users;
import com.hh.service.UserService;
import com.hh.utils.CookieUtils;
import com.hh.utils.JsonUtils;
import com.hh.utils.RedisOperator;
import com.hh.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author HuangHao
 * @date 2020/11/5 22:54
 */
@Api(value = "注册登录", tags = {"用于注册登录的相关接口"})
@RestController
@RequestMapping("passport")
public class PassportController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisOperator redisOperator;


    @ApiOperation(value = "用户名是否已存在", notes = "用户名是否已存在", httpMethod = "GET")
    @GetMapping("/usernameIsExist")
    public Result usernameIsExist(@RequestParam String username) {

        if (StringUtils.isBlank(username)) {
            return Result.errorMsg("username cant null");
        }

        boolean isExist = userService.queryUsernameIsExist(username);
        if (isExist) {
            return Result.errorMsg("username is existed");
        }

        return Result.ok();
    }


    @ApiOperation(value = "用户注册", notes = "用户注册", httpMethod = "POST")
    @PostMapping("/regist")
    public Result regist(@RequestBody UserBO userBO,
                         HttpServletRequest request, HttpServletResponse response) {

        String username = userBO.getUsername();
        String password = userBO.getPassword();
        String confirmPassword = userBO.getConfirmPassword();

        if (StringUtils.isBlank(username) || StringUtils.isBlank(password) || StringUtils.isBlank(confirmPassword)) {
            return Result.errorMsg("params error!");
        }


        boolean isExist = userService.queryUsernameIsExist(username);
        if (isExist) {
            return Result.errorMsg("username is existed");
        }

        if (password.length() < 6) {
            return Result.errorMsg("password's length should more than 6 bite");
        }

        if (!password.equals(confirmPassword)) {
            return Result.errorMsg("Two passwords are different");
        }


        Users users = userService.createUser(userBO);

        setNullProperty(users);

        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(users), -1, true);

        // TODO 生成用户token，存入redis会话
        // 同步购物车数据
        syncShopcartData(users.getId(), request, response);

        return Result.ok(users);
    }


    @ApiOperation(value = "用户登录", notes = "用户登录", httpMethod = "POST")
    @PostMapping("/login")
    public Result login(@RequestBody UserBO userBO,
                        HttpServletRequest request, HttpServletResponse response) {

        String username = userBO.getUsername();
        String password = userBO.getPassword();

        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return Result.errorMsg("用户名或密码不能空");
        }

        // 实现登录
        Users users = userService.queryUserForLogin(username, password);

        if (users == null) {
            return Result.errorMsg("用户名或密码错误");
        }

        setNullProperty(users);

        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(users), -1, true);

        // TODO 生成用户token，存入redis会话
        // 同步购物车数据
        syncShopcartData(users.getId(), request, response);

        return Result.ok(users);
    }

    private void syncShopcartData(String userId, HttpServletRequest request, HttpServletResponse response) {

        String redisKeyName = SHOPCART + ":" + userId;

        String shopcartRedis = redisOperator.get(redisKeyName);
        String shopcartCookie = CookieUtils.getCookieValue(request, SHOPCART, true);
        if (StringUtils.isBlank(shopcartRedis)) {
            if (StringUtils.isNotBlank(shopcartCookie)) {
                redisOperator.set(redisKeyName, shopcartCookie);
            }
        } else {
            if (StringUtils.isBlank(shopcartCookie)) {
                CookieUtils.setCookie(request, response, SHOPCART, shopcartRedis, true);
            } else {
                List<ShopcartBO> redisList = JsonUtils.jsonToList(shopcartRedis, ShopcartBO.class);
                List<ShopcartBO> cookieList = JsonUtils.jsonToList(shopcartCookie, ShopcartBO.class);
                List<ShopcartBO> pengdingDeleteList = new ArrayList<>();
                for (ShopcartBO redisItem : redisList) {
                    for (ShopcartBO cookieItem : cookieList) {
                        if (redisItem.getSpecId().equals(cookieItem.getSpecId())) {
                            redisItem.setBuyCounts(cookieItem.getBuyCounts());
                            pengdingDeleteList.add(cookieItem);
                        }
                    }
                }
                cookieList.removeAll(pengdingDeleteList);
                redisList.addAll(cookieList);

                redisOperator.set(redisKeyName, JsonUtils.objectToJson(redisList));
                CookieUtils.setCookie(request, response, SHOPCART, JsonUtils.objectToJson(redisList), true);
            }
        }

    }


    /**
     * 敏感信息设空
     * @param users 原user
     */
    private void setNullProperty(Users users) {
        users.setPassword(null);
        users.setMobile(null);
        users.setEmail(null);
        users.setCreatedTime(null);
        users.setUpdatedTime(null);
        users.setBirthday(null);
    }

    @ApiOperation(value = "用户退出登录", notes = "用户退出登录", httpMethod = "POST")
    @PostMapping("/logout")
    public Result logout(@RequestParam String userId,
                         HttpServletRequest request, HttpServletResponse response) {

        // 清除用户相关信息的cookie
        CookieUtils.deleteCookie(request, response, "user");

        CookieUtils.deleteCookie(request, response, SHOPCART);

        return Result.ok();
    }

}
