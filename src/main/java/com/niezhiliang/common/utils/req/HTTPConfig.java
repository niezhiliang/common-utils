package com.niezhiliang.common.utils.req;

import org.apache.http.Consts;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.nio.charset.CodingErrorAction;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

public class HTTPConfig {
	private int maxTotal = 400;
	private int retryCount = 4;
	private int socketTimeout = 15000;
	private int connectTimeout = 10000;

	private PoolingHttpClientConnectionManager connectionManager;
	private HttpRequestRetryHandler retryHandler;

	public PoolingHttpClientConnectionManager getConnectionManager() {
		if (connectionManager != null) {
			return connectionManager;
		}
		try {
			Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create()
					.register("http", getHttpSocketFactory()).register("https", getHttpsSocketFactory()).build();
			connectionManager = new PoolingHttpClientConnectionManager(registry);
			connectionManager.setMaxTotal(maxTotal);
			connectionManager.setDefaultMaxPerRoute(maxTotal);
			connectionManager.closeIdleConnections(5, TimeUnit.MINUTES);
			ConnectionConfig cf = ConnectionConfig.custom().setMalformedInputAction(CodingErrorAction.IGNORE)
					.setUnmappableInputAction(CodingErrorAction.IGNORE).setCharset(Consts.UTF_8).build();
			connectionManager.setDefaultConnectionConfig(cf);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return connectionManager;
	}

	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public int getSocketTimeout() {
		return socketTimeout;
	}

	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public HttpRequestRetryHandler getRetryHandler() {
		if (retryHandler != null) {
			return retryHandler;
		}

		retryHandler = new HttpRequestRetryHandler() {
			@Override
			public boolean retryRequest(IOException exception, int count, HttpContext context) {
				if (count > retryCount) {
					return false;
				}

				if (exception instanceof ClientProtocolException) {
					return false;
				}

				if (exception instanceof InterruptedIOException) {
					// Timeout
					return false;
				}
				if (exception instanceof UnknownHostException) {
					// Unknown host
					return false;
				}
				if (exception instanceof ConnectTimeoutException) {
					// Connection refused
					return false;
				}
				if (exception instanceof SSLException) {
					// SSL handshake exception
					return false;
				}
				HttpClientContext clientContext = HttpClientContext.adapt(context);
				HttpRequest request = clientContext.getRequest();
				boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
				if (idempotent) {
					// Retry if the request is considered idempotent
					return true;
				}
				return false;
			}
		};
		return retryHandler;
	}

	private static PlainConnectionSocketFactory getHttpSocketFactory() {
		PlainConnectionSocketFactory psf = new PlainConnectionSocketFactory();
		return psf;
	}

	private static LayeredConnectionSocketFactory getHttpsSocketFactory() throws Exception {

		SSLContext sslContext = SSLContexts.custom().useProtocol("TLSv1")
				// .loadKeyMaterial(keyStore, "1228527702".toCharArray())
				.loadTrustMaterial(new TrustStrategy() {
					@Override
					public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
						return true;
					}
				}).build();

		LayeredConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
		return sslsf;
	}
}
