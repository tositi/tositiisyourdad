package com.oddfar.campus.service.controller.admin;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oddfar.campus.common.result.Result;
import com.oddfar.campus.model.campus.SysDict;
import com.oddfar.campus.model.vo.campus.SysDictVo;
import com.oddfar.campus.service.service.SysDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhiyuan
 */
@RestController
@RequestMapping("/admin/dict")
@Api(tags = "dict字典管理")
public class DictSetController {
    @Autowired
    private SysDictService sysDictService;

    @ApiOperation(value = "条件查询带分页")
    @PostMapping("{page}/{limit}")
    public Result list(@PathVariable Long page, @PathVariable Long limit,
                       @RequestBody(required = false) SysDictVo sysDictVo) {

        Page<SysDict> pageParam = new Page<>(page, limit);
        IPage<SysDict> iPage = sysDictService.selectPage(pageParam, sysDictVo);

        return Result.ok(iPage);
    }


    @ApiOperation(value = "根据dictId获取详细信息")
    @GetMapping(value = "/{dictId}")
    public Result getInfo(@PathVariable Long dictId) {
        return Result.ok(sysDictService.getById(dictId));
    }

    @ApiOperation(value = "修改字典")
    @PutMapping()
    public Result update(@ApiParam(name = "DictVo", value = "DictVo", required = true)
                         @RequestBody @Validated(SysDictVo.edit.class) SysDictVo sysDictVo) {
        SysDict sysDict = sysDictService.selectById(sysDictVo.getDictId());
        BeanUtil.copyProperties(sysDictVo, sysDict);
        sysDictService.updateDict(sysDict);
        return Result.ok();

    }

    @ApiOperation(value = "添加字典")
    @PostMapping()
    public Result add(@ApiParam(name = "DictVo", value = "DictVo", required = true)
                      @RequestBody @Validated(SysDictVo.add.class) SysDictVo sysDictVo) {
        SysDict sysDict = new SysDict();
        BeanUtil.copyProperties(sysDictVo, sysDict);
        sysDictService.addSysDict(sysDict);
        return Result.ok();

    }

    @ApiOperation(value = "删除字典")
    @DeleteMapping("/{dictIds}")
    public Result del(@PathVariable Long[] dictIds) {

        for (Long dictId : dictIds) {
            sysDictService.removeById(dictId);
        }

        return Result.ok();

    }
}
