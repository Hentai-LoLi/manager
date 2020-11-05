package com.key.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author 徐雨轩
 * @description
 * @date 2020-10-13 16:16
 */
@Data
public class UpdateFoodReqVO {

    @ApiModelProperty(value = "商品id")
    private Integer foodId;


    @ApiModelProperty(value = "商品名称")
    @NotBlank(message = "商品名称不能为空")
    private String foodName;

    @ApiModelProperty(value = "商品所属店铺id")
    @NotNull(message = "商品所属店铺id不能为空")
    private Integer foodSid;

    @ApiModelProperty(value = "商品类型")
    private Integer foodType;


    @ApiModelProperty(value = "商品价格")
    @NotNull(message = "商品价格不能为空")
    private BigDecimal foodPrice;


    @ApiModelProperty(value = "商品状态")
    private Integer foodStatus;
}
