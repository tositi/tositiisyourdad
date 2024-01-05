package com.oddfar.campus.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.oddfar.campus.model.campus.Content;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

/**
 * @author zhiyuan
 */
public interface ContentMapper extends BaseMapper<Content> {

    /**
     * 查询文章最近7天有效数据的发表数量
     *
     * @return
     */
    @MapKey("createTime")
    List<Map<String, Object>> getContentsNum();

    /**
     * 查看每个分类的文章数
     *
     * @return
     */
    @MapKey("name")
    List<Map<String, Object>> getMetaContentNum();


}
