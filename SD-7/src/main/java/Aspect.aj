import org.aspectj.lang.Signature;

public aspect Aspect {

    pointcut executeMethod(): execution(* *(..))
        && if (thisJoinPointStaticPart
            .getSignature()
            .getDeclaringType()
            .getPackage()
            .toString()
            .startsWith("package " + System.getProperty("packageToProfile")));

    Object around(): executeMethod() {
        Signature s = thisEnclosingJoinPointStaticPart.getSignature();
        String methodName = s.getDeclaringTypeName() + "#" + s.getName();
        long startTime = System.nanoTime();
        Object result = proceed();
        Profiler.getInstance().addSample(methodName, (double) (System.nanoTime() - startTime) / 1000000.0);
        return result;
    }

}