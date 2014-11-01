package cn.fraudmetrix.http.impl.client;

import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.annotation.Immutable;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;

/**
 * 类KeepAliveHttpClients.java的实现描述：Factory methods for "Keep-Alive" {@link CloseableHttpClient} instances.
 * 
 * @author huagang.li 2014年11月1日 上午11:53:44
 * @see HttpClients
 * @since 1.0
 */
@Immutable
public class KeepAliveHttpClients {

    private KeepAliveHttpClients(){
        super();
    }

    /**
     * 基于给定的"Keep-Alive"分钟数来创建一个{@link CloseableHttpClient}实例。
     * 
     * @param keepAliveMinutes 连接的"Keep-Alive"分钟数
     * @return
     */
    public static CloseableHttpClient create(int keepAliveMinutes) {
        long keepAliveMillis = TimeUnit.MINUTES.toMillis(keepAliveMinutes);
        return createHttpClient(keepAliveMillis);
    }

    /**
     * 基于给定的"Keep-Alive"毫秒数来创建一个{@link CloseableHttpClient}实例。
     * 
     * @param keepAliveMillis 连接的"Keep-Alive"毫秒数
     * @return
     */
    public static CloseableHttpClient create(long keepAliveMillis) {
        return createHttpClient(keepAliveMillis);
    }

    private static CloseableHttpClient createHttpClient(long keepAliveMillis) {
        ConnectionKeepAliveStrategy keepAliveStrategy = getConnectionKeepAliveStrategy(keepAliveMillis);
        return HttpClients.custom().setKeepAliveStrategy(keepAliveStrategy).build();
    }

    /**
     * 返回具有"Keep-Alive"的连接策略。
     * <p>
     * 参考《HttpClient Tutorial》的<a
     * href="http://hc.apache.org/httpcomponents-client-ga/tutorial/html/fundamentals.html#d5e206">1.2. HttpClient
     * interface</a>
     */
    private static ConnectionKeepAliveStrategy getConnectionKeepAliveStrategy(long keepAliveMillis) {
        return new CustomConnectionKeepAliveStrategy(keepAliveMillis);
    }

    /*
     * 自定义"Keep-Alive"连接策略实现类。
     */
    private static class CustomConnectionKeepAliveStrategy extends DefaultConnectionKeepAliveStrategy {

        private long keepAliveMillis; // 连接保持活跃时间（单位：毫秒数）

        public CustomConnectionKeepAliveStrategy(long keepAliveMillis){
            this.keepAliveMillis = keepAliveMillis;
        }

        @Override
        public long getKeepAliveDuration(final HttpResponse response, final HttpContext context) {
            long keepAlive = super.getKeepAliveDuration(response, context);
            if (keepAlive == -1L) {
                // Keep connections alive ${keepAliveMillis} minutes if a keep-alive value
                // has not be explicitly set by the server
                keepAlive = this.keepAliveMillis;
            }
            return keepAlive;
        }

    }

}
