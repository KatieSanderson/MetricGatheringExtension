import java.io.*;

/**
 * <p>{@link MetricsFile} is a Singleton, providing synchronized file access to stored {@link Metrics}.</p>
 *
 * <p>Singleton with synchronized file access ensures multiple threads cannot modify {@link Metrics} concurrently, which is de-serialized, modified, and re-serialized with each application request.</p>
 *
 * <p>Other storage implementations considered:</p>
 * <p>- Appending each request data without calculating maximum, minimum, and average or modifying {@link java.util.Map<Integer, RequestData>}</p>
 * <p>&nbsp* Potentially good alternative if requests for /metrics/* are few compared to requests for web applications with metric-gathering extension</p>
 * <p>&nbsp* Would reduce run time for requests for web applications with metric-gathering extension by removing de-serializing and re-serializing of {@link Metrics} data </p>
 * <p>&nbsp* Would increase run time when requesting {@link Metrics} data (/metrics/*); requires parsing of all {@link Metrics} data</p>
 * <p>- Calculating {@link Metrics} data at set intervals of requests</p>
 * <p>&nbsp* Would reduce run time of most requests for web applications with metric-gathering extension by removing de-serializing and re-serializing of {@link Metrics}</p>
 * <p>&nbsp* Would increase run time of some requests due to parsing of {@link Metrics} data</p>
 * <p>&nbsp* Would increase run time when requesting {@link Metrics} data (/metrics/*); requires parsing of all {@link Metrics} data, but less than previous option</p>
 */

class MetricsFile {

    private static MetricsFile metricsFileInstance = null;

    private Metrics metrics;
    private String file;

    private MetricsFile(String file) {
        this.file = file;
    }

    static MetricsFile getInstance() {
        if (metricsFileInstance == null) {
            metricsFileInstance = new MetricsFile("metrics.txt");
        }
        return metricsFileInstance;
    }

    synchronized void openFile() {
        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            try {
                metrics = (Metrics) objectInputStream.readObject();
            } catch (ClassNotFoundException e) {
                System.out.println(e.getLocalizedMessage() + "Error reading [" + file + "]. Will create new (empty) historical metrics.");
                metrics = new Metrics();
            }
            objectInputStream.close();
        } catch (
                FileNotFoundException e) {
            System.out.println( e.getLocalizedMessage() + "File: [" + file + "]. Will create new file to store metrics.");
            metrics = new Metrics();
        } catch (IOException e) {
            e.printStackTrace();
            metrics = new Metrics();
        }
    }

    void writeMetricsToFile() throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(metrics);
        objectOutputStream.flush();
        objectOutputStream.close();
    }

    Metrics getMetrics() {
        return metrics;
    }

}
