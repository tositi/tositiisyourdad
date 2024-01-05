package com.oddfar.campus.service.controller.admin;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oddfar.campus.common.result.Result;
import com.oddfar.campus.model.campus.Content;
import com.oddfar.campus.model.entity.base.BaseRequest;
import com.oddfar.campus.model.vo.campus.ContentVo;
import com.oddfar.campus.model.vo.campus.FileInfoVo;
import com.oddfar.campus.service.api.InterfaceService;
import com.oddfar.campus.service.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author zhiyuan
 */
@RestController
@RequestMapping("/admin/content")
@Api(tags = "内容管理")
public class ContentSetController {
    @Autowired
    private ContentService contentService;
    @Autowired
    private InterfaceService interfaceService;
    @Autowired
    private PictureService pictureService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private FileInfoService fileInfoService;

    //条件查询带分页
    @ApiOperation(value = "分页查询")
    @PostMapping("{page}/{limit}")
    public Result list(@PathVariable Long page, @PathVariable Long limit,
                       @RequestBody(required = false) ContentVo contentVo) {
        Page<Content> pageParam = new Page<>(page, limit);
        IPage<Content> pageModel = contentService.selectPage(pageParam, contentVo);
        return Result.ok(pageModel);
    }

    @ApiOperation(value = "添加内容")
    @PostMapping("add")
    public Result add(@ApiParam(name = "ContentVo", value = "内容对象", required = true)
                      @RequestBody @Validated(ContentVo.add.class) ContentVo contentVo) {
        Content content = new Content();
        BeanUtil.copyProperties(contentVo, content);
        Boolean aBoolean = contentService.addContent(content);
        if (aBoolean) {
            return Result.ok(content.getCid().toString());
        } else {
            return Result.fail();
        }
    }

    @ApiOperation(value = "通过id逻辑删除")
    @ApiParam(name = "id", value = "id", required = true)
    @PostMapping("delete")
    public Result delete(@ApiParam(name = "ContentVo", value = "内容对象", required = true)
                         @RequestBody @Validated(ContentVo.delete.class) ContentVo contentVo) {
        Content content = new Content();
        BeanUtil.copyProperties(contentVo, content);

        contentService.deleteContent(content);

            return Result.ok();

    }

    @ApiOperation(value = "批量删除")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {
        contentService.delByIds(idList);

        return Result.ok();
    }

    @ApiOperation(value = "批量通过")
    @PostMapping("batchAdopt")
    public Result adoptRows(@RequestBody List<Long> idList) {
        contentService.adoptByCids(idList);
        return Result.ok();
    }

    @ApiOperation(value = "获取发表内容详情")
    @GetMapping("getContent/{cid}")
    public Result getContentSet(@PathVariable Long cid) {

        Content content = contentService.getByCid(cid);
        if (ObjectUtil.isEmpty(content)) {
            return Result.fail("此CID不存在");
        } else {
            return Result.ok(content);
        }

    }

    @ApiOperation(value = "更改内容")
    @PostMapping("edit")
    public Result edit(@RequestBody @Validated(BaseRequest.edit.class) ContentVo contentVo) {
        Content content = new Content();
        BeanUtil.copyProperties(contentVo, content);
        Boolean aBoolean = contentService.editContent(content);
        if (aBoolean) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @ApiOperation(value = "上传文件")
    @PostMapping("fileUpload")
    public Result fileUpload(MultipartFile file,
                             @Validated(ContentVo.upload.class) ContentVo contentVo,
                             HttpServletResponse response) {

        Boolean aBoolean = pictureService.isAddOk(contentVo);

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

            return Result.ok(fileInfoVo);
        } else {
            response.setStatus(500);
            contentService.deleteContent(content);
            return Result.fail("数据异常");
        }

    }
}
