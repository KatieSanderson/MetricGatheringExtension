import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Collection;


@WebServlet(
        name = "metric-gather-servlet",
        urlPatterns = "/MetricGather"
)
public class MetricGatherServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestData processRequestData = new RequestData();
        Metrics historicalMetrics = new Metrics();
        processRequestData.setStartRequestProcess(System.currentTimeMillis());

        // get HttpRequestBody in bytes
        int requestSize = 0;
        int uniqueId = 0;
        Collection<Part> parts = req.getParts();

        processRequestData.setEndRequestProcess(System.currentTimeMillis());
        int requestTime = processRequestData.getRequestTime();

        historicalMetrics.getRequestTimeById().put(uniqueId, requestTime);
        historicalMetrics.getRequestSizeById().put(uniqueId, requestSize);
        historicalMetrics.addRequestMetrics(requestTime, requestSize);
    }
}
