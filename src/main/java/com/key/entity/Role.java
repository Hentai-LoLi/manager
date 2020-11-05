package com.key.entity;

import com.key.vo.resp.PermissionRespNodeVO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class Role implements Serializable {
    private String id;

    private String name;

    private String description;

    private Byte status;

    private Date createTime;

    private Date updateTime;

    private Integer deleted;

    private List<PermissionRespNodeVO> permissionRespNode;

}