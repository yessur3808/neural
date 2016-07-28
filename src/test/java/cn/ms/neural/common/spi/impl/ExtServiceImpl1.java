package cn.ms.neural.common.spi.impl;

import cn.ms.neural.common.spi.ExtService;
import cn.ms.neural.common.spi.SPI;

@SPI
public class ExtServiceImpl1 implements ExtService {

	@Override
	public String echo() {
		return "This is ExtServiceImpl1 echo.";
	}

	@Override
	public String show(String str) {
		return str+", this is ExtServiceImpl1 show.";
	}

}
