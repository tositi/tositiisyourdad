package com.oddfar.campus.service.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.oddfar.campus.model.campus.SysDict;
import com.oddfar.campus.model.vo.campus.SysDictVo;

/**
 * @author zhiyuan
 */
public interface SysDictService extends IService<SysDict> {
    //带条件的分页查询
    IPage<SysDict> selectPage(Page<SysDict> pageParam, SysDictVo sysDictVo);

    IPage<SysDict> selectPage(Page<SysDict> pageParam, SysDict sysDict);

    /**
     * 根据 id 查找字典
     * @param id dictID
     * @return
     */
    SysDict selectById(Long id);

    void updateDict(SysDict sysDict);

    void addSysDict(SysDict sysDict);

}
