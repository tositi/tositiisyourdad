package com.oddfar.campus.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.oddfar.campus.model.campus.Pictures;
import com.oddfar.campus.model.vo.campus.ContentVo;

import java.util.List;

/**
 * @author zhiyuan
 */
public interface PictureService extends IService<Pictures> {

    Boolean add(Pictures pictures);


    /**
     * 是否可以插入图片，true为可插入
     */
    Boolean isAddOk(ContentVo contentVo);

    /**
     * 通过cid查询图片url列表
     * @param cid
     * @return
     */
    List<Pictures> getByCid(Long cid);

    /**
     * 根据cid获取文件id列表
     * @param cid
     * @return
     */
    List<Long> getFileIdByCid(Long cid);
}
