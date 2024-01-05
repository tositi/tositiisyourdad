package com.oddfar.campus.service.controller.tourist;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oddfar.campus.common.exception.CampusException;
import com.oddfar.campus.common.result.Result;
import com.oddfar.campus.model.campus.Content;
import com.oddfar.campus.model.campus.Meta;
import com.oddfar.campus.model.vo.campus.ContentQueryVo;
import com.oddfar.campus.model.vo.campus.ContentVo;
import com.oddfar.campus.model.vo.user.SysUserVo;
import com.oddfar.campus.service.service.CommentService;
import com.oddfar.campus.service.service.ContentService;
import com.oddfar.campus.service.service.MetaService;
import com.oddfar.campus.service.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author zhiyuan
 */
@RestController
@RequestMapping("/tourist")
@Api(tags = "游客路由")
public class TouristController {
    @Autowired
    MetaService metaService;
    @Autowired
    ContentService contentService;
    @Autowired
    CommentService commentService;
    @Autowired
    SysUserService sysUserService;

    @ApiOperation(value = "查询全部分类")
    @GetMapping("getMetas")
    public Result getMetas() {
        List<Meta> metaList = metaService.list();
        return Result.ok(metaList);
    }

    @ApiOperation(value = "模糊查询内容")
    @PostMapping("query")
    public Result query(@ApiParam(name = "ContentQueryVo", value = "内容查询对象", required = false)
                        @RequestBody(required = false) ContentQueryVo contentQueryVo) {
        List<Map<String, String>> mapList = contentService.queryContent(contentQueryVo.getContent());
        return Result.ok(mapList);
    }

    @ApiOperation(value = "分页查询内容列表")
    @PostMapping("contents")
//    @Cacheable(value = "content", keyGenerator = "keyGenerator")
    public Result getContentList(@ApiParam(name = "ContentQueryVo", value = "内容查询对象", required = false) @RequestBody(required = false) ContentQueryVo contentQueryVo, HttpServletRequest request) {
//        String getCid = request.getParameter("cid");
//
//        //如果是get请求，表示查看单个cid
//        if (request.getMethod().equals("GET")) {
//            if (ObjectUtil.isNotEmpty(getCid)) {
//                contentQueryVo = new ContentQueryVo();
//                contentQueryVo.setCid(Long.valueOf(getCid));
//            }
//            //如果是登录的查看自己发表的内容
//            Boolean sign = userInfoService.isSign();
//            if (sign) {
//                Content content = new Content();
//                content.setCid(Long.valueOf(getCid));
//                if (contentService.loginUserSeeOwnByCid(content)) {
//                    return Result.ok(content);
//                }
//            }
//        }

        ContentVo contentVo = new ContentVo();
        contentVo.setStatus(1);//已通过的
        if (ObjectUtil.isEmpty(contentQueryVo)) {
            contentQueryVo = new ContentQueryVo();
            contentQueryVo.setMid(0L);
            contentQueryVo.setPage(1);
        }

        int page = contentQueryVo.getPage();
        if (ObjectUtil.isEmpty(page)) {
            page = 1;
        }

        Page<Content> pageParam = new Page<>(page, 10);

        Long mid = contentQueryVo.getMid();
        if (ObjectUtil.isNotEmpty(mid)) {
            contentVo.setMid(mid);
        }

        Long cid = contentQueryVo.getCid();
        if (ObjectUtil.isNotEmpty(cid)) {
            contentVo.setCid(cid);
        }

        Long uid = contentQueryVo.getUid();
        if (ObjectUtil.isNotEmpty(uid)) {
            contentVo.setUid(uid);
        }

        IPage<Content> contentPage = contentService.selectPage(pageParam, contentVo);
        List<Content> records = contentPage.getRecords();
        for (Content item : records) {
            if (item.getType() == 1) {
                item.getParam().put("uName", "匿名用户");
                item.getParam().put("uMail", "匿名邮箱");
                item.getParam().put("uImage", "https://img0.baidu.com/it/u=3151858629,1834593008&fm=26&fmt=auto");
            }
        }

        return Result.ok(contentPage);
    }

    @ApiOperation(value = "查看指定cid的信息")
    @GetMapping("content/{cid}")
    public Result getContentByCid(@PathVariable Long cid) {

        //如果是登录的查看自己发表的内容
        Boolean sign = sysUserService.isLogin();
        if (sign) {
            Content content = new Content();
            content.setCid(Long.valueOf(cid));
            content = contentService.loginUserSeeOwnByCid(content);
            if (ObjectUtil.isNotEmpty(content)) {
                return Result.ok(content);
            }

        }
        //下面的是查看非自己的cid
        Content content = contentService.getByCid(cid);

        if (content.getStatus() != 1) {
            //如果查看的是没通过
            throw new CampusException("禁止查看", 2001);
        }

        //如果匿名
        if (content.getType() == 1) {
            content.getParam().put("uName", "匿名用户");
            content.getParam().put("uMail", "匿名邮箱");
            content.getParam().put("uImage", "https://img0.baidu.com/it/u=3151858629,1834593008&fm=26&fmt=auto");
        }


        return Result.ok(content);
    }

    @ApiOperation(value = "获取评论")
    @GetMapping("getComment/{cid}")
    public Result getComment(@PathVariable Long cid) {
        return Result.ok(commentService.findByCid(cid));

    }

    @ApiOperation(value = "判断 邮箱、昵称、账号 是否存在")
    @PostMapping("isExist")
    public Result isExist(@RequestBody(required = false) SysUserVo sysUserVo) {

        return Result.ok(sysUserService.isExist(sysUserVo));

    }
}
