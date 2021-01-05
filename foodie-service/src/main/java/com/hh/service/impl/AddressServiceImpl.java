package com.hh.service.impl;

import com.hh.bo.AddressBO;
import com.hh.enums.YesOrNo;
import com.hh.mapper.UserAddressMapper;
import com.hh.pojo.UserAddress;
import com.hh.service.AddressService;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * @author HuangHao
 * @date 2020/11/17 20:50
 */
@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public UserAddress queryUserAddress(String userId, String addressId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        userAddress.setId(addressId);

        return userAddressMapper.selectOne(userAddress);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<UserAddress> queryAll(String userId) {

        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);

        return userAddressMapper.select(userAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void addNewUserAddress(AddressBO addressBO) {
        int isDefault = 0; // 是否默认地址
        List<UserAddress> userAddressList = this.queryAll(addressBO.getUserId());
        if (CollectionUtils.isEmpty(userAddressList)) {
            isDefault = 1;
        }

        String addressId = sid.nextShort();

        UserAddress userAddress = new UserAddress();
        BeanUtils.copyProperties(addressBO, userAddress);
        userAddress.setId(addressId);
        userAddress.setIsDefault(isDefault);
        userAddress.setCreatedTime(new Date());
        userAddress.setUpdatedTime(new Date());

        userAddressMapper.insert(userAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUserAddress(AddressBO addressBO) {

        String addressId = addressBO.getAddressId();

        UserAddress userAddress = new UserAddress();
        BeanUtils.copyProperties(addressBO, userAddress);
        userAddress.setId(addressId);
        userAddress.setUpdatedTime(new Date());

        userAddressMapper.updateByPrimaryKeySelective(userAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteUserAddress(String userId, String addressId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setId(addressId);
        userAddress.setUserId(userId);

        userAddressMapper.delete(userAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void setDefalut(String userId, String addressId) {

        UserAddress queryUserAddress = new UserAddress();
        queryUserAddress.setUserId(userId);
        queryUserAddress.setIsDefault(YesOrNo.YES.type);

        List<UserAddress> userAddressList = userAddressMapper.select(queryUserAddress);
        for (UserAddress address : userAddressList) {
            address.setIsDefault(YesOrNo.NO.type);
            userAddressMapper.updateByPrimaryKeySelective(address);
        }


        UserAddress userAddressNew = new UserAddress();
        userAddressNew.setId(addressId);
        userAddressNew.setUserId(userId);
        userAddressNew.setIsDefault(YesOrNo.YES.type);
        userAddressMapper.updateByPrimaryKeySelective(userAddressNew);
    }



}
