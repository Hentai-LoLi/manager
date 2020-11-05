package com.key.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Store implements Serializable {
    private Integer storeId;

    private Integer storeUid;

    private String storeName;

    private String storeLocation;

    private Integer storeType;

    private String storeDescribe;

    private String storeImage;

    private Integer storeAppreciateNum;

    private Integer storeCommentNum;

    private Date storeDate;

    private Integer storeStatus;

    private static final long serialVersionUID = 1L;

}