import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class MetricGatheringExtension implements AutoCloseable {

    private final Scanner scanner;
    private final BufferedReader bufferedReader;

    public MetricGatheringExtension(Scanner scanner, BufferedReader bufferedReader) {
        this.scanner = scanner;
        this.bufferedReader = bufferedReader;
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String file = "src\\main\\resources\\tempFile";
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(file)));
        try (MetricGatheringExtension mge = new MetricGatheringExtension(scanner, bufferedReader)) {
            Metrics historicalMetrics = new Metrics();

            String line;
            System.out.println("Input URL: ");
            while (!(line = scanner.nextLine()).equals("*")) {
                RequestData processRequestData = new RequestData();
                URL url = new URL(line);
                System.out.println("Setting start time to: " + System.currentTimeMillis());
                processRequestData.setStartRequestProcess(System.currentTimeMillis());
                URLConnection urlConnection = url.openConnection();
                System.out.println("Content length: " + urlConnection.getContentLengthLong());
                processRequestData.setRequestSize(urlConnection.getContentLengthLong());
                System.out.println("Setting end time to: " + System.currentTimeMillis());
                processRequestData.setEndRequestProcess(System.currentTimeMillis());

                historicalMetrics.addRequestMetrics(processRequestData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws Exception {
        scanner.close();
        bufferedReader.close();
    }
}
