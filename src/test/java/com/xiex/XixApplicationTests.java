package com.xiex;

import com.xiex.service.INoticeService;
import com.xiex.service.ISysUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class XixApplicationTests {

	private ISysUserService userService;

	private INoticeService noticeService;

	public XixApplicationTests(ISysUserService userService, INoticeService noticeService) {
		this.userService = userService;
		this.noticeService = noticeService;
	}

	@Test
	public void contextLoads() {
		String s ="";
		System.out.println(s.toLowerCase().contains("xie"));
	}

}
