package cn.ms.neural.moduler.extension.idempotent.conf;

public class IdempotentConf {
	
	private String idempotentId;
	/**
	 * 幂等机制开关
	 */
	private boolean idempotentEnable=false;
	/**
	 * 幂等数据过期周期
	 */
	private long expireCycle=1000*60*2;
	/**
	 * 幂等数据容量大小
	 */
	private int idempStorCapacity=10000;
	
	public String getIdempotentId() {
		return idempotentId;
	}
	public void setIdempotentId(String idempotentId) {
		this.idempotentId = idempotentId;
	}
	public boolean isIdempotentEnable() {
		return idempotentEnable;
	}
	public void setIdempotentEnable(boolean idempotentEnable) {
		this.idempotentEnable = idempotentEnable;
	}
	public long getExpireCycle() {
		return expireCycle;
	}
	public void setExpireCycle(long expireCycle) {
		this.expireCycle = expireCycle;
	}
	public int getIdempStorCapacity() {
		return idempStorCapacity;
	}
	public void setIdempStorCapacity(int idempStorCapacity) {
		this.idempStorCapacity = idempStorCapacity;
	}
	
}
