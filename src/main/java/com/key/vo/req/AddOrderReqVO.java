package com.key.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author 徐雨轩
 * @description
 * @date 2020-10-13 19:58
 */
@Data
public class AddOrderReqVO {
    @ApiModelProperty(value = "下单店铺id")
    @NotNull(message = "下单店铺id不能为空")
    private Integer orderSid;

    @ApiModelProperty(value = "下单用户id")
    @NotNull(message = "下单用户id不能为空")
    private Integer orderUid;

    @ApiModelProperty(value = "订单收货地址")
    @NotBlank(message = "订单收获地址")
    private String orderAddress;

    @ApiModelProperty(value = "订单状态")
    private Integer orderStatus;

    @ApiModelProperty(value = "订单总价")
    @NotNull(message = "订单总价不能为空")
    private BigDecimal orderTotal;

}
