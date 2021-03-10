package com.ron.eduservice.controller.front;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ron.commonutils.JwtUtils;
import com.ron.commonutils.R;
import com.ron.commonutils.orderVo.CourseWebVoOrder;
import com.ron.eduservice.entity.EduCourse;
import com.ron.eduservice.entity.EduTeacher;
import com.ron.eduservice.entity.Vo.CourseInfo;
import com.ron.eduservice.entity.chapter.ChapterVo;
import com.ron.eduservice.entity.frontVo.CourseFrontVo;
import com.ron.eduservice.entity.frontVo.CourseWebVo;
import com.ron.eduservice.feign.OrderClient;
import com.ron.eduservice.service.EduChapterService;
import com.ron.eduservice.service.EduCourseService;
import com.ron.eduservice.service.EduTeacherService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/eduservice/courseFront")
public class CourseFrontController {
    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduChapterService chapterService;

    @Autowired
    private OrderClient orderClient;//查询订单状态

    //条件查询分页课程
    @PostMapping("/getFrontCourseList/{page}/{limit}")
    public R getCourseList(@PathVariable("page")Long page,
                           @PathVariable("limit")Long limit, @RequestBody(required = false) CourseFrontVo courseFrontVo){
        Page<EduCourse> pageInfo=new Page<>(page,limit);
        Map<String,Object> map=courseService.getFrontCourse(pageInfo,courseFrontVo);


        //返回分页信息(前台没有使用elementUi,要返回详细的分页信息)
        return R.ok().data(map);
    }


    //查询课程详情
    @GetMapping("/getFrontCourseInfo/{courseId}")
    public R getFrontCourseInfo(@PathVariable("courseId") String courseId, HttpServletRequest request){
        CourseWebVo courseWebVo=courseService.getFrontCourseInfo(courseId);//查询课程详情

        List<ChapterVo> chapter = chapterService.getChapterById(courseId);//查询课程的所有章节

        //根据课程id和用户id查询课程订单是否被购买
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        boolean status = orderClient.getOrderStatus(courseId, memberId);

        //返回课程详情和章节
        return R.ok().data("courseWebVo",courseWebVo).data("chapterList",chapter).data("isBuy",status);
    }


    //根据课程的id查询课程的信息
    @PostMapping("/getCourseInfo/{id}")
    public CourseWebVoOrder getCourseInfo(@PathVariable("id")String id){
        CourseWebVo frontCourseInfo = courseService.getFrontCourseInfo(id);
        CourseWebVoOrder order = new CourseWebVoOrder();
        BeanUtils.copyProperties(frontCourseInfo,order);

        return order;

    }

}

