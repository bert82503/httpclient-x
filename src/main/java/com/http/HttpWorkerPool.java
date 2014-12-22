/*
 * Copyright (c)
 */
package com.http;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HTTP请求工作者对象池。
 * 
 * @author huagang.li
 * @since 1.0
 */
public class HttpWorkerPool implements Closeable {

	private static final Logger logger = LoggerFactory
			.getLogger(HttpWorkerPool.class);

	/** "HTTP工作者对象池"单例对象 */
	private static final HttpWorkerPool httpWorkerPool = new HttpWorkerPool();

	/** HTTP请求客户端 */
	private final CloseableHttpClient httpClient;

	/** 对象池的连接管理器，用于动态更新每个域名的最大并发连接数 */
	private final PoolingHttpClientConnectionManager connManager;

	/** 请求主机名管理器(<hostname, *>)，保证"每个域名的最大并发连接数"最多被设置一次 */
	private final ConcurrentMap<String, Object> hostnameMap;

	/** 每个域名的最大并发连接数 */
	private final int maxPerRoute;

	/**
	 * 获取"HTTP工作者对象池"实例。
	 * <p>
	 * "单例模式"实现
	 * 
	 * @return
	 */
	public static HttpWorkerPool getInstance() {
		return httpWorkerPool;
	}

	private HttpWorkerPool() {
		connManager = new PoolingHttpClientConnectionManager();
		connManager.setMaxTotal(500);

		// SSL
		LayeredConnectionSocketFactory sslSocketFactory = getSslSocketFactory();

		httpClient = HttpClients.custom().setConnectionManager(connManager)
				.setSSLSocketFactory(sslSocketFactory).build();

		hostnameMap = new ConcurrentHashMap<String, Object>();
		maxPerRoute = HttpConfigUtils.getHttpMaxPerRoute();
	}

	/**
	 * 获取SSL套接字工厂配置。
	 * 
	 * @return
	 */
	private static LayeredConnectionSocketFactory getSslSocketFactory() {
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			// taobao.com
			InputStream instream = new FileInputStream(
					HttpConfigUtils.getTaobaoSslCertFileName());
			try {
				trustStore.load(instream, HttpConfigUtils
						.getTaobaoSslCertFilePassword().toCharArray());
			} finally {
				instream.close();
			}

			// Trust own CA and all self-signed certs
			SSLContext sslcontext = SSLContexts
					.custom()
					.loadTrustMaterial(trustStore,
							new TrustSelfSignedStrategy()).build();
			// Allow TLSv1, TLSv1.1, TLSv1.2 protocol
			SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(
					sslcontext,
					new String[] { "TLSv1", "TLSv1.1", "TLSv1.2" },
					null,
					SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
			return sslSocketFactory;
		} catch (GeneralSecurityException gse) {
			logger.error(gse.getMessage(), gse);
		} catch (IOException ioe) {
			logger.error(
					"Taobao SSL Cert File not found: "
							+ HttpConfigUtils.getTaobaoSslCertFileName(), ioe);
		}
		return null;
	}

	private static final int DEFAULT_HTTP_PORT = 80;

	/**
	 * 设置"每个域名的最大并发连接数"，但每个域名最多被设置一次。
	 * 
	 * @param uri
	 */
	private void setMaxPerRoute(String uri) {
		String hostname = HttpUtils.getHostname(uri);
		if (hostnameMap.putIfAbsent(hostname, "1") == null) { // 首次插入
			HttpHost host = new HttpHost(hostname, DEFAULT_HTTP_PORT);
			connManager.setMaxPerRoute(new HttpRoute(host), maxPerRoute);
		}
	}

	/**
	 * 获取HTTP请求返回的字符串内容。
	 * 
	 * @param uri
	 * @return
	 */
	public String getString(String uri) {
		this.setMaxPerRoute(uri);

		HttpGet request = new HttpGet(uri);
		try {
			CloseableHttpResponse response = httpClient.execute(request);
			try {
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					HttpEntity entity = response.getEntity();
					if (null != entity) {
						String content = EntityUtils.toString(entity);
						return content;
					}
				}
			} finally {
				response.close();
			}
		} catch (IOException ioe) {
			logger.warn("Http GET request is failed: " + uri, ioe);
		}
		return null;
	}

	/**
	 * 获取HTTP请求返回的字符串内容。
	 * 
	 * @param uri
	 * @return
	 */
	public byte[] getByteArray(String uri) {
		this.setMaxPerRoute(uri);

		HttpGet request = new HttpGet(uri);
		try {
			CloseableHttpResponse response = httpClient.execute(request);
			try {
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					HttpEntity entity = response.getEntity();
					if (null != entity) {
						byte[] content = EntityUtils.toByteArray(entity);
						return content;
					}
				}
			} finally {
				response.close();
			}
		} catch (IOException ioe) {
			logger.warn("Http GET request is failed: " + uri, ioe);
		}
		return null;
	}

	@Override
	public void close() throws IOException {
		httpClient.close();
	}

}
