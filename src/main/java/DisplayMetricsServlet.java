import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 *  <p>{@link DisplayMetricsServlet} displays historical metrics gathered by {@link MetricsFilter} on a web page.</p>
 *
 *  <p>METRICS DISPLAYED</p>
 *  <p>1. Request time (milliseconds) - time spent between when the application starts to process the request and the time when the application sends the response to the client </p>
 *  <p>    a. Maximum, minimum, and average request time of all requests</p>
 *  <p>2. Response size (bytes) - size of the HTTP response body in bytes</p>
 *  <p>    a. Maximum, minimum, and average request size of all requests</p>
 *  <p>3. Number of requests processed by metric gathering extension</p>
 */

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
        writer.write("<p> Number of requests processed by metric gathering extension: " + metrics.getRequestCount());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }
}
