package com.key.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author 徐雨轩
 * @description
 * @date 2020-10-13 21:33
 */
@Data
public class AddCircleReqVO {
    @ApiModelProperty(value = "帖子内容")
    @NotBlank(message = "帖子内容不能为空")
    private String circleContent ;

    @ApiModelProperty(value = "帖子图片")
    private String circleImage ;

    @ApiModelProperty(value = "帖子回复数")
    private Integer circleCommentNum ;


    @ApiModelProperty(value = "帖子点赞数")
    private Integer circleAppreciateNum ;


    @ApiModelProperty(value = "发帖用户id")
    @NotNull(message = "发贴用户id不能为空")
    private Integer circleUid ;
}
