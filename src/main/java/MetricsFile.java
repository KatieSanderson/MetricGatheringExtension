import java.io.*;

/**
 * <p>{@link MetricsFile} is a Singleton, providing synchronized file access to stored {@link Metrics}.</p>
 *
 * <p>Singleton with synchronized file access ensures multiple threads cannot modify {@link Metrics} concurrently, which is de-serialized, modified, and re-serialized with each application request.</p>
 *
 * <p>Other storage implementations considered:</p>
 * <p>- Appending each request data without calculating maximum, minimum, and average or modifying {@link java.util.Map}</p>
 * <p>&nbsp* Potentially good alternative if requests for /metrics/* are few compared to requests for web applications with metric-gathering extension</p>
 * <p>&nbsp* Would reduce run time for requests for web applications with metric-gathering extension by removing de-serializing and re-serializing of {@link Metrics} data </p>
 * <p>&nbsp* Would increase run time when requesting {@link Metrics} data (/metrics/*); requires parsing of all {@link Metrics} data</p>
 * <p>- Calculating {@link Metrics} data at set intervals of requests</p>
 * <p>&nbsp* Would reduce run time of most requests for web applications with metric-gathering extension by removing de-serializing and re-serializing of {@link Metrics}</p>
 * <p>&nbsp* Would increase run time of some requests due to parsing of {@link Metrics} data</p>
 * <p>&nbsp* Would increase run time when requesting {@link Metrics} data (/metrics/*); requires parsing of all {@link Metrics} data, but less than previous option</p>
 * <p>- Storing {@link Metrics} data separate from {@link RequestData}, calculating stored {@link RequestData} data at set intervals to add to {@link Metrics} data</p>
 * <p>&nbsp* Would reduce run time of all requests for web applications with metric-gathering extension by removing de-serializing and re-serializing of {@link Metrics}</p>
 * <p>&nbsp* Would move calculations from live requests to server</p>
 * <p>&nbsp* Would reduce run time when requesting {@link Metrics} data (/metrics/*)</p>
 */

class MetricsFile {

    private static MetricsFile metricsFileInstance = null;
    private static String file = "metrics.txt";

    private MetricsFile() {}

    static MetricsFile getInstance() {
        if (metricsFileInstance == null) {
            metricsFileInstance = new MetricsFile();
        }
        return metricsFileInstance;
    }

    static Metrics readFile() {
        MetricsFile metricsFile = getInstance();
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file))){
            return (Metrics) objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            System.out.println(e.getLocalizedMessage() + "Error reading [" + file + "]. Will create new (empty) historical metrics.");
            return new Metrics();
        } catch (FileNotFoundException e) {
            System.out.println( e.getLocalizedMessage() + "File: [" + file + "]. Will create new file to store metrics.");
            return new Metrics();
        } catch (IOException e) {
            e.printStackTrace();
            return new Metrics();
        }
    }

    private void writeToFile(Metrics metrics) throws IOException {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file))) {
            objectOutputStream.writeObject(metrics);
            objectOutputStream.flush();
        }
    }

    synchronized void updateFile(RequestData requestData) throws IOException {
        Metrics metrics = readFile();
        metrics.addRequestMetrics(requestData);
        writeToFile(metrics);
    }

}
