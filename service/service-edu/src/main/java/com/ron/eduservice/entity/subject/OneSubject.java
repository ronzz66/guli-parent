package com.ron.eduservice.entity.subject;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data//一级分类
public class OneSubject {
    private String id;
    private String title;

    //一个一级分类包含多个二级分类
    private List<TwoSubject> children=new ArrayList<>();
}
