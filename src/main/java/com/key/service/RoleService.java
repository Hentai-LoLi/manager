package com.key.service;

import com.key.entity.Role;
import com.key.vo.req.AddRoleReqVO;
import com.key.vo.req.RolePageReqVO;
import com.key.vo.req.RoleUpdateReqVO;
import com.key.vo.resp.PageRespVO;

import java.util.List;

/**
 * @author 徐雨轩
 * @description
 * @date 2020-11-04 15:51
 */
public interface RoleService {

    PageRespVO<Role> pageInfo(RolePageReqVO vo);
    Role addRole(AddRoleReqVO vo);
    List<Role> selectAll();
    Role detailInfo(String id);
    void updateRole(RoleUpdateReqVO vo);
    void deletedRole(String roleId);

    List<String> getNamesByUserId(String userId);
}
