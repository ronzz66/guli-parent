package com.ron.eduservice.entity.chapter;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data//课程章节
public class ChapterVo {

    private String id;

    private String title;

    private List<VideoVo> children=new ArrayList<>();
}
