package com.key.service;

import com.key.vo.req.UserOwnRoleReqVO;

import java.util.List;

/**
 * @author 徐雨轩
 * @description
 * @date 2020-11-04 11:25
 */
public interface UserRoleService {

    List<String> getRoleIdsByUserId(String userId);
    void addUserRoleInfo(UserOwnRoleReqVO vo);
    List<String> getUserIdsByRoleIds(List<String> roleIds);
    List<String> getUserIdsBtRoleId(String roleId);
    int removeUserRoleId(String roleId);
}
