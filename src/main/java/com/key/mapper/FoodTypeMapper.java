package com.key.mapper;

import com.key.entity.FoodType;
import com.key.vo.req.FoodTypePageReqVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodTypeMapper {
    int deleteByPrimaryKey(Integer ftsId);

    int insert(FoodType record);

    int insertSelective(FoodType record);

    FoodType selectByPrimaryKey(Integer ftsId);

    int updateByPrimaryKeySelective(FoodType record);

    int updateByPrimaryKey(FoodType record);

    List<FoodType> selectAll(FoodTypePageReqVO vo);
}