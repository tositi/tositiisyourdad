package com.oddfar.campus.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oddfar.campus.model.campus.Fabulous;

/**
 * @author zhiyuan
 */
public interface FabulousService extends IService<Fabulous> {

    String zan(Long cid);

    Integer queryNum(Long cid);

    Boolean userIsZan(Long uid,Long cid);

    void delByUid(Long uid);
}
