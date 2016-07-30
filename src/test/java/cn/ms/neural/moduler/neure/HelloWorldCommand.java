package cn.ms.neural.moduler.neure;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixEventType;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import com.netflix.hystrix.exception.HystrixRuntimeException;

public class HelloWorldCommand extends HystrixCommand<String> {
	private final String name;

	@SuppressWarnings("deprecation")
	public HelloWorldCommand(String name) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("HelloWorldGroup"))
				.andCommandPropertiesDefaults(
						HystrixCommandProperties.Setter()
						.withExecutionIsolationThreadTimeoutInMilliseconds(500)));
		this.name = name;
	}
//
//	@Override
//	protected String getFallback(){
//		throw new NeuralException();
////		return "exeucute Falled";
//	}

	@Override
	protected String run() throws Exception {
//		throw new NeuralException("runshibail");
//		throw new HystrixBadRequestException("1111111");
		// sleep 1 秒,调用会超时
		TimeUnit.MILLISECONDS.sleep(1000);
		return "Hello " + name + " thread:" + Thread.currentThread().getName();
	}

	public static void main(String[] args) throws Exception {
		long s=System.currentTimeMillis();
		HelloWorldCommand command = new HelloWorldCommand("test-Fallback");
		try {
			String result = command.execute();
			System.out.println(result);
			
		}catch(HystrixBadRequestException bad){//不执行容错
			System.out.println("--->"+bad.getClass());
		}catch(HystrixRuntimeException hre){
			if(hre.getCause() instanceof TimeoutException){
				System.out.println("这是超时异常："+hre.getMessage());
			}else{
				System.out.println("22--->"+hre.getCause());
			}
			
			Throwable uoe=hre.getFallbackException();
			if(uoe!=null){
				if(uoe.getMessage().contains("No fallback available.")){
					System.out.println("No fallback available.");
				}
			}
			System.out.println("1--->"+hre.getCause());
			System.out.println("==="+hre.getFallbackException());
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("--->"+command.getExecutionException());
		System.out.println("--->"+command.getExecutionEvents());
		System.out.println("--->"+(System.currentTimeMillis()-s));
		System.out.println("--->"+(System.nanoTime()-command.getCommandRunStartTimeInNanos())/1000000);
		
		List<HystrixEventType> hystrixEventTypes=command.getExecutionEvents();
		HystrixEventType hystrixEventType=hystrixEventTypes.get(0);
		System.out.println(hystrixEventType.isTerminal());
		System.out.println(hystrixEventType.name());
		System.out.println(hystrixEventType.ordinal());
		
	}
}