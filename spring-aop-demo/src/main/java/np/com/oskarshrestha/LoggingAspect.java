package np.com.oskarshrestha;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Before("execution(* np.com.oskarshrestha.ShoppingCart.checkout(..))")
    public void beforeLogger(JoinPoint jp){
//        System.out.println(jp.getSignature());
        String arg = jp.getArgs()[0].toString();
        System.out.println("Before logger with argument: "+arg);

    }

    // for any return type, for any package, for any class, if there is checkout method
    @After("execution(* *.*.*.*.checkout(..))")
    public void afterLogger(){
        System.out.println("After logger");
    }

    @Pointcut("execution(* np.com.oskarshrestha.quantity(..))")
    public void afterReturningPointCut(){}

    @AfterReturning(pointcut = "afterReturningPointCut()", returning = "retVal")
    public void afterReturning(String retVal){
        System.out.println("After returning :"+retVal);
    }

}

