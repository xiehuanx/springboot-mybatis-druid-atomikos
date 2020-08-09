package com.xiex.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiex.annotation.DataSource;
import com.xiex.entity.xiex.SysUser;
import com.xiex.mapper.xiex.SysUserMapper;
import com.xiex.service.ISysUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author XiaoBingBy
 * @since 2020-08-01
 */
@Service
@DataSource(name="plat")
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

}
