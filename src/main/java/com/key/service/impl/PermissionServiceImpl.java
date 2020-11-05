package com.key.service.impl;

import com.key.constant.Constant;
import com.key.entity.Permission;
import com.key.exception.BusinessException;
import com.key.exception.code.BaseResponseCode;
import com.key.mapper.PermissionMapper;
import com.key.service.PermissionService;
import com.key.service.RedisService;
import com.key.service.RolePermissionService;
import com.key.service.UserRoleService;
import com.key.utils.TokenSettings;
import com.key.vo.req.PermissionAddReqVO;
import com.key.vo.req.PermissionUpdateReqVO;
import com.key.vo.resp.PermissionRespNodeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author 徐雨轩
 * @description
 * @date 2020-11-04 15:14
 */
@Service
@Slf4j
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionMapper sysPermissionMapper;
    @Autowired
    private RolePermissionService rolePermissionService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private TokenSettings tokenSettings;
    @Override
    public List<Permission> selectAll() {
        List<Permission> sysPermissions = sysPermissionMapper.selectAll();
        if(!sysPermissions.isEmpty()){
            for (Permission sysPermission : sysPermissions) {
                Permission parent = sysPermissionMapper.selectByPrimaryKey(sysPermission.getPid());
                if(parent!=null){
                    sysPermission.setPidName(parent.getName());
                }
            }
        }
        return sysPermissions;
    }

    @Override
    public List<PermissionRespNodeVO> selectAllMenuByTree() {
        List<Permission> list=sysPermissionMapper.selectAll();
        List<PermissionRespNodeVO> result=new ArrayList<>();
        PermissionRespNodeVO respNodeVO=new PermissionRespNodeVO();
        respNodeVO.setId("0");
        respNodeVO.setTitle("默认顶级菜单");
        respNodeVO.setChildren(getTree(list,true));
        result.add(respNodeVO);
        return result;
    }
    /**
     * type=true 递归遍历到菜单
     * type=false 递归遍历到按钮
     * 传入菜单数据，返回符合layui的tree插件的菜单节点数据
     * */
    private List<PermissionRespNodeVO> getTree(List<Permission> all, boolean type){

        List<PermissionRespNodeVO> list=new ArrayList<>();
        if(all==null||all.isEmpty()){
            return list;
        }

        for(Permission sysPermission:all){
            if(sysPermission.getPid().equals("0")){
                PermissionRespNodeVO respNodeVO=new PermissionRespNodeVO();
                BeanUtils.copyProperties(sysPermission,respNodeVO);
                respNodeVO.setTitle(sysPermission.getName());
                if(type){
                    respNodeVO.setChildren(getChildExBtn(sysPermission.getId(),all));
                }else {
                    respNodeVO.setChildren(getChild(sysPermission.getId(),all));
                }

                list.add(respNodeVO);
            }
        }
        return list;
    }
    /**
     * 递归遍历所有数据
     */
    private List<PermissionRespNodeVO> getChild(String id, List<Permission> all){

        List<PermissionRespNodeVO> list=new ArrayList<>();
        for (Permission s:
                all) {
            if(s.getPid().equals(id)){
                PermissionRespNodeVO respNodeVO=new PermissionRespNodeVO();
                BeanUtils.copyProperties(s,respNodeVO);
                respNodeVO.setTitle(s.getName());
                respNodeVO.setChildren(getChild(s.getId(),all));
                list.add(respNodeVO);
            }
        }
        return list;
    }
    /**
     * 只递归到菜单
     */
    private List<PermissionRespNodeVO> getChildExBtn(String id, List<Permission> all){
        List<PermissionRespNodeVO> list=new ArrayList<>();
        for (Permission s: all) {
            if(s.getPid().equals(id)&&s.getType()!=3){
                PermissionRespNodeVO respNodeVO=new PermissionRespNodeVO();
                BeanUtils.copyProperties(s,respNodeVO);
                respNodeVO.setTitle(s.getName());
                respNodeVO.setChildren(getChildExBtn(s.getId(),all));
                list.add(respNodeVO);
            }
        }
        return list;
    }

    @Override
    public Permission addPermission(PermissionAddReqVO vo) {
        Permission sysPermission=new Permission();
        BeanUtils.copyProperties(vo,sysPermission);
        verifyForm(sysPermission);
        sysPermission.setId(UUID.randomUUID().toString());
        sysPermission.setCreateTime(new Date());
        int insert = sysPermissionMapper.insertSelective(sysPermission);
        if(insert!=1){
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
        return sysPermission;
    }
    /**
     * - 操作后的菜单类型是目录的时候 父级必须为目录
     * - 操作后的菜单类型是菜单的时候，父类必须为目录类型
     * - 操作后的菜单类型是按钮的时候 父类必须为菜单类型
     */
    private void verifyForm(Permission sysPermission){

        Permission parent=sysPermissionMapper.selectByPrimaryKey(sysPermission.getPid());
        switch (sysPermission.getType()){
            case 1:
                if(parent!=null){
                    if(parent.getType()!=1){
                        throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_CATALOG_ERROR);
                    }
                }else if (!sysPermission.getPid().equals("0")){
                    throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_CATALOG_ERROR);
                }
                break;
            case 2:
                if(parent==null||parent.getType()!=1){
                    throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_MENU_ERROR);
                }
                if(StringUtils.isEmpty(sysPermission.getUrl())){
                    throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_URL_NOT_NULL);
                }
                break;
            case 3:
                if(parent==null||parent.getType()!=2){
                    throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_BTN_ERROR);
                }
                if(StringUtils.isEmpty(sysPermission.getPerms())){
                    throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_URL_PERMS_NULL);
                }
                if(StringUtils.isEmpty(sysPermission.getUrl())){
                    throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_URL_NOT_NULL);
                }
                if(StringUtils.isEmpty(sysPermission.getMethod())){
                    throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_URL_METHOD_NULL);
                }
                if(StringUtils.isEmpty(sysPermission.getCode())){
                    throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_URL_CODE_NULL);
                }
                break;
        }
    }

    @Override
    public List<PermissionRespNodeVO> permissionTreeList(String userId) {
        List<Permission> list=getPermissions(userId);
        return getTree(list,true);
    }

    @Override
    public List<PermissionRespNodeVO> selectAllTree() {
        return getTree(selectAll(),false);
    }

    @Override
    public void updatePermission(PermissionUpdateReqVO vo) {
        //校验数据
        Permission update=new Permission();
        BeanUtils.copyProperties(vo,update);
        verifyForm(update);
        Permission sysPermission = sysPermissionMapper.selectByPrimaryKey(vo.getId());
        if(sysPermission==null){
            log.info("传入的id在数据库中不存在");
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
        if(!sysPermission.getPid().equals(vo.getPid())||sysPermission.getStatus()!=vo.getStatus()){
            //所属菜单发生了变化或者权限状态发生了变化要校验该权限是否存在子集
            List<Permission> sysPermissions = sysPermissionMapper.selectChild(vo.getId());
            if(!sysPermissions.isEmpty()){
                throw new BusinessException(BaseResponseCode.OPERATION_MENU_PERMISSION_UPDATE);
            }
        }

        update.setUpdateTime(new Date());
        int i = sysPermissionMapper.updateByPrimaryKeySelective(update);
        if(i!=1){
            throw new BusinessException(BaseResponseCode.OPERATION_ERROR);
        }

        //判断授权标识符是否发生了变化(权限标识符发生了变化，或者权限状态发生了变化)
        if(!sysPermission.getPerms().equals(vo.getPerms())||sysPermission.getStatus()!=vo.getStatus()){
            List<String> roleIdsByPermissionId = rolePermissionService.getRoleIdsByPermissionId(vo.getId());
            if(!roleIdsByPermissionId.isEmpty()){
                List<String> userIdsByRoleIds = userRoleService.getUserIdsByRoleIds(roleIdsByPermissionId);
                if(!userIdsByRoleIds.isEmpty()){
                    for (String userId:
                            userIdsByRoleIds) {
                        redisService.set(Constant.JWT_REFRESH_KEY+userId,userId,tokenSettings.getAccessTokenExpireTime().toMillis(), TimeUnit.MILLISECONDS);
                        /**
                         * 清楚用户授权数据缓存
                         */
                        redisService.delete(Constant.IDENTIFY_CACHE_KEY+userId);
                    }
                }
            }

        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletedPermission(String permissionId) {
        //判断是否有子集关联
        List<Permission> sysPermissions = sysPermissionMapper.selectChild(permissionId);
        if(!sysPermissions.isEmpty()){
            throw new BusinessException(BaseResponseCode.ROLE_PERMISSION_RELATION);
        }

        //更新权限数据
        Permission sysPermission=new Permission();
        sysPermission.setUpdateTime(new Date());
        sysPermission.setDeleted(0);
        sysPermission.setId(permissionId);
        int i = sysPermissionMapper.updateByPrimaryKeySelective(sysPermission);
        if(i!=1){
            throw new BusinessException(BaseResponseCode.OPERATION_ERROR);
        }
        //判断授权标识符是否发生了变化
        List<String> roleIdsByPermissionId = rolePermissionService.getRoleIdsByPermissionId(permissionId);
        //解除相关角色和该菜单权限的关联
        rolePermissionService.removeRoleByPermissionId(permissionId);
        if(!roleIdsByPermissionId.isEmpty()){
            List<String> userIdsByRoleIds = userRoleService.getUserIdsByRoleIds(roleIdsByPermissionId);
            if(!userIdsByRoleIds.isEmpty()){
                for (String userId:
                        userIdsByRoleIds) {
                    redisService.set(Constant.JWT_REFRESH_KEY+userId,userId,tokenSettings.getAccessTokenExpireTime().toMillis(), TimeUnit.MILLISECONDS);
                    /**
                     * 清楚用户授权数据缓存
                     */
                    redisService.delete(Constant.IDENTIFY_CACHE_KEY+userId);
                }
            }
        }
    }

    @Override
    public List<String> getPermissionByUserId(String userId) {

        List<Permission> permissions = getPermissions(userId);
        if(permissions==null||permissions.isEmpty()){
            return null;
        }
        List<String> result=new ArrayList<>();
        for (Permission s:
                permissions) {
            if(!StringUtils.isEmpty(s.getPerms())){
                result.add(s.getPerms());
            }
        }
        return result;
    }

    @Override
    public List<Permission> getPermissions(String userId) {
        List<String> roleIdsByUserId = userRoleService.getRoleIdsByUserId(userId);
        if(roleIdsByUserId.isEmpty()){
            return null;
        }
        List<String> permissionIdsByRoleIds = rolePermissionService.getPermissionIdsByRoleIds(roleIdsByUserId);
        if (permissionIdsByRoleIds.isEmpty()){
            return null;
        }
        List<Permission> result=sysPermissionMapper.selectByIds(permissionIdsByRoleIds);
        return result;
    }
}
