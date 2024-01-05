package com.oddfar.campus.service.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.oddfar.campus.common.exception.CampusException;
import com.oddfar.campus.common.result.ResultCodeEnum;
import com.oddfar.campus.common.utils.HttpServletUtil;
import com.oddfar.campus.model.campus.Role;
import com.oddfar.campus.model.campus.SysUser;
import com.oddfar.campus.model.user.LoginUser;
import com.oddfar.campus.model.vo.user.RegisterVo;
import com.oddfar.campus.model.vo.user.SysUserVo;
import com.oddfar.campus.service.mapper.SysUserMapper;
import com.oddfar.campus.service.service.*;
import com.oddfar.campus.service.utils.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhiyuan
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService, UserDetailsService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private FileInfoService fileInfoService;
    @Autowired
    private SysConfigService configService;
    @Autowired
    private FabulousService fabulousService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private ContentService contentService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private SysUserRoleService userRoleService;
    @Autowired
    private SysUserOnlineService userOnlineService;

    /**
     * 实现UserDetailsService接口的方法，用于获取用户个人信息
     *
     * @param s 登录账号
     * @return
     */
    @Override
    public UserDetails loadUserByUsername(String s) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        if (s.contains("@")) {
            //邮箱登录
            wrapper.eq("user_email", s);
        } else {
            wrapper.eq("user_account", s);
        }
        SysUser user = baseMapper.selectOne(wrapper);
        if (ObjectUtil.isEmpty(user)) {
            throw new CampusException("账号或密码错误", 3001);
        }
        //如果账号被锁定
        if (user.getStatus() == 0) {
            throw new CampusException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }
        HttpServletRequest request = HttpServletUtil.getRequest();
        //获取来访 URL
        String origin = request.getHeader("origin");
        /**
         * TODO 待修改
         */
        if (ObjectUtil.isNotEmpty(origin)) {
            //用户前端默认部署的环境地址 的域名
            String userURl = configService.selectConfigByKey("sys.user.service.deploy.host");
            userURl = StringUtils.substringAfter(userURl, "//");
            //如果不是通过用户前端登录
            if (!origin.contains(userURl)) {
                if (user.getUserType() == 0) {
                    throw new CampusException("无权限登录", 3001);
                }
            }
        }

        List<GrantedAuthority> grantedAuthorities = this.getUserGrantedAuthority(user.getUid());
        return new User(s, user.getPassword(), grantedAuthorities);
    }

    /**
     * 获取用户权限，并把其添加到 GrantedAuthority 中
     *
     * @param uid
     * @return
     */
    public List<GrantedAuthority> getUserGrantedAuthority(Long uid) {
        List<Role> userRoles = sysUserMapper.getUserRolesById(uid);
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (Role role : userRoles) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_" + role.getKey());
            grantedAuthorities.add(grantedAuthority);
        }
        return grantedAuthorities;

    }

    @Override
    public Boolean add(SysUser sysUser) {
        //获取账号密码

        String password = sysUser.getPassword();

        //查询是否注册
        QueryWrapper<SysUser> queryWrapper = this.repeatAdd(sysUser);
        //如果注册
        if (!ObjectUtil.isEmpty(baseMapper.selectOne(queryWrapper))) {
            throw new CampusException(ResultCodeEnum.ACCOUNT_EXIST);
        }
        //加密密码
        sysUser.setPassword(this.BCrypt(password));
        //加入到数据库
        boolean save = this.save(sysUser);
        if (save) {
            userRoleService.addUserRole(sysUser);
        }


        return save;
    }

    @Override
    public Map<String, Object> register(RegisterVo registerVo) {

        SysUser sysUser = new SysUser();
        BeanUtil.copyProperties(registerVo, sysUser);
        //查询是否注册
        QueryWrapper<SysUser> queryWrapper = this.repeatAdd(sysUser);
        //如果注册
        if (!ObjectUtil.isEmpty(baseMapper.selectOne(queryWrapper))) {
            throw new CampusException(ResultCodeEnum.ACCOUNT_EXIST);
        }

        sysUser.setPassword(BCrypt(sysUser.getPassword()));
        sysUser.setUserType(0);
        //加入到数据库
        boolean save = this.save(sysUser);
        if (save) {
            return null;
        } else {
            return null;
        }

    }

    @Override
    public IPage<SysUser> selectPage(Page<SysUser> pageParam, SysUserVo sysUserVo) {
        //SysUserVo 获取条件值
        String name = sysUserVo.getUserName(); //用户名称
        String mail = sysUserVo.getUserEmail();//用户邮箱
        String account = sysUserVo.getUserAccount();//用户账号
        Integer status = sysUserVo.getStatus();//用户状态
        Integer userType = sysUserVo.getUserType();
        String createTimeBegin = sysUserVo.getCreateTimeBegin(); //开始时间
        String createTimeEnd = sysUserVo.getCreateTimeEnd(); //结束时间

        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();
        //创建查询wrapper
        QueryWrapper<SysUser> wrapper = this.creatQueryWrapper();
        wrapper.ne("uid", user.getUid());

        if (!ObjectUtil.isEmpty(name)) {
            wrapper.like("user_name", name);
        }
        if (!ObjectUtil.isEmpty(mail)) {
            wrapper.like("user_email", mail);
        }
        if (!ObjectUtil.isEmpty(account)) {
            wrapper.like("user_account", account);
        }

        if (!ObjectUtil.isEmpty(status)) {
            wrapper.eq("status", status);
        }

        if (!ObjectUtil.isEmpty(userType)) {
            wrapper.eq("user_type", userType);
        }

        if (!ObjectUtil.isEmpty(createTimeBegin)) {
            wrapper.ge("create_time", createTimeBegin);
        }
        if (!ObjectUtil.isEmpty(createTimeEnd)) {
            wrapper.le("create_time", createTimeEnd);
        }

        //调用mapper的方法
        IPage<SysUser> pages = baseMapper.selectPage(pageParam, wrapper);

        pages.getRecords().forEach(sysUser -> {
            sysUser.setImageUrl(this.getUserImageByAvatar(sysUser.getAvatar()));
        });

        return pages;
    }

    @Override
    public Boolean delByUid(Long uid) {
        //删除用户的评论
        commentService.delByUid(uid);
        //删除用户发表的信息墙
        contentService.delByUid(uid);
        //删除关于用户的点赞
        fabulousService.delByUid(uid);
        //删除用户权限
        userRoleService.delUserRole(uid);
        this.removeById(uid);
        //删除redis
        userOnlineService.delByUid(uid);

        return true;
    }

    @Override
    public Boolean delByIds(Long[] idList) {
        for (Long uid : idList) {
            this.delByUid(uid);
        }

        return true;
    }

    @Override
    public Boolean update(SysUser sysUser) {
        SysUser user = baseMapper.selectById(sysUser.getUid());
        if (!user.getUserEmail().equals(sysUser.getUserEmail())) {
            QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
            wrapper.eq("user_email", sysUser.getUserEmail());
            Integer integer = baseMapper.selectCount(wrapper);
            if (integer > 0) {
                throw new CampusException("更新失败，邮箱已存在！", 2001);
            }
        }

        if (!user.getUserName().equals(sysUser.getUserName())) {
            QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
            wrapper.eq("user_name", sysUser.getUserName());
            Integer integer = baseMapper.selectCount(wrapper);
            if (integer > 0) {
                throw new CampusException("更新失败，昵称已存在！", 2001);
            }
        }

        if (!user.getUserAccount().equals(sysUser.getUserAccount())) {
            QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
            wrapper.eq("user_account", sysUser.getUserAccount());
            Integer integer = baseMapper.selectCount(wrapper);
            if (integer > 0) {
                throw new CampusException("更新失败，账号已存在！", 2001);
            }
        }

        BeanUtil.copyProperties(sysUser, user);
        boolean b = this.updateById(user);
        if (b) {
            userRoleService.addUserRole(user);
        }
        //设置url
        user.setImageUrl(this.getUserImageByFileId(sysUser.getAvatar()));
        //缓存更新
        userOnlineService.updateUser(user);
        return b;
    }

    @Override
    public Boolean lock(Long userId, Integer status) {
        if (status.intValue() == 0 || status.intValue() == 1) {
            SysUser sysUser = this.getById(userId);
            sysUser.setStatus(status);
            this.updateById(sysUser);
            if (status == 0) {
                // 删除登录的redis
                userOnlineService.delByUid(userId);
            }
            return true;
        }
        return false;
    }

    @Override
    public SysUser getLoginUserInfo() {

        LoginUser loginUser = SecurityUtils.getLoginUser();
        //从数据库读取
        SysUser sysUser = this.queryUser(loginUser.getUserId());
        return sysUser;
    }

    @Override
    public SysUser getByAccount(String account) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        if (account.contains("@")) {
            wrapper.eq("user_email", account);
        } else {
            wrapper.eq("user_account", account);
        }
        SysUser sysUser = baseMapper.selectOne(wrapper);
        sysUser.setImageUrl(this.getUserImageByFileId(sysUser.getAvatar()));

        return sysUser;
    }

    @Override
    public List<String> getRolesByUid(Long uid) {
        List<Role> userRoles = sysUserMapper.getUserRolesById(uid);
        List<String> roles = new ArrayList<>();
        for (Role role : userRoles) {
            roles.add("ROLE_" + role.getKey());
        }

        return roles;
    }


    @Override
    public String getUserImageByUid(Long uid) {
        Long avatar = this.getById(uid).getAvatar();
        return this.getUserImageByAvatar(avatar);

    }

    @Override
    public String getUserImageByAvatar(Long avatar) {
        if (ObjectUtil.isNotEmpty(avatar)) {
            return fileInfoService.getFileInfoUrl(avatar);
        } else {
            return configService.selectConfigByKey("sys.user.default.image");
        }

    }


    @Override
    public SysUser queryUser(Long uid) {
        SysUser sysUser = baseMapper.selectById(uid);
        if (ObjectUtil.isEmpty(sysUser)) {
            throw new CampusException("uid不存在", 2001);
        }
        //设置头像URL
        sysUser.setImageUrl(this.getUserImageByFileId(sysUser.getAvatar()));
        return sysUser;
    }

    @Override
    public Boolean isLogin() {
        LoginUser loginUser = null;

        try {
            loginUser = SecurityUtils.getLoginUser();
            SysUser user = loginUser.getUser();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public Boolean isExist(SysUserVo sysUserVo) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper();

        wrapper.eq(ObjectUtil.isNotEmpty(sysUserVo.getUserEmail()), "user_email", sysUserVo.getUserEmail())
                .or()
                .eq(ObjectUtil.isNotEmpty(sysUserVo.getUserName()), "user_name", sysUserVo.getUserName())
                .or()
                .eq(ObjectUtil.isNotEmpty(sysUserVo.getUserAccount()), "user_account", sysUserVo.getUserAccount());

        Integer integer = baseMapper.selectCount(wrapper);
        if (integer >= 1) {
            return true;
        } else {
            return false;
        }
    }

    public String getUserImageByFileId(Long fileId) {
        if (ObjectUtil.isNotEmpty(fileId)) {
            return fileInfoService.getFileInfoUrl(fileId);
        } else {
            return configService.selectConfigByKey("sys.user.default.image");
        }

    }

    public SysUser queryUser(SysUser sysUser) {
        sysUser = this.queryUser(sysUser.getUid());
        return sysUser;
    }

    /**
     * 邮箱 昵称 账号 是否重复
     *
     * @param sysUser sysUser
     * @return QueryWrapper
     */
    public QueryWrapper<SysUser> repeatAdd(SysUser sysUser) {

        QueryWrapper<SysUser> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_email", sysUser.getUserEmail())
                .or().eq("user_name", sysUser.getUserName())
                .or().eq("user_account", sysUser.getUserAccount());

        return queryWrapper;
    }

    /**
     * 返回 BCrypt 加密的密码
     *
     * @param password
     * @return
     */
    public String BCrypt(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //加密后新密码
        String newPassword = passwordEncoder.encode(password);
        return newPassword;
    }

    private QueryWrapper creatQueryWrapper() {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        //不查密码
        queryWrapper.select(SysUser.class, info -> !info.getColumn().equals("password"));

        // 根据时间倒序排列
        queryWrapper.orderByDesc("create_time");
        return queryWrapper;
    }

}
