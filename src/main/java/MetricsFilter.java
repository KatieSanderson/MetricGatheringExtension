import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Random;

/**
 * {@link MetricsFilter} is a filter for servlets specified in the web.xml filter mapping. It extends the web application by gathering metrics before and after the web application's service() call.
 */

public class MetricsFilter implements javax.servlet.Filter {

    private FilterConfig filterConfig;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // pre-processing
        RequestData processRequestData = new RequestData();
        MetricsFile metricsFile = MetricsFile.getInstance();
        metricsFile.openFile();
        Metrics metrics = metricsFile.getMetrics();
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        Random random = new Random();
        int ID = random.nextInt(Integer.MAX_VALUE);
        while (metrics.getRequestDataMap().containsKey(ID)) {
            ID = random.nextInt(Integer.MAX_VALUE);
        }
        httpServletResponse.addHeader("ID", Integer.toString(ID));
        processRequestData.setRequestId(ID);
        processRequestData.setStartRequestProcess(System.currentTimeMillis());

        // call actual web application
        filterChain.doFilter(servletRequest, httpServletResponse);

        // post-processing
        processRequestData.setResponseSize(Long.parseLong(httpServletResponse.getHeader("Content-Length")));
        processRequestData.setEndRequestProcess(System.currentTimeMillis());
        metrics.addRequestMetrics(processRequestData);
        metricsFile.writeMetricsToFile();
    }

    @Override
    public void init(final FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    @Override
    public void destroy() {
        filterConfig = null;
    }
}
