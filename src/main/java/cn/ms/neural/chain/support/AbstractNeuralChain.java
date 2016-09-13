package cn.ms.neural.chain.support;

import cn.ms.neural.chain.INeuralChain;
import cn.ms.neural.moduler.Moduler;

public abstract class AbstractNeuralChain<REQ, RES> implements INeuralChain<REQ, RES> {  
    
	protected Moduler<REQ, RES> moduler;
	protected INeuralChain<REQ, RES> neuralChain;  
    
    
	public AbstractNeuralChain(Moduler<REQ, RES> moduler) {
		this.moduler = moduler;
	}
    
    @Override
    public void setNeuralChain(INeuralChain<REQ, RES> neuralChain) {  
        this.neuralChain = neuralChain;  
    }  
      
} 