package com.key.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OrderDetail implements Serializable {
    private Integer itemId;

    private Integer orderId;

    private Integer foodId;

    private Integer foodNum;

    private BigDecimal foodPrice;

    private BigDecimal foodTotal;

    private static final long serialVersionUID = 1L;


}