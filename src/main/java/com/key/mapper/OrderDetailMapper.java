package com.key.mapper;

import com.key.entity.OrderDetail;
import com.key.vo.req.OrderDetailPageReqVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailMapper {
    int deleteByPrimaryKey(@Param("itemId") Integer itemId);

    int insert(OrderDetail record);

    int insertSelective(OrderDetail record);

    OrderDetail selectByPrimaryKey(@Param("itemId") Integer itemId);

    int updateByPrimaryKeySelective(OrderDetail record);

    int updateByPrimaryKey(OrderDetail record);

    List<OrderDetail> selectAll(OrderDetailPageReqVO vo);
}