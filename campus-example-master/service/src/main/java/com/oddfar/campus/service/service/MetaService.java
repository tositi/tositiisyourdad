package com.oddfar.campus.service.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.oddfar.campus.model.campus.Meta;
import com.oddfar.campus.model.vo.campus.MetaVo;

/**
 * @author zhiyuan
 */
public interface MetaService extends IService<Meta> {

    Boolean addMeta(Meta meta);

    Boolean editMeta(Meta meta);

    Meta detailMeta(Meta meta);

    int deleteMeta(Meta meta);

    IPage<Meta> selectPage(Page<Meta> pageParam, MetaVo metaVo);

}
