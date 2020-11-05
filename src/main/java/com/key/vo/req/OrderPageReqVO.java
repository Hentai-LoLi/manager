package com.key.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class OrderPageReqVO {
    @ApiModelProperty(value = "第几页")
    private int pageNum=1;
    @ApiModelProperty(value = "当前页的数量")
    private int pageSize;

    @ApiModelProperty(value = "订单号")
    private String orderId;
    @ApiModelProperty(value = "订单状态")
    private Integer orderStatus ;
    @ApiModelProperty(value = "开始时间")
    private String startTime;
    @ApiModelProperty(value = "结束时间")
    private String endTime;
}
