package com.oddfar.campus.service.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oddfar.campus.model.campus.Content;
import com.oddfar.campus.model.campus.Pictures;
import com.oddfar.campus.model.vo.campus.ContentVo;
import com.oddfar.campus.service.mapper.PictureMapper;
import com.oddfar.campus.service.service.ContentService;
import com.oddfar.campus.service.service.PictureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhiyuan
 */
@Service
@Slf4j
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Pictures> implements PictureService {

    @Autowired
    ContentService contentService;

    @Override
    public Boolean add(Pictures pictures) {


        boolean save = this.save(pictures);
        return save;
    }

    @Override
    public Boolean isAddOk(ContentVo contentVo) {
        Content content = contentService.getById(contentVo.getCid());
        if (ObjectUtil.isEmpty(content)) {
          return false;
        }
        //获取要上传的图片数量
        Integer pictureNum = content.getPictureNum();
        Long cid = content.getCid();
        //获取已经上传的图片数量
        Integer number = this.getNumByCid(cid);

        //如果数量+1 大于要求的返回 假
        if (number + 1 > pictureNum) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public List<Pictures> getByCid(Long cid) {
        QueryWrapper<Pictures> wrapper = new QueryWrapper<>();
        wrapper.eq("cid", cid);
        List<Pictures> picturesList = baseMapper.selectList(wrapper);
        return picturesList;

    }

    @Override
    public List<Long> getFileIdByCid(Long cid) {
        QueryWrapper<Pictures> wrapper = new QueryWrapper<>();
        wrapper.eq("cid", cid);
        List<Pictures> picturesList = baseMapper.selectList(wrapper);

        List<Long> idList = new ArrayList<>();
        for (Pictures pictures : picturesList) {
            idList.add(pictures.getFileId());
        }
        return idList;

    }


    /**
     * 获取cid图片的数量
     *
     * @param cid
     * @return
     */
    private Integer getNumByCid(Long cid) {
        QueryWrapper wrapper = new QueryWrapper();

        wrapper.eq("cid", cid);

        return baseMapper.selectCount(wrapper);

    }


}
