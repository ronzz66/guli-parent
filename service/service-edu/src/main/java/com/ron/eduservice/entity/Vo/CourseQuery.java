package com.ron.eduservice.entity.Vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
@Data//分页查询课程的条件
public class CourseQuery implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "课程标题,模糊查询")
    private String title;

    @ApiModelProperty(value = "查询最低价格")
    private BigDecimal beginPrice;
    @ApiModelProperty(value = "查询最高价格")
    private BigDecimal endPrice;
    @ApiModelProperty(value = "课程状态")
    private String status ;//课程状态
}
