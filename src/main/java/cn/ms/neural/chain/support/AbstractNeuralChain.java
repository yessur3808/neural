package cn.ms.neural.chain.support;

import cn.ms.neural.chain.INeuralChain;
import cn.ms.neural.moduler.Moduler;

public abstract class AbstractNeuralChain<REQ, RES> {  
    
    private INeuralChain<REQ, RES> neuralChain;  
    protected Moduler<REQ, RES> moduler;
	public AbstractNeuralChain(Moduler<REQ, RES> moduler) {
		this.moduler = moduler;
	}
    
    public INeuralChain<REQ, RES> getNeuralChain() {  
        return neuralChain;  
    }  
  
    public void setNeuralChain(INeuralChain<REQ, RES> neuralChain) {  
        this.neuralChain = neuralChain;  
    }  
      
} 