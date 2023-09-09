public class MethodProfilingResult {

    private final String methodName;
    private final int calls;
    private final double meanDuration;
    private final double maxDuration;

    public MethodProfilingResult(String methodName, int calls, double meanDuration, double maxDuration) {
        this.methodName = methodName;
        this.calls = calls;
        this.meanDuration = meanDuration;
        this.maxDuration = maxDuration;
    }

    public String getMethodName() {
        return methodName;
    }

    public int getCalls() {
        return calls;
    }

    public double getMeanDuration() {
        return meanDuration;
    }

    public double getMaxDuration() {
        return maxDuration;
    }
}
