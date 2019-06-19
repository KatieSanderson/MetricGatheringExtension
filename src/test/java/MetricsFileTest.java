import org.junit.Assert;
import org.junit.Test;

public class MetricsFileTest {

    @Test
    public void getInstance_verifySingleton() {
        MetricsFile metricsFile = MetricsFile.getInstance();

        Assert.assertEquals(metricsFile, MetricsFile.getInstance());
    }
}