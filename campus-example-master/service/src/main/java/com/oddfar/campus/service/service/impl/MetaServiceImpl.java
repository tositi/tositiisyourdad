package com.oddfar.campus.service.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oddfar.campus.common.exception.CampusException;
import com.oddfar.campus.model.campus.Meta;
import com.oddfar.campus.model.vo.campus.MetaVo;
import com.oddfar.campus.service.mapper.MetaMapper;
import com.oddfar.campus.service.service.ContentService;
import com.oddfar.campus.service.service.MetaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhiyuan
 */
@Service
@Slf4j
public class MetaServiceImpl extends ServiceImpl<MetaMapper, Meta> implements MetaService {

    @Autowired
    ContentService contentService;

    @Override
    public Boolean addMeta(Meta meta) {
        if (ObjectUtil.isEmpty(meta)) {
            throw new CampusException("分类不能为空", 2001);
        }
        Map<String, Object> map = new HashMap();
        map.put("slug", meta.getSlug());
        map.put("name", meta.getName());
        List<Meta> metaList = baseMapper.selectByMap(map);
        if (ObjectUtil.isNotEmpty(metaList)) {
            throw new CampusException("分类名或缩略名已经存在", 2001);
        }

        return this.save(meta);
    }

    @Override
    public Boolean editMeta(Meta meta) {
        QueryWrapper<Meta> wrapper = creatQueryWrapper();
        wrapper.and(i -> i.eq("slug", meta.getSlug()).or().eq("name", meta.getName()));

        wrapper.ne("mid", meta.getMid());

        List<Meta> metaList = baseMapper.selectList(wrapper);
        if (ObjectUtil.isNotEmpty(metaList)) {
            throw new CampusException("分类名或缩略名已经存在", 2001);
        }

        Meta newMeta = this.queryMeta(meta);
        BeanUtil.copyProperties(meta, newMeta);
        return this.updateById(newMeta);
    }

    @Override
    public Meta detailMeta(Meta meta) {
        return this.queryMeta(meta);
    }

    @Override
    public int deleteMeta(Meta meta) {
        meta = this.queryMeta(meta);
        //删除关于分类的文章
        Map<String, Object> map = new HashMap<>();
        map.put("mid", meta.getMid());
        contentService.removeByMap(map);
        //删除分类
        int i = baseMapper.deleteById(meta.getMid());
        return i;
    }

    @Override
    public IPage<Meta> selectPage(Page<Meta> pageParam, MetaVo metaVo) {
        //创建查询wrapper
        Meta meta = new Meta();
        BeanUtil.copyProperties(metaVo, meta);
        QueryWrapper<Meta> wrapper = creatQueryWrapper();

        String createTimeBegin = metaVo.getCreateTimeBegin();
        String createTimeEnd = metaVo.getCreateTimeEnd();

        if (!StringUtils.isEmpty(createTimeBegin)) {
            wrapper.ge("create_time", createTimeBegin);
        }
        if (!StringUtils.isEmpty(createTimeEnd)) {
            wrapper.le("create_time", createTimeEnd);
        }

        //调用mapper的方法
        IPage<Meta> pages = baseMapper.selectPage(pageParam, wrapper);
        return pages;

    }


    /**
     * 获取分类信息
     */
    private Meta queryMeta(Meta meta) {
        meta = this.getById(meta.getMid());
        if (ObjectUtil.isEmpty(meta)) {
            throw new CampusException("分类不存在", 2001);
        }
        return meta;
    }

    private QueryWrapper creatQueryWrapper() {
        QueryWrapper<Meta> queryWrapper = new QueryWrapper<>();

        // 根据时间倒序排列
        queryWrapper.orderByDesc("create_time");
        return queryWrapper;
    }

}
