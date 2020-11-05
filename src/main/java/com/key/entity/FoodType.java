package com.key.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class FoodType implements Serializable {
    private Integer ftsId;

    private Integer ftsSid;

    private String ftsName;

    private static final long serialVersionUID = 1L;


}