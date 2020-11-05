package com.key.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 徐雨轩
 * @description
 * @date 2020-09-23 10:43
 */
@Data
public class StoreTypeReqVO {
    @ApiModelProperty(value = "第几页")
    private int pageNum=1;
    @ApiModelProperty(value = "当前页的数量")
    private int pageSize;

    @ApiModelProperty(value = "店铺类型名称")
    private String stsName;
}
