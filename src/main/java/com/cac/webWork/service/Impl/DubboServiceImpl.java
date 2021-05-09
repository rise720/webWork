package com.cac.webWork.service.Impl;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

import com.cac.webWork.service.DubboService;

//@Service(version = "1.0.0",timeout = 10000,interfaceClass = DubboService.class)
//@Component
public class DubboServiceImpl implements DubboService{

	@Override
	public String returnMsg() {
		return "Hello";
	}

	@Override
	public String returnMsg(String msg) {
		
		return msg;
	}

	@Override
	public void showMsg() {
		System.out.print("hello");
		
	}

}
