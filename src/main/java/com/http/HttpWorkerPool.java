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

	private final CloseableHttpClient httpClient;

	private final PoolingHttpClientConnectionManager connManager;

	public HttpWorkerPool() throws GeneralSecurityException {
		connManager = new PoolingHttpClientConnectionManager();
		connManager.setMaxTotal(500);

		// SSL
		LayeredConnectionSocketFactory sslSocketFactory = this
				.getSslSocketFactory();

		httpClient = HttpClients.custom().setConnectionManager(connManager)
				.setSSLSocketFactory(sslSocketFactory)
				.build();
	}

	private LayeredConnectionSocketFactory getSslSocketFactory() {
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

	private static final int MAX_PER_ROUTE = 100;

	public void addHttpHost(String hostname) {
		HttpHost host = new HttpHost(hostname,
				DEFAULT_HTTP_PORT);
		connManager.setMaxPerRoute(new HttpRoute(host),
				MAX_PER_ROUTE);
	}

	public String get(String uri) {
		HttpGet request = new HttpGet(uri);
		try {
			CloseableHttpResponse response = httpClient.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				if (null != entity) {
					String content = EntityUtils.toString(entity);
					EntityUtils.consume(entity);
					return content;
				}
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
