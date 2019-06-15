import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class MetricsTest {

    private Metrics metrics;

    @Mock
    RequestData requestData;

    @Before
    public void setup() {
        metrics = new Metrics();
    }

    @Test
    public void addRequestMetrics_Initial() {
        when(requestData.getRequestTime()).thenReturn(1);
        when(requestData.getRequestSize()).thenReturn(1L);

        metrics.addRequestMetrics(requestData);

        testMetrics(1, 1, 1, 1, 1, 1);
    }

    @Test
    public void addRequestMetrics_NewMaximumRequestTime() {
        callMockedRequest(0, 1, 0, 0);


        testMetrics(1, 0, 0.5, 0, 0, 0);
    }

    @Test
    public void addRequestMetrics_NewMinimumRequestTime() {
        callMockedRequest(1, 0, 0, 0);

        testMetrics(1, 0, 0.5, 0, 0, 0);
    }

    @Test
    public void addRequestMetrics_NewAverageRequestTime() {
        callMockedRequest(1, 1, 0, 0);

        testMetrics(1, 1, 1, 0, 0, 0);
    }

    @Test
    public void addRequestMetrics_NewMaximumRequestSize() {
        callMockedRequest(0, 0, 0, 1);

        testMetrics(0, 0, 0, 1, 0, 0.5);
    }

    @Test
    public void addRequestMetrics_NewMinimumRequestSize() {
        callMockedRequest(0, 0, 1, 0);

        testMetrics(0, 0, 0, 1, 0, 0.5);
    }

    @Test
    public void addRequestMetrics_NewAverageRequestSize() {
        callMockedRequest(0, 0, 1, 1);

        testMetrics(0, 0, 0, 1, 1, 1);
    }
//
//    @Test
//    public void getRequestTimeById_Contains() {
//        metrics.getRequestById().put(1, 2);
//
//        Assert.assertEquals(2, (int) metrics.getRequestById().get(1));
//    }
//
//    @Test
//    public void getRequestSizeById() {
//        metrics.getRequestSizeById().put(1, 2);
//
//        Assert.assertEquals(2, (int) metrics.getRequestSizeById().get(1));
//    }

    private void callMockedRequest(int firstTime, int secondTime, long firstSize, long secondSize) {
        when(requestData.getRequestTime()).thenReturn(firstTime).thenReturn(secondTime);
        when(requestData.getRequestSize()).thenReturn(firstSize).thenReturn(secondSize);

        metrics.addRequestMetrics(requestData);
        metrics.addRequestMetrics(requestData);
    }

    private void testMetrics(int expectedMaximumRequestTime, int expectedMinimumRequestTime, double expectedAverageRequestTime,
                             int expectedMaximumRequestSize, int expectedMinimumRequestSize, double expectedAverageRequestSize) {
        Assert.assertEquals(expectedMaximumRequestTime, metrics.getMaximumRequestTime());
        Assert.assertEquals(expectedMinimumRequestTime, metrics.getMinimumRequestTime());
        Assert.assertEquals(expectedAverageRequestTime, metrics.getAverageRequestTime(), 0.01);
        Assert.assertEquals(expectedMaximumRequestSize, metrics.getMaximumRequestSize());
        Assert.assertEquals(expectedMinimumRequestSize, metrics.getMinimumRequestSize());
        Assert.assertEquals(expectedAverageRequestSize, metrics.getAverageRequestSize(), 0.01);
    }
}