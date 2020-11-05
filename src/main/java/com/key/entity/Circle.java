package com.key.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Circle implements Serializable {
    private Integer circleId;

    private String circleIssue;

    private String circleContent;

    private String circleImage;

    private Integer circleCommentNum;

    private Integer circleAppreciateNum;

    private Date circleDate;

    private Integer circleUid;

    private static final long serialVersionUID = 1L;

}