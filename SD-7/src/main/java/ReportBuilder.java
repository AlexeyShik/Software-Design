import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.List;

public class ReportBuilder {

    private static final Path REPORT_PATH = Path.of("./src/main/resources/report.html");

    public void buildReport(List<MethodProfilingResult> resultList) {
        DecimalFormat format = new DecimalFormat("0.00");

        StringBuilder builder = new StringBuilder()
            .append("<!DOCTYPE html>\n")
            .append("<html lang=\"en\">\n")
            .append("<head>\n")
            .append("\t<meta charset=\"UTF-8\">\n")
            .append("\t<title>Profile</title>\n")
            .append("</head>\n")
            .append("<body>\n")
            .append("\t<table>\n")
            .append("\t\t<tr>\n")
            .append("\t\t\t<td>methodName</td>\n")
            .append("\t\t\t<td>calls</td>\n")
            .append("\t\t\t<td>meanDuration</td>\n")
            .append("\t\t\t<td>maxDuration</td>\n")
            .append("\t\t</tr>\n");

        for (MethodProfilingResult result : resultList) {
            builder
                .append("\t\t<tr>\n")
                .append("\t\t\t<td>").append(result.getMethodName()).append("</td>\n")
                .append("\t\t\t<td>").append(result.getCalls()).append("</td>\n")
                .append("\t\t\t<td>").append(format.format(result.getMeanDuration())).append("</td>\n")
                .append("\t\t\t<td>").append(format.format(result.getMaxDuration())).append("</td>\n")
                .append("\t\t</tr>\n");
        }

        builder
            .append("\t</table>\n")
            .append("</body>\n")
            .append("</html>");

        try {
            Files.writeString(REPORT_PATH, builder.toString());
        } catch (IOException e) {
            System.err.println("Cannot write report to corresponding file: " + REPORT_PATH);
            e.printStackTrace();
        }
    }
}
