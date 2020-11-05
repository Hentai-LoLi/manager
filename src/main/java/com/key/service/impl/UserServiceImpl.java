package com.key.service.impl;

import com.github.pagehelper.PageHelper;
import com.key.constant.Constant;
import com.key.entity.User;
import com.key.exception.BusinessException;
import com.key.exception.code.BaseResponseCode;
import com.key.mapper.UserMapper;
import com.key.service.*;
import com.key.utils.JwtTokenUtil;
import com.key.utils.PageUtil;
import com.key.utils.PasswordUtils;
import com.key.utils.TokenSettings;
import com.key.vo.req.*;
import com.key.vo.resp.LoginRespVO;
import com.key.vo.resp.PageRespVO;
import com.key.vo.resp.UserOwnRoleRespVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author 徐雨轩
 * @description
 * @date 2020-11-04 11:28
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper sysUserMapper;

    @Autowired
    private RedisService redisService;



    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private TokenSettings tokenSettings;
    @Autowired
    private PermissionService permissionService;

    @Override
    public LoginRespVO login(LoginReqVO vo) {
        //通过用户名查询用户信息
        //如果查询存在用户
        //就比较它密码是否一样
        User userInfoByName = sysUserMapper.getUserInfoByName(vo.getUsername());
        if(userInfoByName==null){
            throw new BusinessException(BaseResponseCode.ACCOUNT_ERROR);
        }
        if(userInfoByName.getStatus()==2){
            throw new BusinessException(BaseResponseCode.ACCOUNT_LOCK_TIP);
        }
        if(!PasswordUtils.matches(userInfoByName.getSalt(),vo.getPassword(),userInfoByName.getPassword())){
            throw new BusinessException(BaseResponseCode.ACCOUNT_PASSWORD_ERROR);
        }
        LoginRespVO loginRespVO=new LoginRespVO();
        loginRespVO.setPhone(userInfoByName.getPhone());
        loginRespVO.setUsername(userInfoByName.getUsername());
        loginRespVO.setId(userInfoByName.getId());
        Map<String, Object> claims=new HashMap<>();
        claims.put(Constant.ROLES_INFOS_KEY,getRoleByUserId(userInfoByName.getId()));
        claims.put(Constant.PERMISSIONS_INFOS_KEY,getPermissionByUserId(userInfoByName.getId()));
        claims.put(Constant.JWT_USER_NAME,userInfoByName.getUsername());
        String accessToken= JwtTokenUtil.getAccessToken(userInfoByName.getId(),claims);
        String refreshToken;
        if(vo.getType().equals("1")){
            refreshToken= JwtTokenUtil.getRefreshToken(userInfoByName.getId(),claims);
        }else {
            refreshToken= JwtTokenUtil.getRefreshAppToken(userInfoByName.getId(),claims);
        }
        loginRespVO.setAccessToken(accessToken);
        loginRespVO.setRefreshToken(refreshToken);
        return loginRespVO;
    }


    /**
     * 用过用户id查询拥有的角色信息
     */
    private List<String> getRoleByUserId(String userId){
        return roleService.getNamesByUserId(userId);
    }

    private List<String> getPermissionByUserId(String userId){
        return permissionService.getPermissionByUserId(userId);
    }


    @Override
    public PageRespVO<User> pageInfo(UserPageReqVO vo) {
        PageHelper.startPage(vo.getPageNum(),vo.getPageSize());
        List<User> list=sysUserMapper.selectAll(vo);
        return PageUtil.getPageVO(list);
    }

    @Override
    public void addUser(UserAddReqVO vo) {
        User sysUser=new User();
        BeanUtils.copyProperties(vo,sysUser);
        sysUser.setId(UUID.randomUUID().toString());
        sysUser.setCreateTime(new Date());
        String salt= PasswordUtils.getSalt();
        String ecdPwd= PasswordUtils.encode(vo.getPassword(),salt);
        sysUser.setSalt(salt);
        sysUser.setPassword(ecdPwd);
        int i = sysUserMapper.insertSelective(sysUser);
        if(i!=1){
            throw new BusinessException(BaseResponseCode.OPERATION_ERROR);
        }
    }

    @Override
    public UserOwnRoleRespVO getUserOwnRole(String userId) {
        UserOwnRoleRespVO respVO=new UserOwnRoleRespVO();
        respVO.setOwnRoles(userRoleService.getRoleIdsByUserId(userId));
        respVO.setAllRole(roleService.selectAll());
        return respVO;
    }

    @Override
    public void setUserOwnRole(UserOwnRoleReqVO vo) {
        userRoleService.addUserRoleInfo(vo);
        /**
         * 标记用户 要主动去刷新
         */
        redisService.set(Constant.JWT_REFRESH_KEY+vo.getUserId(),vo.getUserId(),tokenSettings.getAccessTokenExpireTime().toMillis(),TimeUnit.MILLISECONDS);
        /**
         * 清楚用户授权数据缓存
         */
        redisService.delete(Constant.IDENTIFY_CACHE_KEY+vo.getUserId());
    }

    @Override
    public String refreshToken(String refreshToken) {
        //它是否过期
        //它是否被加如了黑名
        if(!JwtTokenUtil.validateToken(refreshToken)||redisService.hasKey(Constant.JWT_REFRESH_TOKEN_BLACKLIST+refreshToken)){
            throw new BusinessException(BaseResponseCode.TOKEN_ERROR);
        }
        String userId= JwtTokenUtil.getUserId(refreshToken);
        String username= JwtTokenUtil.getUserName(refreshToken);
        log.info("userId={}",userId);
        Map<String,Object> claims=new HashMap<>();
        claims.put(Constant.ROLES_INFOS_KEY,getRoleByUserId(userId));
        claims.put(Constant.PERMISSIONS_INFOS_KEY,getPermissionByUserId(userId));
        claims.put(Constant.JWT_USER_NAME,username);
        String newAccessToken= JwtTokenUtil.getAccessToken(userId,claims);
        return newAccessToken;
    }

    @Override
    public void updateUserInfo(UserUpdateReqVO vo, String operationId) {
        User sysUser=new User();
        BeanUtils.copyProperties(vo,sysUser);
        sysUser.setUpdateTime(new Date());
        sysUser.setUpdateId(operationId);
        if(StringUtils.isEmpty(vo.getPassword())){
            sysUser.setPassword(null);
        }else {
            String salt= PasswordUtils.getSalt();
            String endPwd= PasswordUtils.encode(vo.getPassword(),salt);
            sysUser.setSalt(salt);
            sysUser.setPassword(endPwd);
        }

        int i = sysUserMapper.updateByPrimaryKeySelective(sysUser);
        if(i!=1){
            throw new BusinessException(BaseResponseCode.OPERATION_ERROR);
        }
        if(vo.getStatus()==2){
            redisService.set(Constant.ACCOUNT_LOCK_KEY+vo.getId(),vo.getId());
        }else {
            redisService.delete(Constant.ACCOUNT_LOCK_KEY+vo.getId());
        }
    }
    @Override
    public void deletedUsers(List<String> list, String operationId) {
        User sysUser=new User();
        sysUser.setUpdateId(operationId);
        sysUser.setUpdateTime(new Date());
        int i = sysUserMapper.deletedUsers(sysUser, list);
        if(i==0){
            throw new BusinessException(BaseResponseCode.OPERATION_ERROR);
        }
        for (String userId:
                list) {
            redisService.set(Constant.DELETED_USER_KEY+userId,userId,tokenSettings.getRefreshTokenExpireAppTime().toMillis(),TimeUnit.MILLISECONDS);
            /**
             * 清楚用户授权数据缓存
             */
            redisService.delete(Constant.IDENTIFY_CACHE_KEY+userId);
        }
    }



    @Override
    public User detailInfo(String userId) {
        return sysUserMapper.selectByPrimaryKey(userId);
    }

    @Override
    public void userUpdateDetailInfo(UserUpdateDetailInfoReqVO vo, String userId) {
        User sysUser=new User();
        BeanUtils.copyProperties(vo,sysUser);
        sysUser.setId(userId);
        sysUser.setUpdateTime(new Date());
        sysUser.setUpdateId(userId);
        sysUser.setSex(vo.getSex());
        sysUser.setEmail(vo.getEmail());
        sysUser.setPhone(vo.getPhone());
        int i = sysUserMapper.updateByPrimaryKeySelective(sysUser);
        if(i!=1){
            throw new BusinessException(BaseResponseCode.OPERATION_ERROR);
        }
    }

    @Override
    public void userUpdatePwd(UserUpdatePwdReqVO vo, String accessToken, String refreshToken) {
        String userId= JwtTokenUtil.getUserId(accessToken);
        //校验旧密码
        User sysUser = sysUserMapper.selectByPrimaryKey(userId);
        if(sysUser==null){
            throw new BusinessException(BaseResponseCode.TOKEN_ERROR);
        }
        if(!PasswordUtils.matches(sysUser.getSalt(),vo.getOldPwd(),sysUser.getPassword())){
            throw new BusinessException(BaseResponseCode.OLD_PASSWORD_ERROR);
        }
        //保存新密码
        sysUser.setUpdateTime(new Date());
        sysUser.setUpdateId(userId);
        sysUser.setPassword(PasswordUtils.encode(vo.getNewPwd(),sysUser.getSalt()));
        int i = sysUserMapper.updateByPrimaryKeySelective(sysUser);
        if(i!=1){
            throw new BusinessException(BaseResponseCode.OPERATION_ERROR);
        }

        /**
         * 把token 加入黑名单 禁止再访问我们的系统资源
         */
        redisService.set(Constant.JWT_ACCESS_TOKEN_BLACKLIST+accessToken,userId, JwtTokenUtil.getRemainingTime(accessToken), TimeUnit.MILLISECONDS);
        /**
         * 把 refreshToken 加入黑名单 禁止再拿来刷新token
         */
        redisService.set(Constant.JWT_REFRESH_TOKEN_BLACKLIST+refreshToken,userId, JwtTokenUtil.getRemainingTime(refreshToken),TimeUnit.MILLISECONDS);
        /**
         * 清楚用户授权数据缓存
         */
        redisService.delete(Constant.IDENTIFY_CACHE_KEY+userId);
    }

    @Override
    public void logout(String accessToken, String refreshToken) {
        if(StringUtils.isEmpty(accessToken)|| StringUtils.isEmpty(refreshToken)){
            throw new BusinessException(BaseResponseCode.DATA_ERROR);
        }
        Subject subject = SecurityUtils.getSubject();
        if(subject!=null){
            subject.logout();
        }
        String userId= JwtTokenUtil.getUserId(accessToken);
        /**
         * 把accessToken 加入黑名单
         */
        redisService.set(Constant.JWT_ACCESS_TOKEN_BLACKLIST+accessToken,userId, JwtTokenUtil.getRemainingTime(accessToken),TimeUnit.MILLISECONDS);

        /**
         * 把refreshToken 加入黑名单
         */
        redisService.set(Constant.JWT_REFRESH_IDENTIFICATION+refreshToken,userId, JwtTokenUtil.getRemainingTime(refreshToken),TimeUnit.MILLISECONDS);
    }
}
