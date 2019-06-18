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

        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        int ID = (new Random()).nextInt(Integer.MAX_VALUE);
        httpServletResponse.addHeader("ID", Integer.toString(ID));
        processRequestData.setRequestId(ID);
        processRequestData.setRequestSize(Long.parseLong(httpServletResponse.getHeader("Content-Length")));
        processRequestData.setEndRequestProcess(System.currentTimeMillis());
//        System.out.println("ID: " + processRequestData.getRequestId());
//        System.out.println("Size: " + processRequestData.getRequestSize());
//        System.out.println("Time: " + processRequestData.getRequestTime());


        String file = "metrics.txt";
        FileInputStream fileInputStream = null;
        Metrics metrics = new Metrics();
        try {
            fileInputStream = new FileInputStream(file);
            System.out.println("Found file");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            try {
                metrics = (Metrics) objectInputStream.readObject();
                System.out.println("Found metrics");
            } catch (ClassNotFoundException e) {
                System.out.println(e.getLocalizedMessage() + "Error reading [" + file + "].");
            }
            objectInputStream.close();
        } catch (FileNotFoundException e) {
            System.out.println( e.getLocalizedMessage() + "File: [" + file + "]. Will create new file to store metrics. ");
        }

        metrics.addRequestMetrics(processRequestData);

        FileOutputStream fileOutputStream = new FileOutputStream(file);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(metrics);
        objectOutputStream.flush();
        objectOutputStream.close();
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
