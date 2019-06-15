import java.util.Date;

/**
 * {@link RequestData} stores the start and end {@link Date} for an in-process request. {@link RequestData #getRequestTime} calculates the request time.
 *
 * startRequestProcess: the time when the application starts to process the request
 * endRequestProcess: the time when the application sends the response to the client
 * requestTime: the difference between start and endRequestProcess
 */

class RequestData {

    private Date startRequestProcess;
    private Date endRequestProcess;
    private long requestSize;
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

    public void setStartRequestProcess(long startRequestProcess) {
        this.startRequestProcess = new Date(startRequestProcess);
    }

    public void setEndRequestProcess(long endRequestProcess) {
        this.endRequestProcess = new Date(endRequestProcess);
    }

    public long getRequestSize() {
        return requestSize;
    }

    public void setRequestSize(long requestSize) {
        this.requestSize = requestSize;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

}
