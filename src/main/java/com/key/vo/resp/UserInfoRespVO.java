package com.key.vo.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class UserInfoRespVO {
    @ApiModelProperty(value = "id")
    private String id;
    @ApiModelProperty(value = "账号")
    private String username;
    @ApiModelProperty(value = "角色id")
    private String roleId;
    @ApiModelProperty(value = "拥有角色名")
    private String roleName;

}
