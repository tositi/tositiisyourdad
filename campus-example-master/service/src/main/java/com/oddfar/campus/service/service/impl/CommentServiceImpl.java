package com.oddfar.campus.service.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oddfar.campus.common.exception.CampusException;
import com.oddfar.campus.model.campus.Comment;
import com.oddfar.campus.model.campus.Content;
import com.oddfar.campus.model.campus.SysUser;
import com.oddfar.campus.model.user.LoginUser;
import com.oddfar.campus.service.mapper.CommentsMapper;
import com.oddfar.campus.service.service.CommentService;
import com.oddfar.campus.service.service.ContentService;
import com.oddfar.campus.service.service.SysUserService;
import com.oddfar.campus.service.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhiyuan
 */
@Service
@Slf4j
public class CommentServiceImpl extends ServiceImpl<CommentsMapper, Comment> implements CommentService {
    @Autowired
    SysUserService sysUserService;
    @Autowired
    ContentService contentService;

    @Override
    public void addComment(Comment comment) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();
        comment.setUid(user.getUid());
        Long parentId = comment.getParentId();

        //上级id无
        if (ObjectUtil.isEmpty(parentId)) {
            comment.setParentId(0L);
            this.save(comment);
        } else {
            this.queryComment(parentId);
            this.save(comment);
        }

    }

    @Override
    public List<Comment> findByCid(long cid) {

        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", 0);
        wrapper.eq("cid", cid);
        //先获取父评论
        List<Comment> comments = baseMapper.selectList(wrapper);
        if (ObjectUtil.isEmpty(comments)) {
            //如果没有父评论
            return null;
        }
        //有双亲评论时
        for (Comment comment : comments) {
            List<Comment> children = new ArrayList<>();
            comment.setChildren(children);
            this.findChild(comment.getChildren(), comment);
            //设置评论的其他参数，用户的头像和昵称
            Long uid = comment.getUid();
            SysUser userInfo = sysUserService.queryUser(uid);
            Map<String, Object> param = comment.getParam();
            param.put("uImage", userInfo.getImageUrl());
            param.put("uName", userInfo.getUserName());
        }

        return comments;
    }

    @Override
    public IPage<Comment> selectPage(Page<Comment> pageParam, Comment comment) {
        QueryWrapper<Comment> wrapper = this.creatQueryWrapper();

        wrapper.like(ObjectUtil.isNotEmpty(comment.getCoContent()), "co_content", comment.getCoContent());
        //调用mapper的方法
        Page<Comment> commentPage = baseMapper.selectPage(pageParam, wrapper);

        commentPage.getRecords().stream().forEach(item -> {
            //设置其他参数
            this.setOtherParam(item);
        });

        return commentPage;
    }

    @Override
    public void delByCid(Long cid) {

        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("cid", cid);
        this.remove(wrapper);

    }

    @Override
    public void delByUid(Long uid) {
        //把用户的评论删除
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", uid);
        this.remove(wrapper);

    }

    private void setOtherParam(Comment comment) {

        Long cid = comment.getCid();

        Content content = contentService.getByCid(cid);
        SysUser userInfo = sysUserService.queryUser(comment.getUid());

        comment.getParam().put("content", content.getContent());
        comment.getParam().put("uName", userInfo.getUserName());


    }

    private QueryWrapper creatQueryWrapper() {
        QueryWrapper<Content> queryWrapper = new QueryWrapper<>();

        // 根据时间倒序排列
        queryWrapper.orderByDesc("create_time");
        return queryWrapper;
    }

    private void findChild(List<Comment> children, Comment comment) {
        Long coid = comment.getCoid();
        //判断是否有孩子节点
        if (this.isHaveChild(coid)) {
            comment.setHasChildren(true);
            QueryWrapper<Comment> wrapper = new QueryWrapper<>();
            wrapper.eq("parent_id", coid);
            List<Comment> commentList = baseMapper.selectList(wrapper);
            for (Comment childComment : commentList) {
                //设置评论的其他参数，用户的头像和昵称
                Long uid = childComment.getUid();
                SysUser userInfo = sysUserService.queryUser(uid);
                Map<String, Object> param = childComment.getParam();
                param.put("uImage", userInfo.getImageUrl());
                param.put("uName", userInfo.getUserName());

                children.add(childComment);
                //递归添加
                findChild(children, childComment);
            }

        }

    }

    private Boolean isHaveChild(long coid) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", coid);

        Integer integer = baseMapper.selectCount(wrapper);
        if (integer > 0) {
            return true;
        } else {
            return false;
        }
    }

    private Comment queryComment(Long id) {
        Comment comment = baseMapper.selectById(id);
        if (ObjectUtil.isEmpty(comment)) {
            throw new CampusException("id不存在", 2001);
        }
        return comment;
    }

}
