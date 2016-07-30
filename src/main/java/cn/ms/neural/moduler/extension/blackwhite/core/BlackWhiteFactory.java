package cn.ms.neural.moduler.extension.blackwhite.core;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.ms.neural.common.exception.blackwhite.BlackWhiteListException;
import cn.ms.neural.common.exception.blackwhite.black.BlackListException;
import cn.ms.neural.common.exception.blackwhite.white.WhiteListException;
import cn.ms.neural.common.logger.ILogger;
import cn.ms.neural.common.logger.LoggerManager;
import cn.ms.neural.moduler.Conf;
import cn.ms.neural.moduler.Moduler;
import cn.ms.neural.moduler.extension.blackwhite.IBlackWhite;
import cn.ms.neural.moduler.extension.blackwhite.conf.BlackWhiteConf;
import cn.ms.neural.moduler.extension.blackwhite.entity.BlackWhiteEntity;
import cn.ms.neural.moduler.extension.blackwhite.handler.BlackWhiteHandler;
import cn.ms.neural.moduler.extension.blackwhite.processor.IBlackWhiteProcessor;

/**
 * 黑白名单 <br>
 * 1.IP黑名单/白名单 2.服务黑名单/白名单 <br>
 * 
 * @author lry
 *
 * @param <REQ>
 * @param <RES>
 */
public class BlackWhiteFactory<REQ, RES> implements IBlackWhite<REQ, RES> {

	private static final ILogger bizDefaultLog = LoggerManager.getBizDefaultLog();
	
	/**
	 * 模块中心
	 */
	private Moduler<REQ, RES> moduler;
	boolean blackwhiteSwitch=false;
	boolean disableRecordSwitch=false;
	boolean visitRecordSwitch=false;
	
	/**
	 * 黑白名单清单
	 */
	private Map<String,BlackWhiteEntity> blackWhiteListMap;

	/**
	 * http://127.0.0.1:8080/service/?blackwhite.blacklist=172.18.0.1:true:ip,172.18.0.2:true:ip&blackwhite.whitelist=172.18.0.12:true:ip,172.18.0.21:true:ip
	 */
	@Override
	public void notify(Moduler<REQ, RES> moduler) {
		this.moduler=moduler;
		blackwhiteSwitch=this.moduler.getUrl().getModulerParameter(Conf.BLACKWHITE, BlackWhiteConf.SWITCH_KEY, BlackWhiteConf.SWITCH_DEF_VALUE);
		disableRecordSwitch=this.moduler.getUrl().getModulerParameter(Conf.BLACKWHITE, BlackWhiteConf.DISABLE_RECORD_SWITCH_KEY, BlackWhiteConf.DISABLE_RECORD_SWITCH_DEF_VALUE);
		visitRecordSwitch=this.moduler.getUrl().getModulerParameter(Conf.BLACKWHITE, BlackWhiteConf.VISIT_RECORD_SWITCH_KEY, BlackWhiteConf.VISIT_RECORD_SWITCH_DEF_VALUE);
		
		//变更更新内存数据
		blackWhiteListMap=BlackWhiteHandler.getBlackWhiteEntityMap(this.moduler.getUrl());
	}

	@Override
	public void init() throws Throwable {
		
	}

	@Override
	public RES blackwhite(REQ blackWhiteREQ, Map<String, Object> blackWhiteIdKeyVals, IBlackWhiteProcessor<REQ, RES> blackWhiteProcessor, Object... args) throws BlackWhiteListException{
		if (!blackwhiteSwitch) {// 开关未打开,则直接跳过
			if(disableRecordSwitch){//记录禁用
				if (bizDefaultLog.isWarnEnabled()) {
					bizDefaultLog.warn("黑白名单总开关被禁用了");
				}
			}

			return blackWhiteProcessor.processor(blackWhiteREQ);
		}
		
		for (Map.Entry<String, Object> entry:blackWhiteIdKeyVals.entrySet()) {//实时遍历过滤key-value
			BlackWhiteEntity blackWhiteList = blackWhiteListMap.get(entry.getKey());
			System.err.println(blackWhiteList.toString());
			//$NON-NLS-黑名单校验$
			if (!blackWhiteList.isBlackEnabled()) {// 校验开关
				if (bizDefaultLog.isWarnEnabled()) {
					bizDefaultLog.warn(String.format("[%s]黑名单的子开关被禁用了", entry.getKey()));
				}
			}else{
				if(visitRecordSwitch){//记录访问清单
					if (bizDefaultLog.isInfoEnabled()) {
						bizDefaultLog.info("[%s]黑名单的访问ID为[%s]", entry.getKey(), entry.getValue());
					}	
				}
				if (blackWhiteList.getOnlineBlackData().contains(String.valueOf(entry.getValue()))) {//[匹配一]全匹配
					String refusedMsg=String.format("[%s]黑名单拒绝ID为[%s]", entry.getKey(), entry.getValue());
					if (bizDefaultLog.isDebugEnabled()) {
						bizDefaultLog.debug(refusedMsg);
					}
					throw new BlackListException(refusedMsg);
				}else{//[匹配二]正则匹配
					for (String allowedIp:blackWhiteList.getOnlineBlackData()) {
						Pattern pattern=Pattern.compile(allowedIp);
						Matcher matcher=pattern.matcher(String.valueOf(entry.getValue()));
						if (matcher.find()) {
							String refusedMsg=String.format("[%s]黑名单拒绝ID为[%s]", entry.getKey(), entry.getValue());
							if (bizDefaultLog.isDebugEnabled()) {
								bizDefaultLog.debug(refusedMsg);
							}
							throw new BlackListException(refusedMsg);
						}
					}					
				}
			}
			
			//$NON-NLS-白名单校验$
			if(!blackWhiteList.isWhiteEnabled()){
				if (bizDefaultLog.isWarnEnabled()) {
					bizDefaultLog.warn(String.format("[%s]白名单的子开关被禁用了", entry.getKey()));
				}
				continue;
			}else{
				if(visitRecordSwitch){//记录访问清单
					if (bizDefaultLog.isInfoEnabled()) {
						bizDefaultLog.info("[%s]白名单的访问ID为[%s]", entry.getKey(), String.valueOf(entry.getValue()));
					}	
				}
				if (blackWhiteList.getOnlineWhiteData().contains(String.valueOf(entry.getValue()))) {//[匹配一]全匹配
					break;
				}else{//[匹配二]正则匹配
					for (String allowedIp:blackWhiteList.getOnlineWhiteData()) {
						Pattern pattern=Pattern.compile(allowedIp);
						Matcher matcher=pattern.matcher(String.valueOf(entry.getValue()));
						if (matcher.find()) {
							break;
						}
					}
				}
				
				//非白名单,则拒绝请求
				String refusedMsg=String.format("[%s]白名单拒绝ID为[%s]", entry.getKey(), entry.getValue());
				if (bizDefaultLog.isDebugEnabled()) {
					bizDefaultLog.debug(refusedMsg);
				}
				
				throw new WhiteListException(refusedMsg);
			}
		}

		return blackWhiteProcessor.processor(blackWhiteREQ);
	}

	@Override
	public void destory() throws Throwable {
		
	}

}
