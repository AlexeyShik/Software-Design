import java.lang.reflect.InvocationTargetException;

public class Main {

    private static final String MAIN_METHOD = "main";
    private static final String ENV_VAR_PACKAGE = "packageToProfile";

    public static void main(String[] args) {
        if (args == null || args.length < 2 || args[0] == null || args[1] == null) {
            System.err.println("Expected 2 arguments: <name of application> <package to profile>");
            return;
        }

        String packageToProfile = args[1];
        System.setProperty(ENV_VAR_PACKAGE, packageToProfile);

        String applicationName = args[0];
        try {
            Class.forName(applicationName).getMethod(MAIN_METHOD, String[].class).invoke(null, new String[] {null});
            new ReportBuilder().buildReport(Profiler.getInstance().getProfilerResult());
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
            System.err.println("Cannot invoke target application");
            e.printStackTrace();
        }
    }
}
