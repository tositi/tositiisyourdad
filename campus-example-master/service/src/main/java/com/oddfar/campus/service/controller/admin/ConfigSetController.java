package com.oddfar.campus.service.controller.admin;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.oddfar.campus.common.result.Result;
import com.oddfar.campus.model.campus.SysConfig;
import com.oddfar.campus.model.vo.campus.SysConfigVo;
import com.oddfar.campus.service.service.SysConfigService;
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
@RequestMapping("/admin/config")
@Api(tags = "config配置管理")
public class ConfigSetController {

    @Autowired
    private SysConfigService configService;


    @ApiOperation(value = "条件查询带分页")
    @PostMapping("{page}/{limit}")
    public Result list(@PathVariable Long page, @PathVariable Long limit,
                       @RequestBody(required = false) SysConfigVo sysConfigVo) {

        Page<SysConfig> pageParam = new Page<>(page, limit);
        IPage<SysConfig> iPage = configService.selectPage(pageParam, sysConfigVo);

        return Result.ok(iPage);
    }


    @ApiOperation(value = "添加配置")
    @PostMapping()
    public Result add(@ApiParam(name = "SysConfigVo", value = "SysConfigVo", required = true)
                      @RequestBody @Validated(SysConfigVo.add.class) SysConfigVo sysConfigVo) {
        SysConfig sysConfig = new SysConfig();
        BeanUtil.copyProperties(sysConfigVo, sysConfig);
        configService.addSysconfig(sysConfig);
        return Result.ok();

    }

    @ApiOperation(value = "删除配置")
    @DeleteMapping("/{configIds}")
    public Result del(@PathVariable Long[] configIds) {

        configService.deleteConfigByIds(configIds);
        return Result.ok();

    }



    @ApiOperation(value = "修改配置")
    @PutMapping()
    public Result update(@ApiParam(name = "DictVo", value = "DictVo", required = true)
                         @RequestBody @Validated(SysConfigVo.edit.class) SysConfigVo sysConfigVo) {
        SysConfig sysConfig = configService.selectById(sysConfigVo.getConfigId());
        BeanUtil.copyProperties(sysConfigVo, sysConfig);
        configService.updateConfig(sysConfig);
        return Result.ok();

    }

    @ApiOperation(value = "刷新参数缓存")
    @DeleteMapping("refreshCache")
    public Result refreshCache() {
        configService.resetConfigCache();
        return Result.ok();
    }

    /**
     * 根据参数编号获取详细信息
     */
    @ApiOperation(value = "根据参数编号configId获取详细信息")
    @GetMapping(value = "/{configId}")
    public Result getInfo(@PathVariable Long configId) {

        return Result.ok(configService.selectConfigById(configId));
    }


}
