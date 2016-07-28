package cn.ms.neural.moduler.extension.blackwhite;

import java.util.Map;

import cn.ms.neural.common.exception.blackwhite.BlackWhiteListException;
import cn.ms.neural.moduler.IModuler;
import cn.ms.neural.moduler.extension.blackwhite.processor.IBlackWhiteProcessor;

/**
 * 黑白名单
 * <br>
 * 黑白名单配置格式：
 * blackwhite.switch=[总开关:true/false]&blackwhite.list=[值]:[黑白类型:WHITE/BLACK]:[开关:true/false]:[KEY字段][多规则之间使用英文逗号分隔:,]
 * <br>
 * http://127.0.0.1:8080/test/?blackwhite.switch=true&blackwhite.list=10.24.1.10:WHITE:true:ip,10.24.1.12:BLACK:true:ip
 * @author lry
 * @version v1.0
 */
public interface IBlackWhite<REQ, RES> extends IModuler<REQ, RES> {

	 /**
	  * 黑白名单控制
	  * 
	  * @param blackWhiteREQ
	  * @param blackWhiteIdKeyVals
	  * @param blackWhiteProcessor
	  * @param args
	  * @return
	  * @throws BlackWhiteListException
	  */
	 RES blackwhite(REQ blackWhiteREQ, Map<String, Object> blackWhiteIdKeyVals, IBlackWhiteProcessor<REQ, RES> blackWhiteProcessor, Object... args) throws BlackWhiteListException;
	
}
