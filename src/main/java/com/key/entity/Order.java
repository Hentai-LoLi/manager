package com.key.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Order implements Serializable {
    private Integer orderId;

    private Integer orderSid;

    private Integer orderUid;

    private Date orderTime;

    private String orderAddress;

    private Integer orderStatus;

    private BigDecimal orderTotal;

    private static final long serialVersionUID = 1L;


}