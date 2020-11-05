package com.key.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author 徐雨轩
 * @description
 * @date 2020-10-13 14:10
 */
@Data
public class UpdateStoreReqVO {

    @ApiModelProperty(value = "店铺id")
    @NotNull(message = "店铺 id 不能为空")
    private Integer storeId_ae;

    @ApiModelProperty(value = "店铺名称")
    @NotBlank(message = "店铺名称不能为空")
    private String storeName_ae;

    @ApiModelProperty(value = "开店用户id")
    @NotNull(message = "开店用户Id不能为空")
    private Integer storeUid_ae;

    @ApiModelProperty(value = "店铺位置")
    @NotBlank(message = "店铺位置不能为空")
    private String storeLocation_ae;

    @ApiModelProperty(value = "店铺类型")
    private Integer storeType_ae;

    @ApiModelProperty(value = "店铺描述")
    private String storeDescribe_ae;


    @ApiModelProperty(value = "店铺状态")
    private Integer storeStatus_ae;
}
