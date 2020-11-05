package com.key.mapper;

import com.key.entity.Circle;
import com.key.vo.req.CirclePageReqVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CircleMapper {
    int deleteByPrimaryKey(Integer circleId);

    int insert(Circle record);

    int insertSelective(Circle record);

    Circle selectByPrimaryKey(Integer circleId);

    int updateByPrimaryKeySelective(Circle record);

    int updateByPrimaryKey(Circle record);

    List<Circle> selectAll(CirclePageReqVO vo);
}