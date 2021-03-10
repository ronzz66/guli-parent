package com.ron.orderservice.service.impl;

import com.ron.commonutils.orderVo.CourseWebVoOrder;
import com.ron.commonutils.orderVo.UcenterMemberOrder;
import com.ron.orderservice.entity.Order;
import com.ron.orderservice.feign.CourseClient;
import com.ron.orderservice.feign.UserClient;
import com.ron.orderservice.mapper.OrderMapper;
import com.ron.orderservice.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ron.orderservice.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-10-23
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private UserClient userClient;

    @Autowired
    private CourseClient courseClient;
    //生成订单
    @Override
    public String cretaeOrder(String courseId, String userId) {
        //feign远程调用 获取课程和用户信息.

        //1.获取用户信息
        UcenterMemberOrder user = userClient.getUserByid(userId);

        //2.获取课程信息
        CourseWebVoOrder courseInfo = courseClient.getCourseInfo(courseId);

        Order order=new Order();
        IdWorker worker =new IdWorker();
        String orderNo = String.valueOf(worker.nextId());//生成唯一订单号
        order.setOrderNo(orderNo);//订单号
        order.setCourseId(courseInfo.getId());//课程id
        order.setCourseTitle(courseInfo.getTitle());//课程标题
        order.setCourseCover(courseInfo.getCover());//课程图片
        order.setTeacherName(courseInfo.getTeacherName());//老师name
        order.setTotalFee(courseInfo.getPrice());//课程价格
        order.setMemberId(userId);//用户id
        order.setMobile(user.getMobile());//用户手机
        order.setNickname(user.getNickname());//用户昵称
        order.setStatus(0);//订单状态 0,未支付
        order.setPayType(1);//支付类型
        baseMapper.insert(order);//生成订单
        return order.getOrderNo();//返回订单号

    }
}
