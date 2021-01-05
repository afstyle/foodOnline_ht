package com.hh.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author HuangHao
 * @date 2020/11/11 21:52
 */
@ApiModel(value = "用户对象BO", description = "前端封装传入的对象")
public class UserBO {

    @ApiModelProperty(value = "用户名", name = "username", example = "ame", required = true)
    private String username;
    @ApiModelProperty(value = "密码", name = "password", example = "123123", required = true)
    private String password;
    @ApiModelProperty(value = "确认密码", name = "confirmPassword", example = "123123")
    private String confirmPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
