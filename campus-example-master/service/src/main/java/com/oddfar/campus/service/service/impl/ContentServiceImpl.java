package com.oddfar.campus.service.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oddfar.campus.common.exception.CampusException;
import com.oddfar.campus.model.campus.Content;
import com.oddfar.campus.model.campus.Meta;
import com.oddfar.campus.model.campus.Pictures;
import com.oddfar.campus.model.campus.SysUser;
import com.oddfar.campus.model.user.LoginUser;
import com.oddfar.campus.model.vo.campus.ContentVo;
import com.oddfar.campus.service.mapper.ContentMapper;
import com.oddfar.campus.service.service.*;
import com.oddfar.campus.service.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhiyuan
 */
@Service
@Slf4j
public class ContentServiceImpl extends ServiceImpl<ContentMapper, Content> implements ContentService {

    @Autowired
    SysUserService sysUserService;
    @Autowired
    MetaService metaService;
    @Autowired
    PictureService pictureService;
    @Autowired
    FabulousService fabulousService;
    @Autowired
    CommentService commentService;
    @Autowired
    FileInfoService fileInfoService;

    public IPage<Content> selectPage(Page<Content> pageParam, ContentVo contentVo) {
        //创建查询wrapper
        Content content = new Content();
        BeanUtil.copyProperties(contentVo, content);
        QueryWrapper<Content> wrapper = this.creatQueryWrapper();

        String createTimeBegin = contentVo.getCreateTimeBegin();
        String createTimeEnd = contentVo.getCreateTimeEnd();

        if (!ObjectUtil.isEmpty(contentVo.getCid())) {
            wrapper.eq("cid", contentVo.getCid());
        }
        if (!ObjectUtil.isEmpty(contentVo.getMid())) {
            if (contentVo.getMid() != 0) {
                wrapper.eq("mid", contentVo.getMid());
            }
        }
        if (!ObjectUtil.isEmpty(contentVo.getUid())) {
            wrapper.eq("uid", contentVo.getUid());
        }
        if (!ObjectUtil.isEmpty(contentVo.getStatus())) {
            wrapper.eq("status", contentVo.getStatus());
        }

        if (!ObjectUtil.isEmpty(contentVo.getType())) {
            wrapper.eq("type", contentVo.getType());
        }
        if (!ObjectUtil.isEmpty(contentVo.getCtype())) {
            wrapper.eq("ctype", contentVo.getCtype());
        }

        if (!StringUtils.isEmpty(createTimeBegin)) {
            wrapper.ge("create_time", createTimeBegin);
        }
        if (!StringUtils.isEmpty(createTimeEnd)) {
            wrapper.le("create_time", createTimeEnd);
        }


        if (pageParam.getPages() < 1) {
            pageParam.setPages(1);
        }
        //调用mapper的方法
        IPage<Content> pages = baseMapper.selectPage(pageParam, wrapper);

        //设置其他参数
        pages.getRecords().stream().forEach(item -> {
            this.setContentName(item);
        });

        return pages;
    }


    @Override
    public IPage<Content> selectPage(Integer page) {
        Page<Content> pageParam = new Page<>(page, 10);

        ContentVo contentVo = new ContentVo();
        contentVo.setStatus(1);

        return this.selectPage(pageParam, contentVo);
    }

    @Override
    public List<Map<String, String>> queryContent(String content) {
        List<Map<String, String>> mapList = new ArrayList<>();
        QueryWrapper<Content> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1);
        wrapper.like("content", content);

        List<Content> contents = baseMapper.selectList(wrapper);
        if (ObjectUtil.isEmpty(contents)) {
            return null;
        }

        for (Content item : contents) {
            Map<String, String> map = new HashMap<>();
            map.put("label", item.getContent());
            map.put("value", item.getCid().toString());
            mapList.add(map);

        }
        return mapList;

    }


    //设置Content的其他参数
    private void setContentName(Content content) {

        SysUser sysUser = sysUserService.queryUser(content.getUid());
        if(ObjectUtil.isNotEmpty(sysUser)){
            content.getParam().put("uName", sysUser.getUserName());
            content.getParam().put("uMail", sysUser.getUserEmail());
            content.getParam().put("uImage", sysUser.getImageUrl());

        }
        content.getParam().put("mName", metaService.getById(content.getMid()).getName());

        content.getParam().put("cNums", fabulousService.queryNum(content.getCid()));
        if (sysUserService.isLogin()) {
            LoginUser loginUser = SecurityUtils.getLoginUser();
            SysUser user = loginUser.getUser();
            content.getParam().put("uIsZan", fabulousService.userIsZan(user.getUid(), content.getCid()));
//            if (content.getType() == 1) {
//                content.getParam().put("uName", "匿名用户");
//                content.getParam().put("uMail", "匿名邮箱");
//                content.getParam().put("uImage", "https://img0.baidu.com/it/u=3151858629,1834593008&fm=26&fmt=auto");
//            }
        } else {
            content.getParam().put("uIsZan", false);
        }

        //如果只是文字
        if (content.getCtype() == 0) {
            return;
        }
        //如果有 图片或视频
        List<Pictures> picturesList = pictureService.getByCid(content.getCid());
        if (ObjectUtil.isEmpty(picturesList)) {
            return;
        }
        List<String> urlList = new ArrayList<>();
        for (Pictures pictures : picturesList) {
            urlList.add(fileInfoService.getFileInfoUrl(pictures.getFileId()));
        }
        content.setPictureUrl(urlList);
    }

    @Override
    public Boolean addContent(Content content) {
        if (ObjectUtil.isEmpty(content.getUid())) {
            throw new CampusException("uid不能为空", 2001);
        }
        SysUser userInfo = sysUserService.queryUser(content.getUid());
        if (ObjectUtil.isEmpty(userInfo)) {
            throw new CampusException("用户不存在", 2001);
        }
        Meta meta = metaService.getById(content.getMid());
        if (ObjectUtil.isEmpty(meta)) {
            throw new CampusException("分类不存在", 2001);
        }
        boolean save = this.save(content);
        return save;
    }

    @Override
    public Boolean editContent(Content content) {
        Content newContent = this.queryContent(content);
        BeanUtil.copyProperties(content, newContent);
        return this.updateById(newContent);
    }

    @Override
    public void adoptByCids(List<Long> idList) {
        Content content = new Content();
        content.setStatus(1);
        UpdateWrapper wrapper = new UpdateWrapper();
        wrapper.in("cid", idList);
        baseMapper.update(content, wrapper);
    }

    @Override
    public Content getByCid(Long cid) {
        Content content = new Content();
        content.setCid(cid);
        content = this.queryContent(content);

        this.setContentName(content);
        return content;
    }

    @Override
    public void deleteContent(Content content) {
         this.delById(content.getCid());

    }

    @Override
    public void delByIds(List<Long> idList) {
        for (Long cid : idList) {
            this.delById(cid);
        }

    }

    @Override
    public void delById(Long cid) {
        //删除信息墙的评论
        commentService.delByCid(cid);
        //对应文件的操作
        fileInfoService.delContentFile(pictureService.getFileIdByCid(cid));
        //删除信息墙
        this.removeById(cid);
    }


    @Override
    public void delByUid(Long uid) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("uid", uid);
        List<Content> contents = baseMapper.selectByMap(map);
        for (Content content : contents) {
            this.delById(content.getCid());
        }

    }

    @Override
    public Content loginUserSeeOwnByCid(Content content) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();
        QueryWrapper<Content> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", user.getUid());
        wrapper.eq("cid", content.getCid());
        content = baseMapper.selectOne(wrapper);
        this.setContentName(content);

        return content;
    }

    private QueryWrapper creatQueryWrapper() {
        QueryWrapper<Content> queryWrapper = new QueryWrapper<>();

        queryWrapper.orderByAsc("status");
        // 根据时间倒序排列
        queryWrapper.orderByDesc("create_time");
        return queryWrapper;
    }

    /**
     * 获取发表内容信息
     */
    private Content queryContent(Content content) {
        content = this.getById(content.getCid());
        if (ObjectUtil.isEmpty(content)) {
            throw new CampusException("发表内容信息不存在", 2001);
        }
        return content;
    }
}
