import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.NoSuchElementException;

/**
 *  <p>{@link RequestMetricsServlet} displays metrics for a request specified by the web page's form</p>
 *
 *  <p>METRICS DISPLAYED</p>
 *  <p>1. Request time (milliseconds) - time spent between when the application starts to process the request and the time when the application sends the response to the client </p>
 *  <p>2. Response size (bytes) - size of the HTTP response body in bytes</p>
 */

public class RequestMetricsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Metrics metrics = MetricsFile.readFile();

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();

        writer.write("<h>Look up a historical request's metrics</h>");
        writer.write("<form>");
        writer.write("Request's unique ID:<br>");
        writer.write("<input type=\"text\" name=\"id\">");
        writer.write("<input type=\"submit\" value=\"Submit\">");
        writer.write("</form>");

        if (request.getParameter("id") != null) {
            Integer requestId = null;
            try {
                requestId = Integer.parseInt(request.getParameter("id"));
                writer.write("<br/><h>Data from request with id " + requestId + "</h>");
                RequestData requestData = metrics.getRequestDataById(requestId);
                writer.write("<p>Request Time: " + requestData.getRequestTime() + " milliseconds</p>");
                writer.write("<p>Request Size: " + requestData.getResponseSize() + " bytes</p>");
            } catch (NumberFormatException e) {
                writer.write("<p>Input must be a valid integer between 0 and " + Integer.MAX_VALUE + "</p>");
            } catch (NoSuchElementException e) {
                writer.write("<p> No request found in metrics with id " + requestId + "</p>");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }
}