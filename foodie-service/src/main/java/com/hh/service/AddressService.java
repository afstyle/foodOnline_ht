package com.hh.service;

import com.hh.bo.AddressBO;
import com.hh.pojo.UserAddress;

import java.util.List;

/**
 * @author HuangHao
 * @date 2020/11/17 20:48
 */
public interface AddressService {

    public List<UserAddress> queryAll(String userId);
    public void addNewUserAddress(AddressBO addressBO);
    public void updateUserAddress(AddressBO addressBO);
    public void deleteUserAddress(String userId, String addressId);
    public void setDefalut(String userId, String addressId);
    public UserAddress queryUserAddress(String userId, String addressId);



}
