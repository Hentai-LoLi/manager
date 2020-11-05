package com.key.service.impl;

import com.github.pagehelper.PageHelper;
import com.key.constant.Constant;
import com.key.entity.Role;
import com.key.exception.BusinessException;
import com.key.exception.code.BaseResponseCode;
import com.key.mapper.RoleMapper;
import com.key.service.*;
import com.key.utils.PageUtil;
import com.key.utils.TokenSettings;
import com.key.vo.req.AddRoleReqVO;
import com.key.vo.req.RolePageReqVO;
import com.key.vo.req.RolePermissionOperationReqVO;
import com.key.vo.req.RoleUpdateReqVO;
import com.key.vo.resp.PageRespVO;
import com.key.vo.resp.PermissionRespNodeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author 徐雨轩
 * @description
 * @date 2020-11-04 15:52
 */
@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper sysRoleMapper;

    @Autowired
    private RolePermissionService rolePermissionService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private TokenSettings tokenSettings;


    @Override
    public PageRespVO<Role> pageInfo(RolePageReqVO vo) {
        PageHelper.startPage(vo.getPageNum(),vo.getPageSize());
        List<Role> sysRoles =sysRoleMapper.selectAll(vo);
        return PageUtil.getPageVO(sysRoles);
    }

    @Override
    @Transactional(rollbackFor =Exception.class)
    public Role addRole(AddRoleReqVO vo) {
        Role sysRole=new Role();
        BeanUtils.copyProperties(vo,sysRole);
        sysRole.setId(UUID.randomUUID().toString());
        sysRole.setCreateTime(new Date());
        int i = sysRoleMapper.insertSelective(sysRole);
        if(i!=1){
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
        if(vo.getPermissions()!=null&&!vo.getPermissions().isEmpty()){
            RolePermissionOperationReqVO operationReqVO=new RolePermissionOperationReqVO();
            operationReqVO.setRoleId(sysRole.getId());
            operationReqVO.setPermissionIds(vo.getPermissions());
            rolePermissionService.addRolePermission(operationReqVO);
        }
        return sysRole;
    }

    @Override
    public List<Role> selectAll() {
        return sysRoleMapper.selectAll(new RolePageReqVO());
    }

    @Override
    public Role detailInfo(String id) {
        //通过id获取角色信息
        Role sysRole = sysRoleMapper.selectByPrimaryKey(id);
        if(sysRole==null){
            log.error("传入 的 id:{}不合法",id);
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
        //获取所有权限菜单权限树
        List<PermissionRespNodeVO> permissionRespNodeVOS = permissionService.selectAllTree();
        //获取该角色拥有的菜单权限
        List<String> permissionIdsByRoleId = rolePermissionService.getPermissionIdsByRoleId(id);
        Set<String> checkList=new HashSet<>(permissionIdsByRoleId);
        //遍历菜单权限树的数据
        setChecked(permissionRespNodeVOS,checkList);
        sysRole.setPermissionRespNode(permissionRespNodeVOS);
        return sysRole;
    }
    private void setChecked(List<PermissionRespNodeVO> list, Set<String> checkList){

        for(PermissionRespNodeVO node:list){
            /**
             * 子集选中从它往上到跟目录都被选中，父级选中从它到它所有的叶子节点都会被选中
             * 这样我们就直接遍历最底层及就可以了
             */
            if(checkList.contains(node.getId())&&(node.getChildren()==null||node.getChildren().isEmpty())){
                node.setChecked(true);
            }
            setChecked((List<PermissionRespNodeVO>) node.getChildren(),checkList);

        }
    }

    @Override
    public void updateRole(RoleUpdateReqVO vo) {
        //保存角色基本信息
        Role sysRole=sysRoleMapper.selectByPrimaryKey(vo.getId());
        if (null==sysRole){
            log.error("传入 的 id:{}不合法",vo.getId());
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
        BeanUtils.copyProperties(vo,sysRole);
        sysRole.setStatus(vo.getStatus());
        sysRole.setUpdateTime(new Date());
        int count=sysRoleMapper.updateByPrimaryKeySelective(sysRole);
        if(count!=1){
            throw new BusinessException(BaseResponseCode.OPERATION_ERROR);
        }
        //修改该角色和菜单权限关联数据
        RolePermissionOperationReqVO reqVO=new RolePermissionOperationReqVO();
        reqVO.setRoleId(vo.getId());
        reqVO.setPermissionIds(vo.getPermissions());
        rolePermissionService.addRolePermission(reqVO);
        //标记关联用户
        List<String> userIdsBtRoleId = userRoleService.getUserIdsBtRoleId(vo.getId());
        if(!userIdsBtRoleId.isEmpty()){
            for (String userId:
                    userIdsBtRoleId) {
                /**
                 * 标记用户 在用户认证的时候判断这个是否主动刷过
                 */
                redisService.set(Constant.JWT_REFRESH_KEY+userId,userId,tokenSettings.getAccessTokenExpireTime().toMillis(), TimeUnit.MILLISECONDS);

                /**
                 * 清楚用户授权数据缓存
                 */
                redisService.delete(Constant.IDENTIFY_CACHE_KEY+userId);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletedRole(String roleId) {
        //就更新删除的角色数据
        Role sysRole=new Role();
        sysRole.setId(roleId);
        sysRole.setDeleted(0);
        sysRole.setUpdateTime(new Date());
        int i = sysRoleMapper.updateByPrimaryKeySelective(sysRole);
        if(i!=1){
            throw new BusinessException(BaseResponseCode.OPERATION_ERROR);
        }
        //角色菜单权限关联数据删除
        rolePermissionService.removeByRoleId(roleId);
        List<String> userIdsBtRoleId = userRoleService.getUserIdsBtRoleId(roleId);
        //角色用户关联数据删除
        userRoleService.removeUserRoleId(roleId);
        //把跟该角色关联的用户标记起来，需要刷新token
        if(!userIdsBtRoleId.isEmpty()){
            for (String userId:
                    userIdsBtRoleId) {
                /**
                 * 标记用户 在用户认证的时候判断这个是否主动刷过
                 */
                redisService.set(Constant.JWT_REFRESH_KEY+userId,userId,tokenSettings.getAccessTokenExpireTime().toMillis(), TimeUnit.MILLISECONDS);
                /**
                 * 清楚用户授权数据缓存
                 */
                redisService.delete(Constant.IDENTIFY_CACHE_KEY+userId);
            }
        }
    }

    @Override
    public List<String> getNamesByUserId(String userId) {
        List<String> roleIdsByUserId = userRoleService.getRoleIdsByUserId(userId);
        if(roleIdsByUserId.isEmpty()){
            return null;
        }
        return sysRoleMapper.selectNamesByIds(roleIdsByUserId);
    }

}
