package com.key.mapper;

import com.key.entity.StoreType;
import com.key.vo.req.StoreTypeReqVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreTypeMapper {
    int deleteByPrimaryKey(Integer stsId);

    int insert(StoreType record);

    int insertSelective(StoreType record);

    StoreType selectByPrimaryKey(Integer stsId);

    int updateByPrimaryKeySelective(StoreType record);

    int updateByPrimaryKey(StoreType record);

    List<StoreType> selectAll(StoreTypeReqVO vo);
}