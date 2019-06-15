import java.util.HashMap;
import java.util.Map;

/**
 * {@link Metrics} stores historical and performance metrics about requests and responses served by the application.
 * Times are provided in milliseconds. Sizes are provided in bytes.
 *
 * METRICS GATHERED
 * 1. Request time (milliseconds) - time spent between when the application starts to process the request and the time when the application sends the response to the client
 *     a. Maximum, minimum, and average request time of all requests
 *     b. Historical request time by unique identifier (HTTP header)
 * 2. Request size (bytes) - size of the HTTP request body in bytes
 *     a. Maximum, minimum, and average request size of all requests
 *     b. Historical request size by unique identifier (HTTP header)
 */

public class Metrics {

    private final Map<Integer, Integer> requestTimeById;
    private final Map<Integer, Integer> requestSizeById;

    private int maximumRequestTime;
    private int minimumRequestTime;
    private double averageRequestTime;
    private int maximumRequestSize;
    private int minimumRequestSize;
    private double averageRequestSize;
    private int count;

    Metrics() {
        requestTimeById = new HashMap<>();
        requestSizeById = new HashMap<>();
        minimumRequestTime = Integer.MAX_VALUE;
        minimumRequestSize = Integer.MAX_VALUE;
    }

    void addRequestMetrics(int requestTime, int requestSize) {
        maximumRequestTime = Math.max(maximumRequestTime, requestTime);
        minimumRequestTime = Math.min(minimumRequestTime, requestTime);
        averageRequestTime = (averageRequestTime * count + requestTime) / (count + 1);

        maximumRequestSize = Math.max(maximumRequestSize, requestSize);
        minimumRequestSize = Math.min(minimumRequestSize, requestSize);
        averageRequestSize = (averageRequestSize * count + requestSize) / (count + 1);

        count++;
    }

    Map<Integer, Integer> getRequestTimeById() {
        return requestTimeById;
    }

    Map<Integer, Integer> getRequestSizeById() {
        return requestSizeById;
    }

    public int getMaximumRequestTime() {
        return maximumRequestTime;
    }

    public int getMinimumRequestTime() {
        return minimumRequestTime;
    }

    public double getAverageRequestTime() {
        return averageRequestTime;
    }

    public int getMaximumRequestSize() {
        return maximumRequestSize;
    }

    public int getMinimumRequestSize() {
        return minimumRequestSize;
    }

    public double getAverageRequestSize() {
        return averageRequestSize;
    }

}
