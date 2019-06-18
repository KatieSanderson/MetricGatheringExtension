import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * {@link RequestData} stores the start and end {@link Date}, response size, and unique id for a request. {@link RequestData getRequestTime} calculates the request time.
 *
 * <p></p>
 * <p>startRequestProcess: the time when the application starts to process the request</p>
 * <p>endRequestProcess: the time when the application sends the response to the client</p>
 * <p>requestTime: the difference between start and endRequestProcess</p>
 * <p>responseSize: the size of the HTTP response (in bytes)</p>
 * <p>requestId: the unique ID given to request (accessible via HTTP header "id")</p>
 */

class RequestData implements Serializable {

    private Date startRequestProcess;
    private Date endRequestProcess;
    private long responseSize;
    private int requestId;

    /**
     * {@link RequestData #getRequestTime} returns the request time in int. This is acceptable as it is highly unlikely for request time to require beyond int's storage capacity.
     *
     * Request time is calculated by: endRequestProcess - startRequestProcess
     *
     * {@link NullPointerException} is thrown on null startRequestProcess or endRequestProcess. This prevents erroneous return values and properly diagnoses unexpected call (when start and endRequestProcess have not been set).
     *
     * @return Request time
     */

    int getRequestTime() {
        String startEndExceptionString = "startRequestProcess: [" + startRequestProcess + "], " + "endRequestProcess: [" + endRequestProcess + "]";
        try {
            int requestTime = (int) (endRequestProcess.getTime() - startRequestProcess.getTime());
            if (requestTime < 0) {
                throw new IllegalStateException("Error: negative requestTime [" + requestTime + "]. " + startEndExceptionString);
            }
            return requestTime;
        } catch (NullPointerException e) {
            throw new NullPointerException("Cannot calculate requestTime due to [" + null + "]. " + startEndExceptionString);
        }
    }

    void setStartRequestProcess(long startRequestProcess) {
        this.startRequestProcess = new Date(startRequestProcess);
    }

    void setEndRequestProcess(long endRequestProcess) {
        this.endRequestProcess = new Date(endRequestProcess);
    }

    long getResponseSize() {
        return responseSize;
    }

    void setResponseSize(long responseSize) {
        this.responseSize = responseSize;
    }

    int getRequestId() {
        return requestId;
    }

    void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RequestData that = (RequestData) o;
        return responseSize == that.responseSize &&
                requestId == that.requestId &&
                Objects.equals(startRequestProcess, that.startRequestProcess) &&
                Objects.equals(endRequestProcess, that.endRequestProcess);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startRequestProcess, endRequestProcess, responseSize, requestId);
    }
}
