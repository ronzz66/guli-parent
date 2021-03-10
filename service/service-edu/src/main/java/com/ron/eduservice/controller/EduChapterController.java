package com.ron.eduservice.controller;


import com.ron.commonutils.R;
import com.ron.eduservice.entity.EduChapter;
import com.ron.eduservice.entity.chapter.ChapterVo;
import com.ron.eduservice.feign.VodClitent;
import com.ron.eduservice.service.EduChapterService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-10-14
 */
//课程章节模块
@RestController
//@CrossOrigin
@Api(tags ="课程大纲")
@RequestMapping("/eduservice/edu-chapter")
public class EduChapterController {


    @Autowired
    private EduChapterService chapterService;

    //根据课程id查询章节
    @GetMapping("/getChapter/{courseId}")
    public R getChapterByourseId(@PathVariable("courseId")String courseId){
        List<ChapterVo> chapterVoList=chapterService.getChapterById(courseId);

        return R.ok().data("allChapterVideo",chapterVoList);
    }

    //添加章节
    @PostMapping("/addChapter")
    public R addChapter(@RequestBody EduChapter  eduChapter){
        chapterService.save(eduChapter);
        return R.ok();
    }

    //根据章节id查询章节
    @GetMapping("/getChapterInfo/{id}")
    public R getChapter(@PathVariable String  id){
        EduChapter eduChapter = chapterService.getById(id);
        return R.ok().data("chapter",eduChapter);
    }

    //修改章节
    @PostMapping("/updateChapter")
    public R updateChapter(@RequestBody EduChapter  eduChapter){
        chapterService.updateById(eduChapter);
        return R.ok();
    }

    //删除章节
    @DeleteMapping("/deleteChapter/{id}")
    public R deleteChapter(@PathVariable String  id){
        //删除章节
        boolean flag=chapterService.deleteChapter(id);


        if (flag==true){
            return R.ok();
        }else {
            return R.error();
        }

    }
}

