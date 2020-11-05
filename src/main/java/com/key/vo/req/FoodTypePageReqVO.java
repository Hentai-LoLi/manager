package com.key.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 徐雨轩
 * @description
 * @date 2020-10-13 15:06
 */
@Data
public class FoodTypePageReqVO {
    @ApiModelProperty(value = "第几页")
    private int pageNum=1;
    @ApiModelProperty(value = "当前页的数量")
    private int pageSize;

    @ApiModelProperty(value = "商品类型名称")
    private String ftsName;
}
