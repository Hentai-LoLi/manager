package com.key.controller;

import com.key.aop.annotation.MyLog;
import com.key.entity.Role;
import com.key.service.RoleService;
import com.key.utils.DataResult;
import com.key.vo.req.AddRoleReqVO;
import com.key.vo.req.RolePageReqVO;
import com.key.vo.req.RoleUpdateReqVO;
import com.key.vo.resp.PageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author 徐雨轩
 * @description
 * @date 2020-11-03 17:21
 */
@RestController
@RequestMapping("/api")
@Api(tags = "组织管理-角色管理",description = "角色管理相关接口")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @PostMapping("/roles")
    @ApiOperation(value = "分页获取角色数据接口")
    @RequiresPermissions("sys:role:list")
    @MyLog(title = "组织管理-角色管理",action = "分页获取角色数据接口")
    public DataResult<PageVO<Role>> pageInfo(@RequestBody RolePageReqVO vo){
        DataResult result = DataResult.success();
        result.setData(roleService.pageInfo(vo));
        return result;
    }

    @PostMapping("/role")
    @ApiOperation(value = "新增角色接口")
    @RequiresPermissions("sys:role:add")
    @MyLog(title = "组织管理-角色管理",action = "新增角色接口")
    public DataResult<Role> addRole(@RequestBody @Valid AddRoleReqVO vo){
        DataResult result = DataResult.success();
        result.setData(roleService.addRole(vo));
        return result;
    }

    @GetMapping("/role/{id}")
    @ApiOperation(value = "获取角色详情接口")
    @RequiresPermissions("sys:role:detail")
    @MyLog(title = "组织管理-角色管理",action = "获取角色详情接口")
    public DataResult<Role> detailInfo(@PathVariable("id") String id){
        DataResult result= DataResult.success();
        result.setData(roleService.detailInfo(id));
        return result;
    }

    @PutMapping("/role")
    @ApiOperation(value = "更新角色信息接口")
    @RequiresPermissions("sys:role:update")
    @MyLog(title = "组织管理-角色管理",action = "更新角色信息接口")
    public DataResult updateRole(@RequestBody @Valid RoleUpdateReqVO vo){
        DataResult result= DataResult.success();
        roleService.updateRole(vo);
        return result;
    }

    @DeleteMapping("/role/{id}")
    @ApiOperation(value = "删除角色接口")
    @RequiresPermissions("sys:role:deleted")
    @MyLog(title = "组织管理-角色管理",action = "删除角色接口")
    public DataResult deletedRole(@PathVariable("id") String id){
        roleService.deletedRole(id);
        DataResult result= DataResult.success();
        return result;
    }
}
