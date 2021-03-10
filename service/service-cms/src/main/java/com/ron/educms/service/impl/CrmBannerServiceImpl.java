package com.ron.educms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ron.educms.entity.CrmBanner;
import com.ron.educms.mapper.CrmBannerMapper;
import com.ron.educms.service.CrmBannerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-10-18
 */
@Service
public class CrmBannerServiceImpl extends ServiceImpl<CrmBannerMapper, CrmBanner> implements CrmBannerService {


    //查询轮播图
    @Override
    @Cacheable(key = "'selectIndexList'",value = "banner")
    public List<CrmBanner> selectAllBanner() {
        QueryWrapper<CrmBanner> wrapper =new QueryWrapper<>();
        wrapper.orderByDesc("id");
        wrapper.last("limit 2");
        List<CrmBanner> banners = baseMapper.selectList(null);
        return banners;
    }
}
