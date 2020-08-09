package com.xiex.controller;

import com.xiex.datasources.DynamicDataSource;
import com.xiex.entity.xiehuan.Notice;
import com.xiex.entity.xiex.SysUser;;
import com.xiex.service.INoticeService;
import com.xiex.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class TestController {

    //@Autowired
    private ISysUserService sysUserService;
    //@Autowired
    private INoticeService noticeService;


    // 使用构造函数注入
    public TestController(ISysUserService userService, INoticeService noticeService) {
        this.sysUserService = userService;
        this.noticeService = noticeService;
    }



    @GetMapping("/testTransFor")
    @Transactional
    public String testTranss() {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(12);
        sysUser.setLoginName("dsad");
        sysUser.setUserName("dsad");
        sysUser.setUserNickname("sadsa");
        sysUser.setUserPassword("dasdsa");
        sysUser.setUserPasswordHis("xzczxc");
        sysUser.setUserSalt("");
        sysUser.setUserEmail("");
        sysUser.setUserPhone("");
        sysUser.setUserAddress("");
        sysUser.setUserStatus("");
        sysUser.setCreateUser("");
        sysUser.setUpdateUser("");
        //bigdata
        DynamicDataSource.setDataSource("plat");
        sysUserService.save(sysUser);
        Notice notice = new Notice();
        notice.setContents("dsadsa");
        notice.setLevel("1");
        notice.setDate(LocalDateTime.now());
        notice.setStatus("1");
        notice.setStatusdate(LocalDateTime.now());
        DynamicDataSource.setDataSource("bigdata");
        noticeService.save(notice);
        int i =1/0;

        return "OK";

    }


    @GetMapping("/testTrans1")
    @Transactional
    public String testTrans1() {

    return null;
    }

    @GetMapping("/testTrans2")
    @Transactional
    public String testTrans2() {

return null;
    }
    @GetMapping("/getData1")
    public List<SysUser> getData1() {
        return sysUserService.list();
    }
    @GetMapping("/getData2")
    public List<Notice> getData2() {
        return noticeService.list();
    }
}