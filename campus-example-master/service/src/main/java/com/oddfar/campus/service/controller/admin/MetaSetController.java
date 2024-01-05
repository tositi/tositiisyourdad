package com.oddfar.campus.service.controller.admin;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oddfar.campus.common.exception.CampusException;
import com.oddfar.campus.common.result.Result;
import com.oddfar.campus.model.campus.Content;
import com.oddfar.campus.model.campus.Meta;
import com.oddfar.campus.model.vo.campus.MetaVo;
import com.oddfar.campus.service.service.ContentService;
import com.oddfar.campus.service.service.MetaService;
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
@RequestMapping("/admin/meta")
@Api(tags = "分类管理")
//@CrossOrigin
public class MetaSetController {
    @Autowired
    MetaService metaService;
    @Autowired
    ContentService contentService;

    @ApiOperation(value = "查询全部")
    @GetMapping("findAll")
    public Result listAll() {
        List<Meta> metaList = metaService.list();
        return Result.ok(metaList);
    }

    //条件查询带分页
    @ApiOperation(value = "分页查询")
    @PostMapping("{page}/{limit}")
    public Result list(@PathVariable Long page, @PathVariable Long limit,
                       @RequestBody(required = false) MetaVo metaVo) {
        Page<Meta> pageParam = new Page<>(page, limit);
        IPage<Meta> pageModel = metaService.selectPage(pageParam, metaVo);
        return Result.ok(pageModel);
    }

    @ApiOperation(value = "添加分类")
    @PostMapping("add")
    public Result add(@ApiParam(name = "MetaVo", value = "分类对象", required = true)
                      @RequestBody @Validated(MetaVo.add.class) MetaVo metaVo) {
        Meta meta = new Meta();
        BeanUtil.copyProperties(metaVo, meta);
        Boolean aBoolean = metaService.addMeta(meta);
        if (aBoolean) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @ApiOperation(value = "编辑分类")
    @PostMapping("edit")
    public Result edit(@ApiParam(name = "MetaVo", value = "分类对象", required = true)
                       @RequestBody @Validated(MetaVo.edit.class) MetaVo metaVo) {
        Meta meta = new Meta();
        BeanUtil.copyProperties(metaVo, meta);
        Boolean aBoolean = metaService.editMeta(meta);
        if (aBoolean) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @ApiOperation(value = "批量删除")
    @DeleteMapping("batchRemove")
    public Result batchRemoveHospitalSet(@RequestBody List<Long> idList) {
        metaService.removeByIds(idList);
        QueryWrapper<Content> wrapper = new QueryWrapper();
        wrapper.in("mid", idList);
        contentService.remove(wrapper);

        return Result.ok();
    }

    @ApiOperation(value = "详情")
    @GetMapping("/getMetaSet/{id}")
    public Result getMeta(@PathVariable Long id) {

        Meta meta = metaService.getById(id);
        if (ObjectUtil.isEmpty(meta)) {
            throw new CampusException("用户ID不存在", 2001);
        }
        return Result.ok(meta);
    }


    @ApiOperation(value = "通过id查看分类详情")
    @PostMapping("detail")
    public Result detail(@ApiParam(name = "MetaVo", value = "分类对象", required = true)
                         @RequestBody @Validated(MetaVo.detail.class) MetaVo metaVo) {
        Meta meta = new Meta();
        BeanUtil.copyProperties(metaVo, meta);

        return Result.ok(metaService.detailMeta(meta));
    }

    @ApiOperation(value = "通过id逻辑删除分类")
    @ApiParam(name = "id", value = "id", required = true)
    @PostMapping("delete")
    public Result delete(@ApiParam(name = "MetaVo", value = "分类对象", required = true)
                         @RequestBody @Validated(MetaVo.delete.class) MetaVo metaVo) {
        Meta meta = new Meta();
        BeanUtil.copyProperties(metaVo, meta);

        return Result.ok(metaService.deleteMeta(meta));

    }


}
