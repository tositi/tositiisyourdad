package com.oddfar.campus.service.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.oddfar.campus.model.campus.Content;
import com.oddfar.campus.model.vo.campus.ContentVo;

import java.util.List;
import java.util.Map;

/**
 * 发表内容
 *
 * @author zhiyuan
 */
public interface ContentService extends IService<Content> {

    //带条件的分页查询
    IPage<Content> selectPage(Page<Content> pageParam, ContentVo contentVo);

    //size 为10查询分页
    IPage<Content> selectPage(Integer page);

    /**
     * 模糊查询
     * @param content 信息墙内容
     * @return
     */
    List<Map<String, String>> queryContent(String content);

    Boolean addContent(Content content);

    Boolean editContent(Content content);

    /**
     * 审核批量通过
     * @param idList cid列表
     */
    void adoptByCids(List<Long> idList);

    Content getByCid(Long cid);

    void deleteContent(Content content);

    void delByIds(List<Long> idList);

    void delById(Long cid);

    void delByUid(Long uid);

    /**
     * 通过 cid 查看 登录用户 的信息墙详细内容
     * @param content
     * @return
     */
    Content loginUserSeeOwnByCid(Content content);

}
