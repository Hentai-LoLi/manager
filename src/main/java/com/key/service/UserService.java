package com.key.service;

import com.key.entity.User;
import com.key.vo.req.*;
import com.key.vo.resp.LoginRespVO;
import com.key.vo.resp.PageRespVO;
import com.key.vo.resp.UserOwnRoleRespVO;

import java.util.List;

/**
 * @author 徐雨轩
 * @description
 * @date 2020-11-04 11:27
 */
public interface UserService {

    LoginRespVO login(LoginReqVO vo);


    PageRespVO<User> pageInfo(UserPageReqVO vo);

    void addUser(UserAddReqVO vo);

    UserOwnRoleRespVO getUserOwnRole(String userId);

    void setUserOwnRole(UserOwnRoleReqVO vo);

    String refreshToken(String refreshToken);

    void updateUserInfo(UserUpdateReqVO vo, String operationId);

    void deletedUsers(List<String> list, String operationId);


    User detailInfo(String userId);

    //个人用户编辑信息接口
    void userUpdateDetailInfo(UserUpdateDetailInfoReqVO vo, String userId);

    void userUpdatePwd(UserUpdatePwdReqVO vo, String accessToken, String refreshToken);

    void logout(String accessToken, String refreshToken);

}
