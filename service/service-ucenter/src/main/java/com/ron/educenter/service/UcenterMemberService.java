package com.ron.educenter.service;

import com.ron.educenter.entity.UcenterMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ron.educenter.entity.Vo.RegisterVo;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-10-20
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    String login(UcenterMember ucenterMember);

    void register(RegisterVo registerVo);

    UcenterMember getByIdOpenId(String openid);

    Integer countRegister(String day);
}
