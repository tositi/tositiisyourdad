package com.oddfar.campus.service.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oddfar.campus.common.constant.Constants;
import com.oddfar.campus.common.exception.CampusException;
import com.oddfar.campus.common.redis.RedisCache;
import com.oddfar.campus.model.campus.SysConfig;
import com.oddfar.campus.model.vo.campus.SysConfigVo;
import com.oddfar.campus.service.expander.ConfigContainer;
import com.oddfar.campus.service.mapper.SysConfigMapper;
import com.oddfar.campus.service.service.SysConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * @author zhiyuan
 */
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements SysConfigService {

    @Autowired
    private RedisCache redisCache;

    /**
     * 项目启动时，初始化参数到缓存
     */
//    @PostConstruct
//    public void init() {
//        loadingConfigCache();
//    }

    /**
     * 加载参数缓存数据
     */
    @Override
    public void loadingConfigCache() {
        List<SysConfig> configsList = baseMapper.selectList(null);

        for (SysConfig config : configsList) {
            redisCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
            ConfigContainer.putConfig(config.getConfigKey(), config.getConfigValue());
        }


    }


    /**
     * 新增参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public int insertConfig(SysConfig config) {
        int row = baseMapper.insert(config);
        if (row > 0) {
            redisCache.setCacheObject(getCacheKey(config.getConfigKey()), config.getConfigValue());
            ConfigContainer.putConfig(config.getConfigKey(), config.getConfigValue());
        }
        return row;
    }

    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数key
     * @return 参数键值
     */
    @Override
    public String selectConfigByKey(String configKey) {
        String configValue = Convert.toStr(redisCache.getCacheObject(getCacheKey(configKey)));
        if (ObjectUtil.isNotEmpty(configValue)) {
            return configValue;
        }

        QueryWrapper<SysConfig> wrapper = new QueryWrapper<>();
        wrapper.eq("config_key", configKey);
        SysConfig retConfig = baseMapper.selectOne(wrapper);
        if (ObjectUtil.isNotNull(retConfig)) {
            redisCache.setCacheObject(getCacheKey(configKey), retConfig.getConfigValue());
            return retConfig.getConfigValue();
        }
        return StringUtils.EMPTY;
    }

    /**
     * 查询参数配置信息
     *
     * @param configId 参数配置ID
     * @return 参数配置信息
     */
    @Override
    public SysConfig selectConfigById(Long configId) {
        SysConfig config = new SysConfig();
        config.setConfigId(configId);
        return baseMapper.selectById(config);
    }

    /**
     * 清空参数缓存数据
     */
    @Override
    public void clearConfigCache() {
        Collection<String> keys = redisCache.keys(Constants.SYS_CONFIG_KEY + "*");
        redisCache.deleteObject(keys);
    }

    /**
     * 重置参数缓存数据
     */
    @Override
    public void resetConfigCache() {
        clearConfigCache();
        loadingConfigCache();
    }

    /**
     * 校验参数键名是否唯一
     *
     * @param config 参数配置信息
     * @return 结果 真为不存在
     */
    @Override
    public Boolean checkConfigKeyUnique(SysConfig config) {
        Long configId = ObjectUtil.isNull(config.getConfigId()) ? -1L : config.getConfigId();
        QueryWrapper<SysConfig> wrapper = new QueryWrapper<>();
        wrapper.eq("config_key", config.getConfigKey());
        SysConfig info = baseMapper.selectOne(wrapper);
        if (ObjectUtil.isNotNull(info) && info.getConfigId().longValue() != configId.longValue()) {
            return true;
        }
        return false;
    }

    @Override
    public void deleteConfigByIds(Long[] configIds) {
        for (Long configId : configIds)
        {
            SysConfig config = selectConfigById(configId);
            if (StringUtils.equals("Y", config.getConfigType()))
            {
                throw new CampusException(String.format("内置参数【%1$s】不能删除 ", config.getConfigKey()),2001);
            }
            this.removeById(configId);
            redisCache.deleteObject(getCacheKey(config.getConfigKey()));
            ConfigContainer.deleteConfig(config.getConfigKey());
        }
    }

    @Override
    public IPage<SysConfig> selectPage(Page<SysConfig> pageParam, SysConfig sysConfig) {

        SysConfigVo sysDictVo = new SysConfigVo();
        BeanUtil.copyProperties(sysConfig, sysDictVo);
        return this.selectPage(pageParam, sysConfig);

    }

    @Override
    public IPage<SysConfig> selectPage(Page<SysConfig> pageParam, SysConfigVo sysConfigVo) {
        QueryWrapper<SysConfig> wrapper = new QueryWrapper<>();

        wrapper.eq(ObjectUtil.isNotEmpty(sysConfigVo.getGroupCode()), "group_code", sysConfigVo.getGroupCode());
        wrapper.like(ObjectUtil.isNotEmpty(sysConfigVo.getConfigKey()), "config_key", sysConfigVo.getConfigKey());
        wrapper.like(ObjectUtil.isNotEmpty(sysConfigVo.getConfigName()), "config_name", sysConfigVo.getConfigName());

        //调用mapper的方法
        Page<SysConfig> commentPage = baseMapper.selectPage(pageParam, wrapper);

        return commentPage;
    }

    @Override
    public void addSysconfig(SysConfig sysConfig) {
        QueryWrapper<SysConfig> wrapper = new QueryWrapper<>();
        wrapper.eq("config_key", sysConfig.getConfigKey());
        Integer count = baseMapper.selectCount(wrapper);
        if (count > 0) {
            throw new CampusException("参数键名已存在", 2001);
        } else {
           this.insertConfig(sysConfig);
        }
    }

    @Override
    public SysConfig selectById(Long id) {
        SysConfig sysConfig = this.getById(id);
        if (ObjectUtil.isEmpty(sysConfig)) {
            throw new CampusException("configId不存在", 2001);
        } else {
            return sysConfig;
        }
    }

    @Override
    public void updateConfig(SysConfig sysConfig) {
        QueryWrapper<SysConfig> wrapper = new QueryWrapper<>();
        wrapper.ne("config_id", sysConfig.getConfigId());
        wrapper.eq("config_key", sysConfig.getConfigKey());
        Integer count = baseMapper.selectCount(wrapper);
        if (count > 0) {
            throw new CampusException("参数键名已存在", 2001);
        } else {
            this.updateById(sysConfig);
            redisCache.setCacheObject(getCacheKey(sysConfig.getConfigKey()), sysConfig.getConfigValue());
            ConfigContainer.putConfig(sysConfig.getConfigKey(), sysConfig.getConfigValue());
        }

    }

    private QueryWrapper creatQueryWrapper() {
        QueryWrapper<SysConfig> queryWrapper = new QueryWrapper<>();

        // 根据时间倒序排列
        queryWrapper.orderByDesc("create_time");
        return queryWrapper;
    }

    /**
     * 设置cache key
     *
     * @param configKey 参数键
     * @return 缓存键key
     */
    private String getCacheKey(String configKey) {
        return Constants.SYS_CONFIG_KEY + configKey;
    }
}
