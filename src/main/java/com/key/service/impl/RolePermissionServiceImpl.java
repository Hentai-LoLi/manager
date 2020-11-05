package com.key.service.impl;

import com.key.entity.RolePermission;
import com.key.exception.BusinessException;
import com.key.exception.code.BaseResponseCode;
import com.key.mapper.RolePermissionMapper;
import com.key.service.RolePermissionService;
import com.key.vo.req.RolePermissionOperationReqVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author 徐雨轩
 * @description
 * @date 2020-11-04 11:28
 */
@Service
@Slf4j
public class RolePermissionServiceImpl implements RolePermissionService {

    @Autowired
    private RolePermissionMapper sysRolePermissionMapper;
    @Override
    public void addRolePermission(RolePermissionOperationReqVO vo) {
        sysRolePermissionMapper.removeByRoleId(vo.getRoleId());
        if(vo.getPermissionIds()==null||vo.getPermissionIds().isEmpty()){
            return;
        }
        List<RolePermission> list=new ArrayList<>();
        for (String permissionId:
                vo.getPermissionIds()) {
            RolePermission sysRolePermission=new RolePermission();
            sysRolePermission.setId(UUID.randomUUID().toString());
            sysRolePermission.setCreateTime(new Date());
            sysRolePermission.setRoleId(vo.getRoleId());
            sysRolePermission.setPermissionId(permissionId);
            list.add(sysRolePermission);
        }
        int i = sysRolePermissionMapper.batchInsertRolePermission(list);
        if(i==0){
            throw new BusinessException(BaseResponseCode.OPERATION_ERROR);
        }
    }

    @Override
    public List<String> getRoleIdsByPermissionId(String permissionId) {
        return sysRolePermissionMapper.getRoleIdsByPermissionId(permissionId);
    }

    @Override
    public int removeRoleByPermissionId(String permissionId) {
        return sysRolePermissionMapper.removeByPermissionId(permissionId);
    }

    @Override
    public List<String> getPermissionIdsByRoleId(String roleId) {
        return sysRolePermissionMapper.getPermissionIdsByRoleId(roleId);
    }

    @Override
    public int removeByRoleId(String roleId) {
        return sysRolePermissionMapper.removeByRoleId(roleId);
    }
    @Override
    public List<String> getPermissionIdsByRoleIds(List<String> roleIds) {

        return sysRolePermissionMapper.getPermissionIdsByRoleIds(roleIds);
    }
}
