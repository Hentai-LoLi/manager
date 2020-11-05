package com.key.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Food implements Serializable {
    private Integer foodId;

    private Integer foodSid;

    private String foodName;

    private Integer foodType;

    private BigDecimal foodPrice;

    private String foodImage;

    private Date foodDate;

    private Integer foodStatus;

    private static final long serialVersionUID = 1L;

}