package cn.ms.neural.chain.support;

import cn.ms.neural.chain.INeuralChain;
import cn.ms.neural.moduler.Moduler;

public abstract class AbstractNeuralChain<REQ, RES> {  
    
    private INeuralChain<REQ, RES> neuralChainHandler;  
    protected Moduler<REQ, RES> moduler;
	public AbstractNeuralChain(Moduler<REQ, RES> moduler) {
		this.moduler = moduler;
	}
    
    public INeuralChain<REQ, RES> getNeuralChainHandler() {  
        return neuralChainHandler;  
    }  
  
    public void setNeuralChainHandler(INeuralChain<REQ, RES> neuralChainHandler) {  
        this.neuralChainHandler = neuralChainHandler;  
    }  
      
} 