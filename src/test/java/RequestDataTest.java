import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

public class RequestDataTest {

    private RequestData requestData;

    @Before
    public void setup() {
        requestData = new RequestData();
    }

    @Test
    public void getRequestTime_Successful() {
        requestData.setStartRequestProcess(0);
        requestData.setEndRequestProcess(1);

        Assert.assertEquals(1, requestData.getRequestTime());

    }

    @Test (expected = IllegalStateException.class)
    public void getRequestTime_NegativeRequestTime() {
        requestData.setStartRequestProcess(1);
        requestData.setEndRequestProcess(0);

        requestData.getRequestTime();
    }

    @Test (expected = NullPointerException.class)
    public void getRequestTime_NullStartRequestTime() {
        requestData.setEndRequestProcess(0);

        requestData.getRequestTime();
    }

    @Test (expected = NullPointerException.class)
    public void getRequestTime_NullEndRequestTime() {
        requestData.setStartRequestProcess(0);

        requestData.getRequestTime();
    }

    @Test (expected = NullPointerException.class)
    public void getRequestTime_NullRequestTimes() {
        requestData.getRequestTime();
    }

    @Test
    public void whenSerializingAndDeserializing_ThenObjectIsTheSame() throws IOException {
        RequestData requestData = new RequestData();
        requestData.setStartRequestProcess(0);
        requestData.setEndRequestProcess(1);
        requestData.setResponseSize(1L);

        String file = "test.txt";
        new File(file).delete();

        // write to file
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file))) {
            objectOutputStream.writeObject(requestData);
            objectOutputStream.flush();
        }

        // read from file
        RequestData inputRequestData;
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file))){
            inputRequestData = (RequestData) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            System.out.println( e.getLocalizedMessage() + "File: [" + file + "]. Will create new file to store metrics. ");
            inputRequestData = new RequestData();
        } catch (IOException e) {
            e.printStackTrace();
            inputRequestData = new RequestData();
        } catch (ClassNotFoundException e) {
            System.out.println(e.getLocalizedMessage() + "Error reading [" + file + "]. Will create new (empty) historical metrics.");
            inputRequestData = new RequestData();
        }
        new File(file).delete();

        Assert.assertEquals(requestData.getRequestTime(), inputRequestData.getRequestTime());
        Assert.assertEquals(requestData.getResponseSize(), inputRequestData.getResponseSize());
        Assert.assertEquals(requestData.getRequestId(), inputRequestData.getRequestId());
    }
}