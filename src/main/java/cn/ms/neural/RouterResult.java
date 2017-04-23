package cn.ms.neural;

public class RouterResult {

	private ResultType resultType;
	private String value;

	public RouterResult() {
	}

	public RouterResult(ResultType resultType) {
		this.resultType = resultType;
	}

	public RouterResult(ResultType resultType, String value) {
		this.resultType = resultType;
		this.value = value;
	}

	public ResultType getResultType() {
		return resultType;
	}

	public void setResultType(ResultType resultType) {
		this.resultType = resultType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "RouterResult [resultType=" + resultType + ", value=" + value
				+ "]";
	}

}
