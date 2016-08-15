package cn.ms.neural.moduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.ms.neural.moduler.extension.blackwhite.IBlackWhite;
import cn.ms.neural.moduler.extension.degrade.IDegrade;
import cn.ms.neural.moduler.extension.echosound.IEchoSound;
import cn.ms.neural.moduler.extension.flowrate.IFlowRate;
import cn.ms.neural.moduler.extension.gracestop.IGraceStop;
import cn.ms.neural.moduler.extension.idempotent.Idempotent;
import cn.ms.neural.moduler.extension.pipescaling.IPipeScaling;
import cn.ms.neural.moduler.neure.INeure;

public enum ModulerType {

	/**
	 * 优雅停机
	 */
	GraceStop(0, IGraceStop.class),
	/**
	 * 黑白名单
	 */
	BlackWhite(1, IBlackWhite.class),
	/**
	 * 管道缩放
	 */
	PipeScaling(2, IPipeScaling.class),
	/**
	 * 流量控制
	 */
	FlowRate(3, IFlowRate.class),
	/**
	 * 服务降级
	 */
	Degrade(4, IDegrade.class),
	/**
	 * 幂等模块
	 */
	Idempotent(5, Idempotent.class),
	/**
	 * 回声探测
	 */
	EchoSound(6, IEchoSound.class),
	/**
	 * 容错内核
	 */
	Neure(7, INeure.class);

	Integer no;
	Class<?> clazz;
	static List<ModulerType> modulerTypes = new ArrayList<ModulerType>();

	ModulerType(int no, Class<?> clazz) {
		this.no = no;
		this.clazz = clazz;
	}

	public Integer getNo() {
		return no;
	}

	public void setNo(Integer no) {
		this.no = no;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public static List<ModulerType> getModulerTypes() {
		if (modulerTypes.isEmpty()) {
			for (ModulerType modulerType : values()) {
				modulerTypes.add(modulerType);
			}
			
			Collections.sort(modulerTypes, new Comparator<ModulerType>() {
				public int compare(ModulerType arg0, ModulerType arg1) {
					return arg0.getNo().compareTo(arg1.getNo());
				}
			});
		}

		return modulerTypes;
	}

}
