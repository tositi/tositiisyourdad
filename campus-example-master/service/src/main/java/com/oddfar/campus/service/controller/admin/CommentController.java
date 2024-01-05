package com.oddfar.campus.service.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oddfar.campus.common.result.Result;
import com.oddfar.campus.model.campus.Comment;
import com.oddfar.campus.model.vo.campus.CommentVo;
import com.oddfar.campus.service.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhiyuan
 */
@RestController
@RequestMapping("/admin/comment")
@Api(tags = "评论管理")
public class CommentController {


    @Autowired
    private CommentService commentService;


    //条件查询带分页
    @ApiOperation(value = "分页查询")
    @PostMapping("{page}/{limit}")
    public Result list(@PathVariable Long page, @PathVariable Long limit,
                       @RequestBody(required = false) Comment comment) {
        Page<Comment> pageParam = new Page<>(page, limit);
        IPage<Comment> pageModel = commentService.selectPage(pageParam, comment);
        return Result.ok(pageModel);
    }

    @ApiOperation(value = "删除评论")
    @PostMapping("delete")
    public Result addComment(@ApiParam(name = "CommentVo", value = "内容对象", required = true)
                             @RequestBody @Validated(CommentVo.delete.class) CommentVo commentVo) {

        boolean b = commentService.removeById(commentVo.getCoid());
        return Result.ok(b);

    }

    @ApiOperation(value = "批量删除")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {
        boolean b = commentService.removeByIds(idList);
        if (b) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

}
