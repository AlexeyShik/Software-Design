import java.util.ArrayList;
import java.util.List;

public class MethodMetadata {

    private final String methodName;
    private final List<Double> methodCalls;

    public MethodMetadata(String methodName) {
        this.methodName = methodName;
        methodCalls = new ArrayList<>();
    }

    public void addMethodCall(double duration) {
        methodCalls.add(duration);
    }

    public MethodProfilingResult getProfilerResult() {
        int calls = 0;
        double meanDuration = 0;
        double maxDuration = 0;

        for (Double duration : methodCalls) {
            if (duration != null) {
                ++calls;
                meanDuration += duration;
                maxDuration = Math.max(maxDuration, duration);
            }
        }
        meanDuration /= calls;

        return new MethodProfilingResult(methodName, calls, meanDuration, maxDuration);
    }
}
