import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MetricsTest {

    private Metrics metrics;

    @Before
    public void setup() {
        metrics = new Metrics();
    }

    @Test
    public void addRequestMetrics_Initial() {
        metrics.addRequestMetrics(1, 1);

        testMetrics(1, 1, 1, 1, 1, 1);
    }

    @Test
    public void addRequestMetrics_NewMaximumRequestTime() {
        metrics.addRequestMetrics(0, 0);
        metrics.addRequestMetrics(1, 0);

        testMetrics(1, 0, 0, 0, 0, 0);
    }

    @Test
    public void addRequestMetrics_NewMinimumRequestTime() {
        metrics.addRequestMetrics(1, 0);
        metrics.addRequestMetrics(0, 0);

        testMetrics(1, 0, 0, 0, 0, 0);
    }

    @Test
    public void addRequestMetrics_NewAverageRequestTime() {
        metrics.addRequestMetrics(1, 0);
        metrics.addRequestMetrics(1, 0);

        testMetrics(1, 1, 1, 0, 0, 0);
    }

    @Test
    public void addRequestMetrics_NewMaximumRequestSize() {
        metrics.addRequestMetrics(0, 0);
        metrics.addRequestMetrics(0, 1);

        testMetrics(0, 0, 0, 1, 0, 0);
    }

    @Test
    public void addRequestMetrics_NewMinimumRequestSize() {
        metrics.addRequestMetrics(0, 1);
        metrics.addRequestMetrics(0, 0);

        testMetrics(0, 0, 0, 1, 0, 0);
    }

    @Test
    public void addRequestMetrics_NewAverageRequestSize() {
        metrics.addRequestMetrics(0, 1);
        metrics.addRequestMetrics(0, 1);

        testMetrics(0, 0, 0, 1, 1, 1);
    }

    @Test
    public void getRequestTimeById_Contains() {
        metrics.getRequestTimeById().put(1, 2);

        Assert.assertEquals(2, (int) metrics.getRequestTimeById().get(1));
    }

    @Test
    public void getRequestSizeById() {
        metrics.getRequestSizeById().put(1, 2);

        Assert.assertEquals(2, (int) metrics.getRequestSizeById().get(1));
    }

    private void testMetrics(int expectedMaximumRequestTime, int expectedMinimumRequestTime, int expectedAverageRequestTime,
                             int expectedMaximumRequestSize, int expectedMinimumRequestSize, int expectedAverageRequestSize) {
        Assert.assertEquals(expectedMaximumRequestTime, metrics.getMaximumRequestTime());
        Assert.assertEquals(expectedMinimumRequestTime, metrics.getMinimumRequestTime());
        Assert.assertEquals(expectedAverageRequestTime, metrics.getAverageRequestTime());
        Assert.assertEquals(expectedMaximumRequestSize, metrics.getMaximumRequestSize());
        Assert.assertEquals(expectedMinimumRequestSize, metrics.getMinimumRequestSize());
        Assert.assertEquals(expectedAverageRequestSize, metrics.getAverageRequestSize());
    }
}