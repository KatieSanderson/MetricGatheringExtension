import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.*;
import java.util.NoSuchElementException;

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
        when(requestData.getResponseSize()).thenReturn(1L);

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

    @Test (expected = NoSuchElementException.class)
    public void getRequestDataById_NoRequest() {
        metrics.getRequestDataById(1);
    }

    @Test
    public void whenSerializingAndDeserializing_ThenObjectIsTheSame() throws IOException {
        Metrics metrics = new Metrics();
        RequestData requestData = new RequestData();
        requestData.setStartRequestProcess(0);
        requestData.setEndRequestProcess(1);
        requestData.setResponseSize(1L);

        metrics.addRequestMetrics(requestData);

        String file = "test.txt";
        new File(file).delete();

        // write to file
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file))) {
            objectOutputStream.writeObject(metrics);
            objectOutputStream.flush();
        }

        // read from file
        Metrics inputMetrics;
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file))){
            inputMetrics = (Metrics) objectInputStream.readObject();
        } catch (
                FileNotFoundException e) {
            System.out.println( e.getLocalizedMessage() + "File: [" + file + "]. Will create new file to store metrics. ");
            inputMetrics = new Metrics();
        } catch (IOException e) {
            e.printStackTrace();
            inputMetrics = new Metrics();
        } catch (ClassNotFoundException e) {
            System.out.println(e.getLocalizedMessage() + "Error reading [" + file + "]. Will create new (empty) historical metrics.");
            inputMetrics = new Metrics();
        }
        new File(file).delete();

        Assert.assertEquals(metrics.getMaximumRequestTime(), inputMetrics.getMaximumRequestTime());
        Assert.assertEquals(metrics.getMinimumRequestTime(), inputMetrics.getMinimumRequestTime());
        Assert.assertEquals(metrics.getAverageRequestTime(), inputMetrics.getAverageRequestTime(), 0.01);
        Assert.assertEquals(metrics.getMaximumResponseSize(), inputMetrics.getMaximumResponseSize());
        Assert.assertEquals(metrics.getMinimumResponseSize(), inputMetrics.getMaximumResponseSize());
        Assert.assertEquals(metrics.getAverageResponseSize(), inputMetrics.getAverageResponseSize(), 0.01);
    }

    private void callMockedRequest(int firstTime, int secondTime, long firstSize, long secondSize) {
        when(requestData.getRequestTime()).thenReturn(firstTime).thenReturn(secondTime);
        when(requestData.getResponseSize()).thenReturn(firstSize).thenReturn(secondSize);

        metrics.addRequestMetrics(requestData);
        metrics.addRequestMetrics(requestData);
    }

    private void testMetrics(int expectedMaximumRequestTime, int expectedMinimumRequestTime, double expectedAverageRequestTime,
                             int expectedMaximumRequestSize, int expectedMinimumRequestSize, double expectedAverageRequestSize) {
        Assert.assertEquals(expectedMaximumRequestTime, metrics.getMaximumRequestTime());
        Assert.assertEquals(expectedMinimumRequestTime, metrics.getMinimumRequestTime());
        Assert.assertEquals(expectedAverageRequestTime, metrics.getAverageRequestTime(), 0.01);
        Assert.assertEquals(expectedMaximumRequestSize, metrics.getMaximumResponseSize());
        Assert.assertEquals(expectedMinimumRequestSize, metrics.getMinimumResponseSize());
        Assert.assertEquals(expectedAverageRequestSize, metrics.getAverageResponseSize(), 0.01);
    }
}