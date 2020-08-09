package com.xiex.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiex.entity.xiehuan.Register;
import com.xiex.mapper.xiehuan.RegisterMapper;
import com.xiex.service.IRegisterService;
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
public class RegisterServiceImpl extends ServiceImpl<RegisterMapper, Register> implements IRegisterService {

}
