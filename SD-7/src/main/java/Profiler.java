import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Profiler {

    private static final Profiler INSTANCE = new Profiler();
    private final Map<String, MethodMetadata> methodsMetadata;

    public static Profiler getInstance() {
        return INSTANCE;
    }

    private Profiler() {
        methodsMetadata = new ConcurrentHashMap<>();
    }

    public void addSample(String methodName, double duration) {
        methodsMetadata
            .computeIfAbsent(methodName, MethodMetadata::new)
            .addMethodCall(duration);
    }

    public List<MethodProfilingResult> getProfilerResult() {
        return methodsMetadata.values()
            .stream()
            .map(MethodMetadata::getProfilerResult)
            .collect(Collectors.toList());
    }
}
