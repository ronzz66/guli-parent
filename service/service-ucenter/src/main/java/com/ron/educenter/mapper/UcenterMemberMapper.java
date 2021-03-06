package com.ron.educenter.mapper;

import com.ron.educenter.entity.UcenterMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2020-10-20
 */
public interface UcenterMemberMapper extends BaseMapper<UcenterMember> {
    //查询一天的注册人数
    Integer countRegister(String day);
}
