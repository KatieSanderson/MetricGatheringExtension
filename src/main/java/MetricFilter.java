import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Random;

public class MetricFilter implements javax.servlet.Filter {

    private FilterConfig filterConfig;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        RequestData processRequestData = new RequestData();
        processRequestData.setStartRequestProcess(System.currentTimeMillis());

        filterChain.doFilter(servletRequest, servletResponse);

        MetricsFile metricsFile = MetricsFile.getInstance();
        metricsFile.openMetricsFile();
        Metrics metrics = metricsFile.getMetrics();

        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        Random random = new Random();
        int ID = random.nextInt(Integer.MAX_VALUE);
        while (metrics.getRequestById().containsKey(ID)) {
            ID = random.nextInt(Integer.MAX_VALUE);
        }
        httpServletResponse.addHeader("ID", Integer.toString(ID));
        processRequestData.setRequestId(ID);
        processRequestData.setRequestSize(Long.parseLong(httpServletResponse.getHeader("Content-Length")));
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
