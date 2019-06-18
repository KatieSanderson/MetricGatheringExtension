import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class MetricsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String file = "metrics.txt";
        FileInputStream fileInputStream;
        Metrics metrics;
        try {
            fileInputStream = new FileInputStream(file);
            System.out.println("Found file");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            try {
                metrics = (Metrics) objectInputStream.readObject();
                System.out.println("Found metrics");
            } catch (ClassNotFoundException e) {
                System.out.println(e.getLocalizedMessage() + "Error reading [" + file + "]. Will create new (empty) historical metrics.");
                metrics = new Metrics();
            }
            objectInputStream.close();
        } catch (FileNotFoundException e) {
            System.out.println( e.getLocalizedMessage() + "File: [" + file + "]. Will create new file to store metrics. ");
            metrics = new Metrics();

        }

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();

        writer.write("<h>Request Time Metrics</h>");
        writer.write("<p> Minimum" + "Request Time: " + metrics.getMinimumRequestTime());
        writer.write("<p> Maximum" + "Request Time: " + metrics.getMaximumRequestTime());
        writer.write("<p> Average" + "Request Time: " + metrics.getAverageRequestTime());
        writer.write("<p></p>");
        writer.write("<h>Response Size Metrics</h>");
        writer.write("<p> Minimum" + "Response Size: " + metrics.getMinimumRequestSize());
        writer.write("<p> Maximum" + "Response Size: " + metrics.getMaximumRequestSize());
        writer.write("<p> Average" + "Response Size: " + metrics.getAverageRequestSize());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }
}
