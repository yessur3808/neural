package cn.ms.neural.moduler.extension.pipescaling.core;

import java.util.Random;

import cn.ms.neural.Conf;
import cn.ms.neural.common.exception.pipescaling.PipeScalingException;
import cn.ms.neural.common.spi.Adaptive;
import cn.ms.neural.moduler.Moduler;
import cn.ms.neural.moduler.extension.pipescaling.IPipeScaling;
import cn.ms.neural.moduler.extension.pipescaling.conf.PipeScalingConf;
import cn.ms.neural.moduler.extension.pipescaling.processor.IPipeScalingProcessor;

/**
 * 管道缩放
 * 
 * @author lry
 * @version v1.0
 */
@Adaptive
public class PipeScalingFactory<REQ, RES> implements IPipeScaling<REQ, RES> {
	
	private Moduler<REQ, RES> moduler;
	/**
	 * 缩放率计算
	 */
	private Random pipeScalingRandom=new Random();
	private boolean pipeScalingSwitch=false;
	private double pipeScalingRate=0.0000;
	

	@Override
	public void notify(Moduler<REQ, RES> moduler) {
		this.moduler=moduler;
		
		pipeScalingSwitch=this.moduler.getUrl().getModulerParameter(Conf.PIPESCALING, PipeScalingConf.SCALING_SWITCH_KEY, PipeScalingConf.SCALING_SWITCH_DEF_KEY);
		pipeScalingRate=this.moduler.getUrl().getModulerParameter(Conf.PIPESCALING, PipeScalingConf.SCALING_RATE_KEY, PipeScalingConf.SCALING_RATE_DEF_VAL);
	}

	@Override
	public void init() throws Throwable {
		
	}

	@Override
	public RES pipescaling(REQ req, IPipeScalingProcessor<REQ, RES> processor, Object... args) {
		if(!pipeScalingSwitch){//开关未开,则跳过校验器
			return processor.processor(req, args);
		}
		
		if(pipeScalingRandom.nextDouble()>pipeScalingRate){//放通率控制
			throw new PipeScalingException("The PipeScaling rate is " + "declined, the current rate(pipeScalingRate) is "+(pipeScalingRate*100)+"%.");
		}
		
		return processor.processor(req, args);
	}
	
	@Override
	public void destory() throws Throwable {
		
	}
	
}
