package cn.ms.neural.grayrouter;

public class RouterResult {

	private ResultType resultType;
	private String version;

	public RouterResult() {
	}

	public RouterResult(ResultType resultType) {
		this.resultType = resultType;
	}

	public RouterResult(ResultType resultType, String version) {
		this.resultType = resultType;
		this.version = version;
	}

	public ResultType getResultType() {
		return resultType;
	}

	public void setResultType(ResultType resultType) {
		this.resultType = resultType;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "RouterResult [resultType=" + resultType + ", version="
				+ version + "]";
	}

}
