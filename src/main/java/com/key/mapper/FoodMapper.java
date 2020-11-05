package com.key.mapper;

import com.key.entity.Food;
import com.key.vo.req.FoodPageReqVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodMapper {
    int deleteByPrimaryKey(Integer foodId);

    int insert(Food record);

    int insertSelective(Food record);

    Food selectByPrimaryKey(Integer foodId);

    int updateByPrimaryKeySelective(Food record);

    int updateByPrimaryKey(Food record);

    List<Food> selectAll(FoodPageReqVO vo);
}