import java.io.*;

class MetricsFile {

    private static MetricsFile metricsFileInstance = null;

    private Metrics metrics;
    private String file;

    private MetricsFile() {
        file = "metrics.txt";
    }

    static MetricsFile getInstance() {
        if (metricsFileInstance == null) {
            metricsFileInstance = new MetricsFile();
        }
        return metricsFileInstance;
    }

    synchronized void openMetricsFile() {
        FileInputStream fileInputStream;
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
        } catch (
                FileNotFoundException e) {
            System.out.println( e.getLocalizedMessage() + "File: [" + file + "]. Will create new file to store metrics. ");
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
