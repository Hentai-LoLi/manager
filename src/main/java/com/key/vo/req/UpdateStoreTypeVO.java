package com.key.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author 徐雨轩
 * @description
 * @date 2020-09-23 14:35
 */
@Data
public class UpdateStoreTypeVO {

    @ApiModelProperty(value = "店铺类型id")
    private String stsId;


    @ApiModelProperty(value = "店铺类型名称")
    @NotBlank(message = "店铺类型名称不能为空")
    private String stsName;
}
