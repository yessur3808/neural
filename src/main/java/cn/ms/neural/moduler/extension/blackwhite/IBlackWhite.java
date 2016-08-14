package cn.ms.neural.moduler.extension.blackwhite;

import java.util.Map;

import cn.ms.neural.common.exception.blackwhite.BlackWhiteListException;
import cn.ms.neural.moduler.IModuler;
import cn.ms.neural.moduler.extension.blackwhite.processor.IBlackWhiteProcessor;

/**
 * 黑白名单
 * <br>
 * 支持各类自定义的黑白名单过滤,如IP过滤、服务ID过滤、渠道ID过滤...
 * <br>
 * 黑白名单配置格式：[KEY字段]=>[黑/白名单类型:WHITE/BLACK]|[子开关:true/false]|[黑白名单值:VALUE]
 * <br>
 * ip=>WHITE|true|10.24.1.10,ip=>WHITE|true|10.24.1.10
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
