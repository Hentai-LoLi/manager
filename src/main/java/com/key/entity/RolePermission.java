package com.key.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class RolePermission implements Serializable {
    private String id;

    private String roleId;

    private String permissionId;

    private Date createTime;
}