package cn.ms.neural.chain.support;

import cn.ms.neural.chain.NeuralChainHandler;
import cn.ms.neural.moduler.Moduler;

public abstract class AbstractNeuralChainHandler<REQ, RES> {  
    
    private NeuralChainHandler<REQ, RES> neuralChainHandler;  
    protected Moduler<REQ, RES> moduler;
	public AbstractNeuralChainHandler(Moduler<REQ, RES> moduler) {
		this.moduler = moduler;
	}
    
    public NeuralChainHandler<REQ, RES> getNeuralChainHandler() {  
        return neuralChainHandler;  
    }  
  
    public void setNeuralChainHandler(NeuralChainHandler<REQ, RES> neuralChainHandler) {  
        this.neuralChainHandler = neuralChainHandler;  
    }  
      
} 