import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class DisplayMetricsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        MetricsFile metricsFile = MetricsFile.getInstance();
        metricsFile.openFile();
        Metrics metrics = metricsFile.getMetrics();

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();

        writer.write("<h>Request Time Metrics</h>");
        writer.write("<p> Minimum" + "Request Time: " + metrics.getMinimumRequestTime());
        writer.write("<p> Maximum" + "Request Time: " + metrics.getMaximumRequestTime());
        writer.write("<p> Average" + "Request Time: " + metrics.getAverageRequestTime());
        writer.write("<p>");
        writer.write("<h>Response Size Metrics</h>");
        writer.write("<p> Minimum" + "Response Size: " + metrics.getMinimumResponseSize());
        writer.write("<p> Maximum" + "Response Size: " + metrics.getMaximumResponseSize());
        writer.write("<p> Average" + "Response Size: " + metrics.getAverageResponseSize());
        writer.write("<p>");
        writer.write("<p>");
        writer.write("<p> Number of requests processed by metric gathering extension: " + metrics.getCount());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }
}
