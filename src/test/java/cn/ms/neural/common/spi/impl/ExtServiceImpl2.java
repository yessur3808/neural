package cn.ms.neural.common.spi.impl;

import cn.ms.neural.common.spi.ExtService;

public class ExtServiceImpl2 implements ExtService {

	@Override
	public String echo() {
		return "This is ExtServiceImpl1 echo.";
	}

	@Override
	public String show(String str) {
		return str+", this is ExtServiceImpl1 show.";
	}

}
