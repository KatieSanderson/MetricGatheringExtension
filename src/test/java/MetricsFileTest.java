import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MetricsFileTest {

    @Test
    public void getInstance_verifySingleton() {
        MetricsFile metricsFile = MetricsFile.getInstance();

        Assert.assertEquals(metricsFile, MetricsFile.getInstance());
    }
}