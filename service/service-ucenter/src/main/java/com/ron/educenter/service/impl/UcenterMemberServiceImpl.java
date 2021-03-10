package com.ron.educenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ron.commonutils.JwtUtils;
import com.ron.commonutils.MD5;
import com.ron.educenter.entity.UcenterMember;
import com.ron.educenter.entity.Vo.RegisterVo;
import com.ron.educenter.mapper.UcenterMemberMapper;
import com.ron.educenter.service.UcenterMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ron.exceptionhandler.GuliException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-10-20
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {


    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    //登录方法
    @Override
    public String login(UcenterMember ucenterMember) {

        String mobile = ucenterMember.getMobile();
        String password = ucenterMember.getPassword();
        //判断手机号和密码是否为空
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
            throw new GuliException(20001, "登录失败");
        }
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        //根据手机号查询用户
        UcenterMember mobileMember = baseMapper.selectOne(wrapper);
        if (mobileMember == null) {
            throw new GuliException(20001, "登录失败");
        }
        String ps = mobileMember.getPassword();//获取查询出来的密码
        //MD5加密密码判断是否一致
        if (!ps.equals(MD5.encrypt(password))) {//判断密码是否一致
            throw new GuliException(20001, "登录失败");
        }
        if (mobileMember.getIsDeleted()) {//判断是否禁用
            throw new GuliException(20001, "登录失败");
        }
        //登录成功,生成token返回
        String jwtToken = JwtUtils.getJwtToken(mobileMember.getId(), mobileMember.getNickname());
        return jwtToken;
    }

    //注册方法
    @Override
    public void register(RegisterVo registerVo) {
        //获取验证码
        String code = registerVo.getCode();
        //手机号
        String mobile = registerVo.getMobile();
        //账户
        String nickname = registerVo.getNickname();
        //密码
        String password = registerVo.getPassword();
        //判断这些是否为空
        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(mobile) || StringUtils.isEmpty(nickname) || StringUtils.isEmpty(password)) {
            throw new GuliException(20001, "注册失败");
        }
        //redis中的验证码和用户输入是否一致
        String redisCode = redisTemplate.opsForValue().get(mobile);
        if (!redisCode.equals(code)) {
            throw new GuliException(20001, "验证码错误");
        }
        //判断手机号是否注册过
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile", mobile);
        //根据手机号查询用户
        Integer integer = baseMapper.selectCount(wrapper);
        if (integer>0){
            throw new GuliException(20001, "用户已注册过");
        }

        //最终添加进数据库
        UcenterMember member=new UcenterMember();
        member.setMobile(mobile);//手机
        member.setNickname(nickname);//昵称
        member.setPassword(MD5.encrypt(password));//加密后的mim
        member.setAvatar("https://timgsa.baidu.com/timg?image&" +
                "quality=80&size=b9999_10000&sec=1602594033140&di=f58" +
                "509beca7906e3945c127dcff14378&imgtype=0&src=http%3A%2" +
                "F%2Fattach.bbs.miui.com%2Fforum%2F201108%2F12" +
                "%2F153524babrex391xoaz5za.jpg");//默认头像
        baseMapper.insert(member);

    }

    //根据openid查询登录用户
    @Override
    public UcenterMember getByIdOpenId(String openid) {
        QueryWrapper<UcenterMember> wrapper=new QueryWrapper<>();
        wrapper.eq("openid",openid);

        UcenterMember ucenterMember = baseMapper.selectOne(wrapper);

        return ucenterMember;
    }

    //查询一天的注册人数
    @Override
    public Integer countRegister(String day) {

        return baseMapper.countRegister(day);
    }
}
