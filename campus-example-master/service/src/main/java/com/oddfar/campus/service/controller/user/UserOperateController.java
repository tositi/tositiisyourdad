package com.oddfar.campus.service.controller.user;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oddfar.campus.common.result.Result;
import com.oddfar.campus.model.campus.Comment;
import com.oddfar.campus.model.campus.Content;
import com.oddfar.campus.model.campus.SysUser;
import com.oddfar.campus.model.user.LoginUser;
import com.oddfar.campus.model.vo.campus.CommentVo;
import com.oddfar.campus.model.vo.campus.ContentQueryVo;
import com.oddfar.campus.model.vo.campus.ContentVo;
import com.oddfar.campus.model.vo.campus.FileInfoVo;
import com.oddfar.campus.service.api.InterfaceService;
import com.oddfar.campus.service.service.*;
import com.oddfar.campus.service.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @author zhiyuan
 */
@RestController
@RequestMapping("/api/operate")
@Api(tags = "用户API操作接口")
public class UserOperateController {
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private InterfaceService interfaceService;

    @Autowired
    private ContentService contentService;

    @Autowired
    private FabulousService fabulousService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private PictureService pictureService;

    @Autowired
    private FileInfoService fileInfoService;

    @ApiOperation(value = "上传头像")
    @PostMapping("headPortrait")
    public Result uploadHeadPortrait(MultipartFile file, HttpServletResponse response) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser loginUserInfo = loginUser.getUser();

        if (ObjectUtil.isEmpty(loginUserInfo)) {
            response.setStatus(500);
            return Result.fail("信息验证错误，请重新登录");
        }
        String contentType = file.getContentType();
        if (contentType.contains("image")) {
            //上传文件
            FileInfoVo fileInfoVo = fileInfoService.uploadHeadImageFile(loginUserInfo.getUid(), file);

            return Result.ok(fileInfoVo.getUrl());
        } else {
            response.setStatus(500);
            return Result.fail("只能上传图片");
        }

    }

    @ApiOperation(value = "添加内容")
    @PostMapping("addContent")
    public Result addContent(@ApiParam(name = "ContentVo", value = "内容对象", required = true)
                             @RequestBody @Validated(ContentVo.userAdd.class) ContentVo contentVo) {
        Content content = new Content();
        Long uid = SecurityUtils.getLoginUser().getUser().getUid();
        contentVo.setUid(uid);
        contentVo.setStatus(0);

        BeanUtil.copyProperties(contentVo, content);
        Boolean aBoolean = contentService.addContent(content);
        if (aBoolean) {
            return Result.ok(content.getCid().toString());
        } else {
            return Result.fail();
        }
    }

    @ApiOperation(value = "发表内容上传文件")
    @PostMapping("fileUpload")
    public Result fileUpload(MultipartFile file,
                             @Validated(ContentVo.upload.class) ContentVo contentVo,
                             HttpServletResponse response) {

        Boolean aBoolean = pictureService.isAddOk(contentVo);
        //设置uid为当前登录用户的user_id
        Long uid = SecurityUtils.getLoginUser().getUser().getUid();

        contentVo.setUid(uid);

        Content content = new Content();
        BeanUtil.copyProperties(contentVo, content);

        String contentType = file.getContentType();
        if (contentVo.getCtype() == 1) {
            if (!contentType.contains("image")) {
                response.setStatus(500);
                contentService.deleteContent(content);
                return Result.fail("只能上传图片");

            }
        }
        if (contentVo.getCtype() == 2) {
            if (!contentType.contains("video")) {
                response.setStatus(500);
                contentService.deleteContent(content);
                return Result.fail("只能上传视频");

            }
        }

        if (aBoolean) {
//            String timeUrl = new DateTime().toString("yyyy/MM/dd");
//            String uuid = UUID.randomUUID().toString();
            //上传文件
//            String url = interfaceService.FileAliYunUpload(file);
//            String url = interfaceService.FileLocalUpload(file, contentVo.getUid(), "content/" + timeUrl + "/" + uuid);
            //上传文件
            FileInfoVo fileInfoVo = fileInfoService.uploadContentFile(contentVo.getUid(), contentVo.getCid(), file);

            return Result.ok(fileInfoVo.getUrl());
        } else {
            response.setStatus(500);
            contentService.deleteContent(content);
            return Result.fail("数据异常");
        }

    }

    //条件查询带分页
    @ApiOperation(value = "查看自己发表的信息（已通过的）")
    @PostMapping("contents")
    public Result list(@RequestBody(required = false) ContentQueryVo contentQueryVo) {
        //@RequestBody(required = false) ContentVo contentVo

        ContentVo contentVo = new ContentVo();
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();
        //设置登录着Uid
        contentVo.setUid(user.getUid());
        contentVo.setStatus(1);//已通过的
        if (ObjectUtil.isEmpty(contentQueryVo)) {
            contentQueryVo = new ContentQueryVo();
            contentQueryVo.setMid(0L);
            contentQueryVo.setPage(1);
        }

        int page = contentQueryVo.getPage();
        Page<Content> pageParam = new Page<>(page, 10);

        Long mid = contentQueryVo.getMid();
        if (ObjectUtil.isNotEmpty(mid)) {
            contentVo.setMid(mid);
        }

        IPage<Content> pageModel = contentService.selectPage(pageParam, contentVo);
        return Result.ok(pageModel);
    }

    //条件查询带分页
    @ApiOperation(value = "查看自己发表的信息（全部）")
    @PostMapping("ownContents")
    public Result ownList(@RequestBody(required = false) ContentQueryVo contentQueryVo) {
        //@RequestBody(required = false) ContentVo contentVo

        ContentVo contentVo = new ContentVo();
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();
        //设置登录着Uid
        contentVo.setUid(user.getUid());
        if (ObjectUtil.isEmpty(contentQueryVo)) {
            contentQueryVo = new ContentQueryVo();
            contentQueryVo.setMid(0L);
            contentQueryVo.setPage(1);
        }

        int page = contentQueryVo.getPage();
        Page<Content> pageParam = new Page<>(page, 10);

        Long mid = contentQueryVo.getMid();
        if (ObjectUtil.isNotEmpty(mid)) {
            contentVo.setMid(mid);
        }

        IPage<Content> pageModel = contentService.selectPage(pageParam, contentVo);
        return Result.ok(pageModel);
    }

    //条件查询带分页
    @ApiOperation(value = "查看自己发表的评论")
    @PostMapping("ownComments")
    public Result ownComments(@RequestBody(required = false) JSONObject jsonObject) {

        Comment comment = new Comment();
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();
        //设置登录着Uid
        comment.setUid(user.getUid());

        int page = (int) jsonObject.get("page");
        Page<Comment> pageParam = new Page<>(page, 10);

        IPage<Comment> pageModel = commentService.selectPage(pageParam, comment);
        return Result.ok(pageModel);
    }


    //点赞
    @ApiOperation(value = "点赞")
    @PostMapping("zan/{cid}")
    public Result zan(@PathVariable Long cid) {
        return Result.ok(fabulousService.zan(cid));

    }

    @ApiOperation(value = "添加评论")
    @PostMapping("addComment")
    public Result addComment(@RequestBody @Validated(CommentVo.add.class) CommentVo commentVo) {

        Comment comment = new Comment();
        if (ObjectUtil.isNotEmpty(commentVo.getCoid())) {
            //设置双亲ID
            comment.setParentId(commentVo.getCoid());
        }
        Long cid = commentVo.getCid();
        comment.setCoContent(commentVo.getCoContent());
        comment.setCid(cid);
        Integer contentStatus = contentService.getByCid(cid).getStatus();

        if (contentStatus == 0) {
            return Result.fail().message("此信息在审核中，禁止评论");
        }
        if (contentStatus == 2) {
            return Result.fail().message("此信息已下架，禁止评论");
        }
        commentService.addComment(comment);
        return Result.ok("评论成功");
    }

    @ApiOperation(value = "删除发表评论")
    @PostMapping("delComment")
    public Result delComment(@RequestBody @Validated(CommentVo.delete.class) CommentVo commentVo) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();

        QueryWrapper<Comment> wrapper = new QueryWrapper();
        wrapper.eq("uid", user.getUid());
        wrapper.eq("coid", commentVo.getCoid());
        int count = commentService.count(wrapper);
        if (count >= 1) {
            commentService.removeById(commentVo.getCoid());
            return Result.ok("删除成功");
        } else {
            return Result.fail().message("coid不存在");

        }

    }

    @ApiOperation(value = "删除发表内容")
    @PostMapping("delContent")
    public Result delContent(@RequestBody @Validated(ContentQueryVo.delete.class) ContentQueryVo contentQueryVo) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();

        QueryWrapper<Content> wrapper = new QueryWrapper();
        wrapper.eq("uid", user.getUid());
        wrapper.eq("cid", contentQueryVo.getCid());
        int count = contentService.count(wrapper);
        if (count >= 1) {
            contentService.removeById(contentQueryVo.getCid());
            return Result.ok("删除成功");
        } else {
            return Result.fail().message("cid不存在");

        }

    }

}
