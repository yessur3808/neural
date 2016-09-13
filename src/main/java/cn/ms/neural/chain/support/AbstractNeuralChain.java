package cn.ms.neural.chain.support;

import cn.ms.neural.chain.INeuralChain;
import cn.ms.neural.moduler.Moduler;

/**
 * 微服务神经元抽象类调用链
 * 
 * @author lry
 *
 * @param <REQ>
 * @param <RES>
 */
public abstract class AbstractNeuralChain<REQ, RES> implements INeuralChain<REQ, RES> {  
    
	public Moduler<REQ, RES> moduler;
	public INeuralChain<REQ, RES> neuralChain;  
    
	public AbstractNeuralChain(Moduler<REQ, RES> moduler) {
		this.moduler = moduler;
	}
    
    @Override
    public void setNeuralChain(INeuralChain<REQ, RES> neuralChain) {  
        this.neuralChain = neuralChain;  
    }  
      
} 