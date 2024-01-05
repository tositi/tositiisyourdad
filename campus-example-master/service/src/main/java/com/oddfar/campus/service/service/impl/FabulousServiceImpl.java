package com.oddfar.campus.service.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oddfar.campus.common.exception.CampusException;
import com.oddfar.campus.model.campus.Content;
import com.oddfar.campus.model.campus.Fabulous;
import com.oddfar.campus.model.campus.SysUser;
import com.oddfar.campus.model.user.LoginUser;
import com.oddfar.campus.service.mapper.FabulousMapper;
import com.oddfar.campus.service.service.ContentService;
import com.oddfar.campus.service.service.FabulousService;
import com.oddfar.campus.service.service.SysUserService;
import com.oddfar.campus.service.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhiyuan
 */
@Service
@Slf4j
public class FabulousServiceImpl extends ServiceImpl<FabulousMapper, Fabulous> implements FabulousService {

    @Autowired
    ContentService contentService;
    @Autowired
    private SysUserService sysUserService;

    @Override
    public String zan(Long cid) {
        Content content = contentService.getById(cid);
        if (ObjectUtil.isEmpty(content)) {
            throw new CampusException("cid不存在", 2001);
        }
        if (content.getStatus() != 1) {
            throw new CampusException("禁止点赞", 2001);
        }

        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();

        Long zan = isZan(user.getUid(), cid);
        if (ObjectUtil.isNotEmpty(zan)) {
            //取消点赞
            this.removeById(zan);
            return "取消点赞成功！";
        } else {
            //点赞
            Fabulous fabulous = new Fabulous();
            fabulous.setUid(user.getUid());
            fabulous.setCid(cid);
            this.save(fabulous);
            return "点赞成功！";
        }

    }

    @Override
    public Integer queryNum(Long cid) {
        QueryWrapper<Fabulous> wrapper = new QueryWrapper<>();
        wrapper.eq("cid", cid);
        Integer integer = baseMapper.selectCount(wrapper);
        return integer;
    }

    @Override
    public Boolean userIsZan(Long uid, Long cid) {

        QueryWrapper<Fabulous> wrapper = new QueryWrapper<>();
        wrapper.eq("cid", cid);
        wrapper.eq("uid", uid);
        Integer integer = baseMapper.selectCount(wrapper);

        if (integer >= 1) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void delByUid(Long uid) {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", uid);
        this.removeByMap(map);
    }

    private Long isZan(Long uid, Long cid) {
        QueryWrapper<Fabulous> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", uid).eq("cid", cid);
        Fabulous fabulous = baseMapper.selectOne(wrapper);
        if (ObjectUtil.isEmpty(fabulous)) {
            return null;
        } else {
            return fabulous.getFid();
        }
    }


}
