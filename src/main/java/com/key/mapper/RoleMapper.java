package com.key.mapper;

import com.key.entity.Role;
import com.key.vo.req.RolePageReqVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleMapper {
    int deleteByPrimaryKey(String id);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);


    List<Role> selectAll(RolePageReqVO vo);


    List<String> selectNamesByIds(List<String> ids);
}