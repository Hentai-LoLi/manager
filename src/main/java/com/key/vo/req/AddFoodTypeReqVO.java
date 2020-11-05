package com.key.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author 徐雨轩
 * @description
 * @date 2020-10-13 15:10
 */
@Data
public class AddFoodTypeReqVO {

    @ApiModelProperty(value = "商品类型所属店铺id")
    @NotNull(message = "商品类型所属店铺id不能为空")
    private Integer ftsSid;


    @ApiModelProperty(value = "商品类型名称")
    @NotBlank(message = "商品类型名称不能为空")
    private String ftsName;
}
