package com.oddfar.campus.service.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oddfar.campus.common.exception.CampusException;
import com.oddfar.campus.model.campus.SysDict;
import com.oddfar.campus.model.vo.campus.SysDictVo;
import com.oddfar.campus.service.mapper.SysDictMapper;
import com.oddfar.campus.service.service.SysDictService;
import org.springframework.stereotype.Service;

/**
 * @author zhiyuan
 */
@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements SysDictService {


    @Override
    public IPage<SysDict> selectPage(Page<SysDict> pageParam, SysDictVo sysDictVo) {
        QueryWrapper<SysDict> wrapper = this.creatQueryWrapper(sysDictVo);

        //调用mapper的方法
        Page<SysDict> commentPage = baseMapper.selectPage(pageParam, wrapper);

        return commentPage;
    }

    @Override
    public IPage<SysDict> selectPage(Page<SysDict> pageParam, SysDict sysDict) {
        SysDictVo sysDictVo = new SysDictVo();
        BeanUtil.copyProperties(sysDict,sysDictVo);
        return this.selectPage(pageParam,sysDictVo);
    }


    @Override
    public SysDict selectById(Long id) {
        SysDict sysDict = this.getById(id);
        if (ObjectUtil.isEmpty(sysDict)) {
            throw new CampusException("dictId不存在", 2001);
        } else {
            return sysDict;
        }
    }

    @Override
    public void updateDict(SysDict sysDict) {
        QueryWrapper<SysDict> wrapper = new QueryWrapper<>();
        wrapper.ne("dict_id", sysDict.getDictId())
                .and(i -> i.eq("dict_code", sysDict.getDictCode()).or().eq("dict_name", sysDict.getDictName()));

        Integer count = baseMapper.selectCount(wrapper);
        if (count > 0) {
            throw new CampusException("字典编码或名称已存在", 2001);
        } else {
            this.updateById(sysDict);
        }
    }

    @Override
    public void addSysDict(SysDict sysDict) {
        QueryWrapper<SysDict> wrapper = new QueryWrapper<>();
        wrapper.eq("dict_code", sysDict.getDictCode()).or().eq("dict_name", sysDict.getDictName());
        Integer count = baseMapper.selectCount(wrapper);
        if (count > 0) {
            throw new CampusException("字典编码或名称已存在", 2001);
        } else {
            this.save(sysDict);
        }
    }

    private QueryWrapper creatQueryWrapper() {
        QueryWrapper<SysDict> wrapper = new QueryWrapper<>();

        // 根据倒序排列
        wrapper.orderByDesc("dict_sort");
        return wrapper;
    }
    private QueryWrapper creatQueryWrapper(SysDictVo sysDictVo) {
        QueryWrapper<SysDict> wrapper = new QueryWrapper<>();

        if(ObjectUtil.isNotEmpty(sysDictVo)){
            wrapper.like(ObjectUtil.isNotEmpty(sysDictVo.getDictCode()), "dict_code", sysDictVo.getDictCode());
            wrapper.like(ObjectUtil.isNotEmpty(sysDictVo.getDictName()), "dict_name", sysDictVo.getDictName());

            wrapper.ge(ObjectUtil.isNotEmpty(sysDictVo.getCreateTimeBegin()), "create_time", sysDictVo.getCreateTimeBegin());
            wrapper.le(ObjectUtil.isNotEmpty(sysDictVo.getCreateTimeEnd()), "create_time", sysDictVo.getCreateTimeEnd());

        }

        // 根据倒序排列
        wrapper.orderByDesc("dict_sort");
        return wrapper;
    }
}
