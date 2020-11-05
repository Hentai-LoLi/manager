package com.key.service;

import com.key.entity.Permission;
import com.key.vo.req.PermissionAddReqVO;
import com.key.vo.req.PermissionUpdateReqVO;
import com.key.vo.resp.PermissionRespNodeVO;

import java.util.List;

/**
 * @author 徐雨轩
 * @description
 * @date 2020-11-04 15:13
 */

public interface PermissionService {

    List<Permission> selectAll();
    List<PermissionRespNodeVO> selectAllMenuByTree();
    Permission addPermission(PermissionAddReqVO vo);
    List<PermissionRespNodeVO> permissionTreeList(String userId);
    List<PermissionRespNodeVO> selectAllTree();
    void updatePermission(PermissionUpdateReqVO vo);
    void deletedPermission(String permissionId);
    List<String> getPermissionByUserId(String userId);
    List<Permission> getPermissions(String userId);
}
