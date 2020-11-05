package com.key.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author 徐雨轩
 * @description
 * @date 2020-09-23 14:07
 */
@Data
public class AddStoreTypeReqVO {

    @ApiModelProperty(value = "店铺类型名称")
    @NotBlank(message = "店铺类型名称不能为空")
    private String stsName;
}
