package com.ron.eduservice.entity.Vo;

import lombok.Data;

@Data//最终页面显示对象
public class CourseFinally {

    private String id;
    private String title;

    private String cover;

    private Integer lessonNum;

    private String subjectLevelOne;

    private String subjectLevelTwo;

    private String teacherName;

    private String price;//只用于显示
}
