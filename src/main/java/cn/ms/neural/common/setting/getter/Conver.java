package cn.ms.neural.common.setting.getter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import cn.ms.neural.common.utils.StringUtils;

/**
 * 类型转换器
 * 
 * @author lry
 */
public class Conver {
	
	public static String toStr(Object value, String defaultValue) {
		if(null == value) {
			return defaultValue;
		}
		if(value instanceof String) {
			return (String)value;
		}
		return value.toString();
	}
	
	public static Character toChar(Object value, Character defaultValue) {
		if(null == value) {
			return defaultValue;
		}
		if(value instanceof Character) {
			return (Character)value;
		}
		
		final String valueStr = toStr(value, null);
		return StringUtils.isEmpty(valueStr) ? defaultValue : valueStr.charAt(0);
	}

	public static Byte toByte(Object value, Byte defaultValue) {
		if (value == null){
			return defaultValue;
		}
		if(value instanceof Byte) {
			return (Byte)value;
		}
		if(value instanceof Number){
			return ((Number) value).byteValue();
		}
		final String valueStr = toStr(value, null);
		if (StringUtils.isBlank(valueStr)){
			return defaultValue;
		}
		try {
			return Byte.parseByte(valueStr);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	public static Short toShort(Object value, Short defaultValue) {
		if (value == null){
			return defaultValue;
		}
		if(value instanceof Short) {
			return (Short)value;
		}
		if(value instanceof Number){
			return ((Number) value).shortValue();
		}
		final String valueStr = toStr(value, null);
		if (StringUtils.isBlank(valueStr)){
			return defaultValue;
		}
		try {
			return Short.parseShort(valueStr.trim());
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static Number toNumber(Object value, Number defaultValue) {
		if (value == null){
			return defaultValue;
		}
		if(value instanceof Number) {
			return (Number)value;
		}
		final String valueStr = toStr(value, null);
		if (StringUtils.isBlank(valueStr)){
			return defaultValue;
		}
		try {
			return NumberFormat.getInstance().parse(valueStr);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	public static Integer toInt(Object value, Integer defaultValue) {
		if (value == null){
			return defaultValue;
		}
		if(value instanceof Integer) {
			return (Integer)value;
		}
		if(value instanceof Number){
			return ((Number) value).intValue();
		}
		final String valueStr = toStr(value, null);
		if (StringUtils.isBlank(valueStr)){
			return defaultValue;
		}
		try {
			return Integer.parseInt(valueStr.trim());
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	public static Integer[] toIntArray(boolean isIgnoreConvertError, Object... values) {
		if(values==null||values.length==0) {
			return new Integer[]{};
		}
		final Integer[] ints = new Integer[values.length];
		for(int i = 0; i < values.length; i++) {
			final Integer v = toInt(values[i], null);
			if(null == v && isIgnoreConvertError == false) {
				throw new RuntimeException(String.format("Convert [%s] to Integer error!", values[i]));
			}
			ints[i] = v;
		}
		return ints;
	}

	public static Long toLong(Object value, Long defaultValue) {
		if (value == null){
			return defaultValue;
		}
		if(value instanceof Long) {
			return (Long)value;
		}
		if(value instanceof Number){
			return ((Number) value).longValue();
		}
		final String valueStr = toStr(value, null);
		if (StringUtils.isBlank(valueStr)){
			return defaultValue;
		}
		try {
			//支持科学计数法
			return new BigDecimal(valueStr.trim()).longValue();
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	public static Long[] toLongArray(boolean isIgnoreConvertError, Object... values) {
		if(values==null||values.length==0) {
			return new Long[]{};
		}
		final Long[] longs = new Long[values.length];
		for(int i = 0; i < values.length; i++) {
			final Long v = toLong(values[i], null);
			if(null == v && isIgnoreConvertError == false) {
				throw new RuntimeException(String.format("Convert [%s] to Long error!", values[i]));
			}
			longs[i] = v;
		}
		return longs;
	}

	public static Double toDouble(Object value, Double defaultValue) {
		if (value == null){
			return defaultValue;
		}
		if(value instanceof Double) {
			return (Double)value;
		}
		if(value instanceof Number){
			return ((Number) value).doubleValue();
		}
		final String valueStr = toStr(value, null);
		if (StringUtils.isBlank(valueStr)){
			return defaultValue;
		}
		try {
			//支持科学计数法
			return new BigDecimal(valueStr.trim()).doubleValue();
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	public static Double[] toDoubleArray(boolean isIgnoreConvertError, Object... values) {
		if(values==null||values.length==0) {
			return new Double[]{};
		}
		final Double[] doubles = new Double[values.length];
		for(int i = 0; i < values.length; i++) {
			final Double v = toDouble(values[i], null);
			if(null == v && isIgnoreConvertError == false) {
				throw new RuntimeException(String.format("Convert [%s] to Double error!", values[i]));
			}
			doubles[i] = v;
		}
		return doubles;
	}
	
	public static Float toFloat(Object value, Float defaultValue) {
		if (value == null){
			return defaultValue;
		}
		if(value instanceof Float) {
			return (Float)value;
		}
		if(value instanceof Number){
			return ((Number) value).floatValue();
		}
		final String valueStr = toStr(value, null);
		if (StringUtils.isBlank(valueStr)){
			return defaultValue;
		}
		try {
			return Float.parseFloat(valueStr.trim());
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	public static <T> Float[] toFloatArray(boolean isIgnoreConvertError, Object... values) {
		if(values==null||values.length==0) {
			return new Float[]{};
		}
		final Float[] floats = new Float[values.length];
		for(int i = 0; i < values.length; i++) {
			final Float v = toFloat(values[i], null);
			if(null == v && isIgnoreConvertError == false) {
				throw new RuntimeException(String.format("Convert [%s] to Float error!", values[i]));
			}
			floats[i] = v;
		}
		return floats;
	}

	public static Boolean toBool(Object value, Boolean defaultValue) {
		if (value == null){
			return defaultValue;
		}
		if(value instanceof Boolean) {
			return (Boolean)value;
		}
		final String valueStr = toStr(value, null);
		if (StringUtils.isBlank(valueStr)){
			return defaultValue;
		}
		try {
			return Boolean.parseBoolean(valueStr.trim());
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	public static Boolean[] toBooleanArray(boolean isIgnoreConvertError, Object... values) {
		if(values==null||values.length==0) {
			return new Boolean[]{};
		}
		final Boolean[] bools = new Boolean[values.length];
		for(int i = 0; i < values.length; i++) {
			final Boolean v = toBool(values[i], null);
			if(null == v && isIgnoreConvertError == false) {
				throw new RuntimeException(String.format("Convert [%s] to Boolean error!", values[i]));
			}
			bools[i] = v;
		}
		return bools;
	}
	
	public static <E extends Enum<E>> E toEnum(Class<E> clazz, Object value, E defaultValue) {
		if (value == null){
			return defaultValue;
		}
		if (clazz.isAssignableFrom(value.getClass())) {
			@SuppressWarnings("unchecked")
			E myE = (E) value;
			return myE;
		}
		final String valueStr = toStr(value, null);
		if (StringUtils.isBlank(valueStr)){
			return defaultValue;
		}
		try {
			return Enum.valueOf(clazz,valueStr);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	public static BigInteger toBigInteger(Object value, BigInteger defaultValue) {
		if (value == null){
			return defaultValue;
		}
		if(value instanceof BigInteger) {
			return (BigInteger)value;
		}
		if(value instanceof Long) {
			return BigInteger.valueOf((Long)value);
		}
		final String valueStr = toStr(value, null);
		if (StringUtils.isBlank(valueStr)){
			return defaultValue;
		}
		try {
			return new BigInteger(valueStr);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	public static BigDecimal toBigDecimal(Object value, BigDecimal defaultValue) {
		if (value == null){
			return defaultValue;
		}
		if(value instanceof BigDecimal) {
			return (BigDecimal)value;
		}
		if(value instanceof Long) {
			return new BigDecimal((Long)value);
		}
		if(value instanceof Double) {
			return new BigDecimal((Double)value);
		}
		if(value instanceof Integer) {
			return new BigDecimal((Integer)value);
		}
		final String valueStr = toStr(value, null);
		if (StringUtils.isBlank(valueStr)){
			return defaultValue;
		}
		try {
			return new BigDecimal(valueStr);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static String toSBC(String input) {
		return toSBC(input, null);
	}
	
	public static String toSBC(String input, Set<Character> notConvertSet) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if(null != notConvertSet && notConvertSet.contains(c[i])) {
				//跳过不替换的字符
				continue;
			}
			
			if (c[i] == ' ') {
				c[i] = '\u3000';
			} else if (c[i] < '\177') {
				c[i] = (char) (c[i] + 65248);
			}
		}
		return new String(c);
	}

	public static String toDBC(String input) {
		return toDBC(input, null);
	}
	
	public static String toDBC(String text, Set<Character> notConvertSet) {
		char c[] = text.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if(null != notConvertSet && notConvertSet.contains(c[i])) {
				continue;
			}
			
			if (c[i] == '\u3000') {
				c[i] = ' ';
			} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
				c[i] = (char) (c[i] - 65248);
			}
		}
		String returnString = new String(c);
		
		return returnString;
	}
	
	public static String toHex(byte[] bytes) {
		final StringBuilder des = new StringBuilder();
		String tmp = null;
		for (int i = 0; i < bytes.length; i++) {
			tmp = (Integer.toHexString(bytes[i] & 0xFF));
			if (tmp.length() == 1) {
				des.append("0");
			}
			des.append(tmp);
		}
		return des.toString();
	}
	
	public static Object parse(Class<?> clazz, Object value) {
		try {
			return clazz.cast(value);
		} catch (ClassCastException e) {
			String valueStr = String.valueOf(value);
			
			Object result = parseBasic(clazz, valueStr);
			if(result != null) {
				return result;
			}
			
			if(Date.class.isAssignableFrom(clazz)) {
				try {
					return new SimpleDateFormat("yyyy-MM-dd").parse(valueStr);
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}else if(clazz == BigInteger.class){
				return new BigInteger(valueStr);
			}else if(clazz == BigDecimal.class) {
				return new BigDecimal(valueStr);
			}else if(clazz == byte[].class) {
				return valueStr.getBytes();
			}
			return value;
		}
	}
	
	public static Object parseBasic(Class<?> clazz, String valueStr) {
		if(null == clazz || null == valueStr) {
			return null;
		}
		if(clazz.isAssignableFrom(String.class)){
			return valueStr;
		}
		BasicType basicType = null;
		try {
			basicType = BasicType.valueOf(clazz.getSimpleName().toUpperCase());
		} catch (Exception e) {
			return null;
		}
		switch (basicType) {
			case BYTE:
				if(clazz == byte.class) {
					return Byte.parseByte(valueStr);
				}
				return Byte.valueOf(valueStr);
			case SHORT:
				if(clazz == short.class) {
					return Short.parseShort(valueStr);
				}
				return Short.valueOf(valueStr);
			case INT:
				return Integer.parseInt(valueStr);
			case INTEGER:
				return Integer.valueOf(valueStr);
			case LONG:
				if(clazz == long.class) {
					return new BigDecimal(valueStr).longValue();
				}
				return Long.valueOf(valueStr);
			case DOUBLE:
				if(clazz == double.class) {
					return new BigDecimal(valueStr).doubleValue();
				}
			case FLOAT:
				if(clazz == float.class) {
					return Float.parseFloat(valueStr);
				}
				return Float.valueOf(valueStr);
			case BOOLEAN:
				if(clazz == boolean.class) {
					return Boolean.parseBoolean(valueStr);
				}
				return Boolean.valueOf(valueStr);
			case CHAR:
				return valueStr.charAt(0);
			case CHARACTER:
				return Character.valueOf(valueStr.charAt(0));
			default:
				return null;
		}
	}
	
}
