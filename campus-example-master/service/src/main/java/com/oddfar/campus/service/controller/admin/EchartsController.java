package com.oddfar.campus.service.controller.admin;

import com.oddfar.campus.common.result.Result;
import com.oddfar.campus.service.mapper.ContentMapper;
import com.oddfar.campus.service.service.*;
import com.oddfar.campus.service.service.impl.ContentServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhiyuan
 */
@RestController
@RequestMapping("/admin/echarts")
@Api(tags = "数据统计")
public class EchartsController {
    @Autowired
    ContentServiceImpl contentServiceImpl;
    @Autowired
    ContentService contentService;
    @Autowired
    SysUserService sysUserService;
    @Autowired
    CommentService commentService;
    @Autowired
    FabulousService fabulousService;

    @ApiOperation(value = "查询文章最近7天有效数据的发表数量")
    @GetMapping("getContentsNum")
    public Result getContentsNum() {
        ContentMapper baseMapper = contentServiceImpl.getBaseMapper();
        List<Map<String, Object>> contentsNum = baseMapper.getContentsNum();

        List<List<Object>> res = new ArrayList<>();
        List<Object> list1 = new ArrayList<>();
        List<Object> list2 = new ArrayList<>();
        for (Map<String, Object> item : contentsNum) {

            String createTime = item.get("createTime").toString().substring(5);
            list1.add(createTime);

            String num = item.get("num").toString();
            list2.add(num);
        }
        res.add(list1);
        res.add(list2);
        return Result.ok(res);
    }

    @ApiOperation(value = "查看每个分类的文章数")
    @GetMapping("getMetaContentNum")
    public Result getMetaContentNum() {
        ContentMapper baseMapper = contentServiceImpl.getBaseMapper();
        List<Map<String, Object>> metaContentNum = baseMapper.getMetaContentNum();

//        List<List<Object>> res = this.toList(metaContentNum, "name", "num");
        return Result.ok(metaContentNum);
    }

    private List<List<Object>> toList(List<Map<String, Object>> old, String key1, String key2) {
        List<List<Object>> res = new ArrayList<>();
        List<Object> list1 = new ArrayList<>();
        List<Object> list2 = new ArrayList<>();
        for (Map<String, Object> item : old) {

            String createTime = item.get(key1).toString().substring(5);
            list1.add(createTime);

            String num = item.get(key2).toString();
            list2.add(num);
        }
        res.add(list1);
        res.add(list2);

        return res;

    }

    @ApiOperation(value = "查看后台4个的总数")
    @GetMapping("getNums")
    public Result getNums() {
        int count_1 = sysUserService.count();
        int count_2 = commentService.count();
        int count_3 = contentService.count();
        int count_4 = fabulousService.count();

        List<Integer> res = new ArrayList<>();
        res.add(count_1);
        res.add(count_2);
        res.add(count_3);
        res.add(count_4);

        return Result.ok(res);

    }

}
