package com.key.service;

import com.key.vo.req.RolePermissionOperationReqVO;

import java.util.List;

/**
 * @author 徐雨轩
 * @description
 * @date 2020-11-04 11:28
 */
public interface RolePermissionService {

    void addRolePermission(RolePermissionOperationReqVO vo);
    List<String> getRoleIdsByPermissionId(String permissionId);
    int removeRoleByPermissionId(String permissionId);
    List<String> getPermissionIdsByRoleId(String roleId);
    int removeByRoleId(String roleId);
    List<String> getPermissionIdsByRoleIds(List<String> roleIds);
}
