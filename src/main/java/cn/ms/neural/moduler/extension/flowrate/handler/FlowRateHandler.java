package cn.ms.neural.moduler.extension.flowrate.handler;

import java.util.HashMap;
import java.util.Map;

import cn.ms.neural.common.URL;
import cn.ms.neural.common.utils.StringUtils;
import cn.ms.neural.moduler.conf.Conf;
import cn.ms.neural.moduler.extension.flowrate.conf.FlowRateConf;
import cn.ms.neural.moduler.extension.flowrate.entity.FlowRateEntity;
import cn.ms.neural.moduler.extension.flowrate.type.FlowRateType;

public class FlowRateHandler {

	public static Map<String,FlowRateEntity> getFlowRateEntityMap(URL  url){
		Map<String,FlowRateEntity> frMap=new HashMap<String,FlowRateEntity>();
		String flowratelistData=url.getModulerParameter(Conf.FLOWRATE, FlowRateConf.FLOWRATE_LIST_KEY, "");

		if(StringUtils.isNotBlank(flowratelistData)){
			String[] flowratelistArray=flowratelistData.split(Conf.SEPARATOR);
			if(flowratelistArray.length>0){
				for (String flowratelist:flowratelistArray) {
					try {
						if(StringUtils.isNotBlank(flowratelist)){
							String[] array=flowratelist.split(Conf.RES_KV_SEQ);
							if(array.length==7){
								FlowRateEntity entity=new FlowRateEntity();
								entity.setFlowRateType(FlowRateType.valueOf(array[0]));
								entity.setSubswitch(Boolean.valueOf(array[1]));
								entity.setPermits(Integer.valueOf(array[2]));
								entity.setWarmup(Double.valueOf(array[3]));
								entity.setAcquire(Boolean.valueOf(array[4]));
								entity.setTimeout(Long.valueOf(array[5]));
								entity.setFrkey(array[6]);
								
								frMap.put(entity.getFlowRateType().getVal()+Conf.ANT+entity.getFrkey(), entity);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		return frMap;
	}
	
}
