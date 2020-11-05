package com.key.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author 徐雨轩
 * @description
 * @date 2020-10-13 20:47
 */
@Data
public class UpdateOrderDetailReqVO {

    @ApiModelProperty(value = "订单详情Id")
    private Integer itemId;

    @ApiModelProperty(value = "订单Id")
    @NotNull(message = "订单Id不能为空")
    private Integer orderId;

    @ApiModelProperty(value = "商品id")
    @NotNull(message = "商品id不能为空")
    private Integer foodId;

    @ApiModelProperty(value = "商品数量")
    @NotNull(message = "商品数量不能为空")
    private Integer foodNum;

    @ApiModelProperty(value = "商品单价")
    @NotNull(message = "商品单价不能为空")
    private BigDecimal foodPrice;

    @ApiModelProperty(value = "商品总价")
    @NotNull(message = "商品总价不能为空")
    private BigDecimal foodTotal;
}
