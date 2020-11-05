package com.key.mapper;

import com.key.entity.Store;
import com.key.vo.req.StorePageReqVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreMapper {
    int deleteByPrimaryKey(Integer storeId);

    int insert(Store record);

    int insertSelective(Store record);

    Store selectByPrimaryKey(Integer storeId);

    int updateByPrimaryKeySelective(Store record);

    int updateByPrimaryKey(Store record);

    List<Store> selectAll(StorePageReqVO vo);
}