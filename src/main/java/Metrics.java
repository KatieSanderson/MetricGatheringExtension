import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * <p>{@link Metrics} stores historical and performance metrics about requests and responses served by the indicated applications.</p>
 * <p>Times are provided in milliseconds. Sizes are provided in bytes.</p>
 *
 * <p>METRICS GATHERED</p>
 * <p>1. Request time (milliseconds) - time spent between when the application starts to process the request and the time when the application sends the response to the client</p>
 * <p>    a. Maximum, minimum, and average request time of all requests</p>
 * <p>    b. Historical request time by unique identifier (HTTP header)</p>
 * <p>2. Request size (bytes) - size of the HTTP request body in bytes</p>
 * <p>    a. Maximum, minimum, and average request size of all requests</p>
 * <p>    b. Historical request size by unique identifier (HTTP header)</p>
 */

class Metrics implements Serializable {

    private final Map<Integer, RequestData> requestDataMap;
    private int requestCount;

    private int maximumRequestTime;
    private int minimumRequestTime;
    private double averageRequestTime;

    private long maximumResponseSize;
    private long minimumResponseSize;
    private double averageResponseSize;

    Metrics() {
        requestDataMap = new HashMap<>();
        minimumRequestTime = Integer.MAX_VALUE;
        minimumResponseSize = Integer.MAX_VALUE;
    }

    void addRequestMetrics(RequestData requestData) {
        requestDataMap.put(requestData.getRequestId(), requestData);
        int requestTime = requestData.getRequestTime();
        long requestSize = requestData.getResponseSize();

        // calculate new global metrics
        maximumRequestTime = Math.max(maximumRequestTime, requestTime);
        minimumRequestTime = Math.min(minimumRequestTime, requestTime);
        averageRequestTime = (averageRequestTime * requestCount + requestTime) / (requestCount + 1);

        maximumResponseSize = Math.max(maximumResponseSize, requestSize);
        minimumResponseSize = Math.min(minimumResponseSize, requestSize);
        averageResponseSize = (averageResponseSize * requestCount + requestSize) / (requestCount + 1);

        requestCount++;
    }

    RequestData getRequestDataById(int id) {
        if (!requestDataMap.containsKey(id)) {
            throw new NoSuchElementException("No RequestData for ID: [" + id + "].");
        }
        return requestDataMap.get(id);
    }

    int getRequestCount() {
        return requestCount;
    }

    int getMaximumRequestTime() {
        return maximumRequestTime;
    }

    int getMinimumRequestTime() {
        return minimumRequestTime;
    }

    double getAverageRequestTime() {
        return averageRequestTime;
    }

    long getMaximumResponseSize() {
        return maximumResponseSize;
    }

    long getMinimumResponseSize() {
        return minimumResponseSize;
    }

    double getAverageResponseSize() {
        return averageResponseSize;
    }

}
