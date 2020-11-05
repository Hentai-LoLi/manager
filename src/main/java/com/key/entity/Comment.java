package com.key.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Comment implements Serializable {
    private Integer commentId;

    private Integer commentUid;

    private String commentContent;

    private Integer commentUided;

    private Integer commentFoodCircleId;

    private Date commentDate;

    private static final long serialVersionUID = 1L;


}