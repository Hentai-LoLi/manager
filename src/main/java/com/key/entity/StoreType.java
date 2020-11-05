package com.key.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class StoreType implements Serializable {
    private Integer stsId;

    private String stsName;

    private static final long serialVersionUID = 1L;

}