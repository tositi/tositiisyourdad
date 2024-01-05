package com.oddfar.campus.service.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.oddfar.campus.model.campus.SysConfig;
import com.oddfar.campus.model.vo.campus.SysConfigVo;

/**
 * @author zhiyuan
 */
public interface SysConfigService extends IService<SysConfig> {

    /**
     * 加载参数缓存数据
     */
    public void loadingConfigCache();

    /**
     * 新增参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    public int insertConfig(SysConfig config);

    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数键名
     * @return 参数键值
     */
    public String selectConfigByKey(String configKey);

    /**
     * 查询参数配置信息
     *
     * @param configId 参数配置ID
     * @return 参数配置信息
     */
    public SysConfig selectConfigById(Long configId);

    /**
     * 清空参数缓存数据
     */
    public void clearConfigCache();

    /**
     * 重置参数缓存数据
     */
    public void resetConfigCache();

    /**
     * 校验参数键名是否唯一
     *
     * @param config 参数信息
     * @return 结果  真为不存在
     */
    public Boolean checkConfigKeyUnique(SysConfig config);

    /**
     * 批量删除参数信息
     *
     * @param configIds 需要删除的参数ID
     * @return 结果
     */
    public void deleteConfigByIds(Long[] configIds);

    //带条件的分页查询
    IPage<SysConfig> selectPage(Page<SysConfig> pageParam, SysConfig sysConfig);

    //带条件的分页查询
    IPage<SysConfig> selectPage(Page<SysConfig> pageParam, SysConfigVo sysConfigVo);

    /**
     * 添加 sysConfig
     * @param sysConfig sysConfig
     */
    void addSysconfig(SysConfig sysConfig);

    SysConfig selectById(Long id);

    void updateConfig(SysConfig sysConfig);
}
