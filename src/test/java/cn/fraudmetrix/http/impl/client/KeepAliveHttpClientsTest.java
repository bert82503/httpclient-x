package cn.fraudmetrix.http.impl.client;

import static java.lang.System.out;
import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;

/**
 * 类KeepAliveHttpClientsTest.java的实现描述：Test for {@link KeepAliveHttpClients} instance.
 * 
 * @author huagang.li 2014年11月1日 下午2:42:54
 */
public class KeepAliveHttpClientsTest {

    @Test(invocationCount = 3, threadPoolSize = 3)
    public void doKeepAliveRequest() throws IOException, InterruptedException {
        String uri = "http://localhost";

        CloseableHttpClient httpClient = KeepAliveHttpClients.create(5); // 连接5min存活时间
        HttpUriRequest request = new HttpGet(uri);
        try {
            for (int j = 0; j < 2; j++) {
                for (int i = 0; i < 3; i++) {
                    this.execute(httpClient, request);
                    TimeUnit.SECONDS.sleep(10L);
                }
                out.println("");
                if (j < 1) {
                    TimeUnit.MINUTES.sleep(7L); // 模拟"Keep-Alive" HTTP连接失效过期而被关闭，会重新握手连接（结合tcpdump）
                }
            }
        } finally {
            // resource deallocation (资源再分配)
            httpClient.close();
        }
    }

    @Test(invocationCount = 3, threadPoolSize = 3)
    public void doKeepAlivePoolRequest() throws IOException, InterruptedException {
        String uri = "http://localhost";

        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setMaxTotal(128);
        connManager.setDefaultMaxPerRoute(16);

        CloseableHttpClient httpClient = KeepAliveHttpClients.create(connManager, 5); // 连接5min存活时间
        HttpUriRequest request = new HttpGet(uri);
        try {
            // for (int j = 0; j < 2; j++) {
            for (int i = 0; i < 3; i++) {
                this.execute(httpClient, request);
                TimeUnit.SECONDS.sleep(10L);
            }
            out.println("");
            // [leased: 0; pending: 0; available: 1; max: 128]
            out.println(connManager.getTotalStats().toString());
            // if (j < 1) {
            // TimeUnit.MINUTES.sleep(7L); // 模拟"Keep-Alive" HTTP连接失效过期而被关闭，会重新握手连接（结合tcpdump）
            // }
            // }
        } finally {
            // resource deallocation (资源再分配)
            httpClient.close();
        }
    }

    private void execute(CloseableHttpClient httpClient, HttpUriRequest request) throws IOException {
        CloseableHttpResponse response = httpClient.execute(request);

        out.println("Status Line: " + response.getStatusLine());
        assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
        HttpEntity entity = response.getEntity();
        // String responseContent = EntityUtils.toString(entity, StandardCharsets.UTF_8);
        // out.println("Response Content: " + responseContent);
        EntityUtils.consume(entity);

        response.close();
    }

}
