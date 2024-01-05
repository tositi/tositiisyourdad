package com.oddfar.campus.service.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.oddfar.campus.model.campus.Comment;

import java.util.List;

/**
 * @author zhiyuan
 */
public interface CommentService extends IService<Comment> {

    void addComment(Comment comment);

    List<Comment> findByCid(long cid);

    //带条件的分页查询
    IPage<Comment> selectPage(Page<Comment> pageParam, Comment comment);

    void  delByCid(Long cid);

    void  delByUid(Long uid);

}
