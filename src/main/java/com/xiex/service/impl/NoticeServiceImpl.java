package com.xiex.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiex.annotation.DataSource;
import com.xiex.entity.xiehuan.Notice;
import com.xiex.mapper.xiehuan.NoticeMapper;
import com.xiex.service.INoticeService;
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
@DataSource(name="bigdata")
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements INoticeService {

}
