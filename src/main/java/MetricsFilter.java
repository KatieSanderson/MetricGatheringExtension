import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        int ID = (new Random()).nextInt(Integer.MAX_VALUE);
        httpServletResponse.addHeader("ID", Integer.toString(ID));
        processRequestData.setStartRequestProcess(System.currentTimeMillis());

        // call actual web application
        filterChain.doFilter(servletRequest, httpServletResponse);

        // post-processing
        processRequestData.setEndRequestProcess(System.currentTimeMillis());
        processRequestData.setRequestId(ID);
        processRequestData.setResponseSize(Long.parseLong(httpServletResponse.getHeader("Content-Length")));

        // post-processing to update stored metrics
        MetricsFile.getInstance().updateFile(processRequestData);
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
