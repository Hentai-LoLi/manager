package com.key.mapper;

import com.key.entity.User;
import com.key.vo.req.UserPageReqVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    int deleteByPrimaryKey(String id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User getUserInfoByName(String username);

    List<User> selectAll(UserPageReqVO vo);

    int deletedUsers(@Param("sysUser") User sysUser, @Param("list") List<String> list);
}