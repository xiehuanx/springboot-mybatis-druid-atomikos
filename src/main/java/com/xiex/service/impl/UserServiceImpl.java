package com.xiex.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiex.entity.xiehuan.User;
import com.xiex.mapper.xiehuan.UserMapper;
import com.xiex.service.IUserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xiex
 * @since 2020-08-02
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
