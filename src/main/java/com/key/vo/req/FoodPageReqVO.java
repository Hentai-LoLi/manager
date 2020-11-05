package com.key.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 徐雨轩
 * @description
 * @date 2020-10-13 16:09
 */
@Data
public class FoodPageReqVO {
    @ApiModelProperty(value = "第几页")
    private int pageNum=1;
    @ApiModelProperty(value = "当前页的数量")
    private int pageSize;

    @ApiModelProperty(value = "商品名称")
    private String foodName;
    @ApiModelProperty(value = "商品状态")
    private Integer foodStatus ;
    @ApiModelProperty(value = "开始时间")
    private String startTime;
    @ApiModelProperty(value = "结束时间")
    private String endTime;
}
