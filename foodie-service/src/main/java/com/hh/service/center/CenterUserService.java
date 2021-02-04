package com.hh.service.center;

import com.hh.bo.center.CenterUserBO;
import com.hh.pojo.Users;

/**
 * @author HuangHao
 * @date 2020/11/5 22:44
 */
public interface CenterUserService {


    public Users queryUserInfo(String userId);
    public Users updateUserInfo(String userId, CenterUserBO centerUserBO);
    public Users updateUserFace(String userId, String faceUrl);



}
