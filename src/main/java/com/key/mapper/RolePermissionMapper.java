package com.key.mapper;

import com.key.entity.RolePermission;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePermissionMapper {
    int deleteByPrimaryKey(String id);

    int insert(RolePermission record);

    int insertSelective(RolePermission record);

    RolePermission selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(RolePermission record);

    int updateByPrimaryKey(RolePermission record);

    int batchInsertRolePermission(List<RolePermission> list);

    List<String> getRoleIdsByPermissionId(String permissionId);

    //根据permissionId 删除角色和菜单权限关联表相关数据
    int removeByPermissionId(String permissionId);

    //根据角色id获取该角色关联的菜单权限id集合
    List<String> getPermissionIdsByRoleId(String roleId);




    //根绝角色id删除角色和菜单权限关联表相关数据
    int removeByRoleId(String roleId);


    List<String> getPermissionIdsByRoleIds(List<String> roleIds);
}