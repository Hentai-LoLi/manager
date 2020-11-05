package com.key.vo.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author 徐雨轩
 * @description
 * @date 2020-10-13 22:33
 */
@Data
public class AddCommentReqVO {

    @ApiModelProperty(value = "发起评论的用户id")
    @NotNull(message = "发起评论的用户id不能为空")
    private Integer commentUid  ;

    @ApiModelProperty(value = "评论内容")
    @NotBlank(message = "评论内容不能为空")
    private String commentContent  ;

    @ApiModelProperty(value = "被回复的用户")
    @NotNull(message = "被回复的用户id不能为空")
    private Integer commentUided  ;

    @ApiModelProperty(value = "回复的美食圈帖子id")
    private Integer commentFoodCircleId   ;

}
