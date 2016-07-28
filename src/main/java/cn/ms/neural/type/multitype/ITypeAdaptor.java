package cn.ms.neural.type.multitype;

import cn.ms.neural.type.IMsgAdaptor;

/**
 * The Adaptor Type.
 * 
 * @author lry
 */
public interface ITypeAdaptor extends IMsgAdaptor {

	/**
	 * Getter filter value.
	 * 
	 * @return
	 */
	String getVal();

	/**
	 * Setter filter value.
	 * 
	 * @param value
	 */
	void setVal(String value);

}
