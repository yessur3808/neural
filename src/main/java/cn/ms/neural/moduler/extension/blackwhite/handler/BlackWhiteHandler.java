package cn.ms.neural.moduler.extension.blackwhite.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ms.neural.Conf;
import cn.ms.neural.common.URL;
import cn.ms.neural.common.utils.StringUtils;
import cn.ms.neural.moduler.extension.blackwhite.conf.BlackWhiteConf;
import cn.ms.neural.moduler.extension.blackwhite.entity.BlackWhiteEntity;
import cn.ms.neural.moduler.extension.blackwhite.type.BlackWhiteType;

public class BlackWhiteHandler {

	public static Map<String,BlackWhiteEntity> getBlackWhiteEntityMap(URL  url){
		Map<String,BlackWhiteEntity> bwMap=new HashMap<String,BlackWhiteEntity>();
		String blacklistData=url.getModulerParameter(Conf.BLACKWHITE, BlackWhiteConf.BLACKWHITELIST_KEY, "");

		if(StringUtils.isNotBlank(blacklistData)){
			String[] blacklistArray=blacklistData.split(Conf.SEPARATOR);
			if(blacklistArray.length>0){
				for (String blacklist:blacklistArray) {
					try {
						if(StringUtils.isNotBlank(blacklist)){
							String[] array=blacklist.split(BlackWhiteConf.RES_KV_SEQ);
							if(array.length==4){
								BlackWhiteEntity entity=bwMap.get(array[3]);
								if(entity==null){
									entity=new BlackWhiteEntity();
									entity.setBwName(array[3]);
								}
								System.out.println(blacklist);
								boolean blackWhiteEnabled=Boolean.valueOf(array[2]);
								if(BlackWhiteType.BLACK.getVal().equals(array[1])){//黑名单收集
									List<String> blackData=new ArrayList<String>();
									if(blackWhiteEnabled){
										entity.setBlackEnabled(blackWhiteEnabled);
										if(!entity.getOnlineBlackData().isEmpty()){
											blackData.addAll(entity.getOnlineBlackData());		
										}
										blackData.add(array[0]);
										entity.setOnlineBlackData(blackData);	
									}else{
										if(!entity.getOfflineBlackData().isEmpty()){
											blackData.addAll(entity.getOfflineBlackData());		
										}
										blackData.add(array[0]);
										entity.setOfflineBlackData(blackData);										
									}
								}else if(BlackWhiteType.WHITE.getVal().equals(array[1])){//白名单收集
									List<String> whiteData=new ArrayList<String>();
									if(blackWhiteEnabled){
										entity.setWhiteEnabled(blackWhiteEnabled);
										if(!entity.getOnlineWhiteData().isEmpty()){
											whiteData.addAll(entity.getOnlineWhiteData());		
										}
										whiteData.add(array[0]);
										entity.setOnlineWhiteData(whiteData);	
									}else{
										if(!entity.getOfflineWhiteData().isEmpty()){
											whiteData.addAll(entity.getOfflineWhiteData());		
										}
										whiteData.add(array[0]);
										entity.setOfflineWhiteData(whiteData);										
									}
								}
								
								bwMap.put(array[3], entity);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		return bwMap;
	}
	
}
