package com.key.service.impl;

import com.key.entity.UserRole;
import com.key.exception.BusinessException;
import com.key.exception.code.BaseResponseCode;
import com.key.mapper.UserRoleMapper;
import com.key.service.UserRoleService;
import com.key.vo.req.UserOwnRoleReqVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author 徐雨轩
 * @description
 * @date 2020-11-04 11:26
 */
@Service
@Slf4j
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleMapper sysUserRoleMapper;
    @Override
    public List<String> getRoleIdsByUserId(String userId) {
        return sysUserRoleMapper.getRoleIdsByUserId(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUserRoleInfo(UserOwnRoleReqVO vo) {
        //删除他们关联数据
        sysUserRoleMapper.removeRoleByUserId(vo.getUserId());
        if(vo.getRoleIds()==null||vo.getRoleIds().isEmpty()){
            return;
        }
        List<UserRole> list=new ArrayList<>();
        for (String roleId:
                vo.getRoleIds()) {
            UserRole sysUserRole=new UserRole();
            sysUserRole.setId(UUID.randomUUID().toString());
            sysUserRole.setCreateTime(new Date());
            sysUserRole.setUserId(vo.getUserId());
            sysUserRole.setRoleId(roleId);
            list.add(sysUserRole);
        }
        int i = sysUserRoleMapper.batchInsertUserRole(list);
        if(i==0){
            throw new BusinessException(BaseResponseCode.OPERATION_ERROR);
        }
    }

    @Override
    public List<String> getUserIdsByRoleIds(List<String> roleIds) {
        return sysUserRoleMapper.getUserIdsByRoleIds(roleIds);
    }

    @Override
    public List<String> getUserIdsBtRoleId(String roleId) {
        return sysUserRoleMapper.getUserIdsByRoleId(roleId);
    }

    @Override
    public int removeUserRoleId(String roleId) {
        return sysUserRoleMapper.removeUserRoleId(roleId);
    }
}
