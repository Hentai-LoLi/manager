package com.key.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class User implements Serializable {
    private String id;

    private String username;

    private String salt;

    private String password;

    private String phone;

    private String nickName;

    private String address;

    private String email;

    private Integer status;

    private Integer sex;

    private Integer deleted;

    private String createId;

    private String updateId;

    private Byte createWhere;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

}