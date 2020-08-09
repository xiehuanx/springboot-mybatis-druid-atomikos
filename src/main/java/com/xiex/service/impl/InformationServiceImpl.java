package com.xiex.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiex.entity.xiehuan.Information;
import com.xiex.mapper.xiehuan.InformationMapper;
import com.xiex.service.IInformationService;
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
public class InformationServiceImpl extends ServiceImpl<InformationMapper, Information> implements IInformationService {

}
