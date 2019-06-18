import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;

public class MetricFilter implements javax.servlet.Filter {

    private FilterConfig filterConfig;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        RequestData processRequestData = new RequestData();
        processRequestData.setStartRequestProcess(System.currentTimeMillis());

        filterChain.doFilter(servletRequest, servletResponse);

        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        int ID = (new Random()).nextInt(Integer.MAX_VALUE);
        httpServletResponse.addHeader("ID", Double.toString(ID));
        processRequestData.setRequestId(ID);
        processRequestData.setRequestSize(Long.parseLong(httpServletResponse.getHeader("Content-Length")));
        processRequestData.setEndRequestProcess(System.currentTimeMillis());
        System.out.println("ID: " + processRequestData.getRequestId());
        System.out.println("Size: " + processRequestData.getRequestSize());
        System.out.println("Time: " + processRequestData.getRequestTime());
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
