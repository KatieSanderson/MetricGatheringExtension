import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RequestDataTest {

    RequestData times;

    @Before
    public void setup() {
        times = new RequestData();
    }

    @Test
    public void getRequestTime_Successful() {
        times.setStartRequestProcess(0);
        times.setEndRequestProcess(1);

        Assert.assertEquals(1, times.getRequestTime());

    }

    @Test (expected = IllegalStateException.class)
    public void getRequestTime_NegativeRequestTime() {
        times.setStartRequestProcess(1);
        times.setEndRequestProcess(0);

        times.getRequestTime();
    }

    @Test (expected = NullPointerException.class)
    public void getRequestTime_NullStartRequestTime() {
        times.setEndRequestProcess(0);

        times.getRequestTime();
    }

    @Test (expected = NullPointerException.class)
    public void getRequestTime_NullEndRequestTime() {
        times.setStartRequestProcess(0);

        times.getRequestTime();
    }

    @Test (expected = NullPointerException.class)
    public void getRequestTime_NullRequestTimes() {
        times.getRequestTime();
    }
}