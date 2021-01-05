package com.hh.service;

import com.hh.bo.UserBO;
import com.hh.pojo.Users;

/**
 * @author HuangHao
 * @date 2020/11/5 22:44
 */
public interface UserService {


    /**
     * 判断用户名是否存在
     */
    public boolean queryUsernameIsExist(String username);


    /**
     * 创建新用户
     */
    public Users createUser(UserBO userBO);


    /**
     * 检索用户名密码是否匹配，用于登录
     * @param username  username
     * @param password  password
     * @return
     */
    public Users queryUserForLogin(String username, String password);


}
