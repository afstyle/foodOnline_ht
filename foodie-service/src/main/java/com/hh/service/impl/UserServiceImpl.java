package com.hh.service.impl;

import com.hh.bo.UserBO;
import com.hh.enums.Sex;
import com.hh.mapper.UsersMapper;
import com.hh.pojo.Users;
import com.hh.service.UserService;
import com.hh.utils.DateUtil;
import com.hh.utils.MD5Utils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

/**
 * @author HuangHao
 * @date 2020/11/5 22:46
 */
@Service
public class  UserServiceImpl implements UserService {

    @Autowired
    public UsersMapper usersMapper;

    @Autowired
    private Sid sid;

    private static final String DEFAULT_USER_FACE_URL = "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2057588226,2402156864&fm=11&gp=0.jpg";


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUsernameIsExist(String username) {

        Example userExample = new Example(Users.class);
        Example.Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("username", username);

        Users result = usersMapper.selectOneByExample(userExample);

        return result != null;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users createUser(UserBO userBO) {

        String userId = sid.nextShort();

        Users user = new Users();
        user.setId(userId);
        user.setUsername(userBO.getUsername());
        try {
            user.setPassword(MD5Utils.getMD5Str(userBO.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        user.setNickname(userBO.getUsername());
        user.setFace(DEFAULT_USER_FACE_URL);
        user.setBirthday(DateUtil.stringToDate("1900-01-01"));
        user.setSex(Sex.secret.type);
        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());

        usersMapper.insert(user);

        return user;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserForLogin(String username, String password) {
        Example userExample = new Example(Users.class);
        Example.Criteria criteria = userExample.createCriteria();
        criteria.andEqualTo("username", username);
        try {
            criteria.andEqualTo("password", MD5Utils.getMD5Str(password));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return usersMapper.selectOneByExample(userExample);
    }

}
