package com.key.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 徐雨轩
 * @description
 * @date 2020-10-13 20:40
 */
@Data
public class OrderDetailPageReqVO {

    @ApiModelProperty(value = "第几页")
    private int pageNum=1;
    @ApiModelProperty(value = "当前页的数量")
    private int pageSize;

    @ApiModelProperty(value = "订单号")
    private String orderId;

}
