package com.sandeep.prototypes.person.circuitbreaker;

import com.netflix.hystrix.HystrixCommand;

public abstract class AbstractCircuitBreakerCommand {

  public static class MyHystrixRutimeException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    
    public MyHystrixRutimeException(Throwable e){
      super(e);
    }
    
    public Exception getFault(){
      return (Exception) this.getCause();
    }
  }
  
  public static class MyHystrixCommand extends HystrixCommand<String>{
    private final AbstractCircuitBreakerCommand myC;
    private final PhoenixFallbackHandler<String> h;
    protected MyHystrixCommand(AbstractCircuitBreakerCommand c, Setter setter, PhoenixFallbackHandler<String> h) {
      super(setter);
      this.myC = c;
      this.h = h;
    }

    @Override
    protected String run() throws Exception {
      return myC.execute();
    }

    @Override
    protected String getFallback() {
      if(h != null){
        h.getFallback();
      }
      throw new MyHystrixRutimeException(this.getFailedExecutionException().getCause());
    }
    
  }
  private final PhoenixFallbackHandler<String> handler;
  public AbstractCircuitBreakerCommand(String threadPoolName, PhoenixFallbackHandler<String> h){
    this.handler = h;
  }
  
  @Override
  public final String execute() {
    try{
      
    }catch(MyHystrixRutimeException e){
      throw e;
    }
    return null;
  }
  
  protected final <T> T call() throws Exception{
    MyHystrixCommand cx = new MyHystrixCommand(this, null, null);
    try{
      cx.execute();
    }catch(MyHystrixRutimeException e){
      throw e.getFault();
    }
    return null;
  }
}
