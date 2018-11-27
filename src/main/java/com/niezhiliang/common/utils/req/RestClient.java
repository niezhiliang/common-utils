package com.niezhiliang.common.utils.req;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class RestClient {

	private static RestClient instance = new RestClient(new HTTP(new HTTPConfig()));

	private HTTP httpClient;

	public RestClient(HTTP httpClient) {
		this.httpClient = httpClient;
	}

	public static RestClient getDefault() {
		return instance;
	}

	enum Method {
		POST, PUT, PATCH, GET, DELETE, HEAD, OPTIONS, TRACE
	}

	public Response post(String url, Map<String, ? extends CharSequence> headers, List<File> files)
			throws IOException {
		return uploadFile(Method.POST, url, null, headers, files);
	}

	public Response post(String url, Map<String, ? extends CharSequence> headers, String json)
			throws IOException {
		return reqWithEntity(Method.POST, url, null, headers, json);
	}

	public Response post(String url, Map<String, ? extends CharSequence> headers, HttpEntity entity)
			throws IOException {
		return reqWithEntity(Method.POST, url, null, headers, entity);
	}

    public Response postUploadFile(String url, Map<String, ? extends CharSequence> headers,Map<String, ? extends CharSequence> bodys, HttpEntity entity)
            throws IOException {
        return reqWithEntity(Method.POST, url, bodys, headers, entity);
    }

	public Response post(String url, Map<String, ? extends CharSequence> headers, Map<String, Object> data)
			throws IOException {
		return reqWithEntity(Method.POST, url, null, headers, data);
	}

	public Response post(String url, Map<String, Object> data) throws IOException {
		return reqWithEntity(Method.POST, url, null, null, data);
	}

	public Response post(String url, String json) throws IOException {
		return reqWithEntity(Method.POST, url, null, null, json);
	}

	public Response put(String url, Map<String, ? extends CharSequence> headers, String json)
			throws IOException {
		return reqWithEntity(Method.PUT, url, null, headers, json);
	}

	public Response put(String url, Map<String, ? extends CharSequence> headers, Map<String, Object> data)
			throws IOException {
		return reqWithEntity(Method.PUT, url, null, headers, data);
	}

	public Response put(String url, String json) throws IOException {
		return reqWithEntity(Method.PUT, url, null, null, json);
	}

	public Response put(String url, Map<String, Object> data) throws IOException {
		return reqWithEntity(Method.PUT, url, null, null, data);
	}

	public Response get(String url, Map<String, ? extends CharSequence> headers)
			throws IOException {
		return reqNoEntity(Method.GET, url, null, headers);
	}

	public Response get(String url) throws IOException {
		return reqNoEntity(Method.GET, url, null, null);
	}
	public Response getParams(String url, Map<String, String> params) throws  IOException {
		return reqNoEntity(Method.GET, url, params, null);
	}

	public Response delete(String url, Map<String, ? extends CharSequence> headers)
			throws IOException {
		return reqNoEntity(Method.DELETE, url, null, headers);
	}

	public Response delete(String url) throws IOException {
		return reqNoEntity(Method.DELETE, url, null, null);
	}

	public static String buildUri(String url, Map<String, ?> params) {
		try {
			URIBuilder ub = new URIBuilder(url);
			if (params != null) {
				for (Entry<String, ?> entry : params.entrySet()) {
					if (entry.getKey() != null && entry.getValue() != null) {
						ub.addParameter(entry.getKey(), entry.getValue().toString());
					}
				}
			}
			return ub.build().toString();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	private void setHeaders(HttpUriRequest request, Map<String, ? extends CharSequence> headers) {
		if (headers != null) {
			for (Entry<String, ? extends CharSequence> entry : headers.entrySet()) {
				if (entry.getKey() != null && entry.getValue() != null) {
					request.addHeader(entry.getKey(), entry.getValue().toString());
				}
			}
		}
	}

	private Response uploadFile(Method method, String url, Map<String, ? extends CharSequence> params,
                                                                Map<String, ? extends CharSequence> headers, List<File> list) throws IOException {
		HttpEntityEnclosingRequestBase request;
		if (method == Method.POST) {
			request = new HttpPost(buildUri(url, params));
		} else {
			request = new HttpPut(buildUri(url, params));
		}
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

		for (int i = 0; i < list.size(); i++) {
			FileBody fileBody = new FileBody(list.get(i));
			builder.addPart("file" + i, fileBody);
		}

		request.setEntity(builder.build());

		return httpClient.execute(request, ResponseHandlers.REST);
	}

    public Response post(String url, Map<String, ? extends CharSequence> headers, Map<String, ? extends CharSequence> params, Map<String, File> files)
            throws IOException {
        return uploadFile(Method.POST, url, params, headers, files);
    }
    private Response uploadFile(Method method, String url, Map<String, ? extends CharSequence> params,
                                Map<String, ? extends CharSequence> headers, Map<String, File> files) throws IOException {
        HttpEntityEnclosingRequestBase request;
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        if (method == Method.POST) {
            request = new HttpPost(url);

            if (params != null) {
                for (Entry<String, ?> entry : params.entrySet()) {
                    if (entry.getKey() != null && entry.getValue() != null) {

                        builder.addTextBody(entry.getKey(), entry.getValue().toString());
                    }
                }
            }
        } else {
            request = new HttpPut(buildUri(url, params));
        }

        setHeaders(request, headers);

        if(files!=null){
            for(String key: files.keySet()){
                FileBody fileBody = new FileBody(files.get(key));
                builder.addPart(key, fileBody);
            }
        }
        request.setEntity(builder.build());

        return httpClient.execute(request, ResponseHandlers.REST);
    }



    private Response reqWithEntity(Method method, String url, Map<String, ? extends CharSequence> params,
								   Map<String, ? extends CharSequence> headers, HttpEntity entity) throws IOException {
		HttpEntityEnclosingRequestBase request;
		if (method == Method.POST) {
			request = new HttpPost(buildUri(url, params));
		} else {
			request = new HttpPut(buildUri(url, params));
		}
		setHeaders(request, headers);
		request.setEntity(entity);
		if (null != headers && headers.get(HttpHeaders.CONTENT_TYPE) != null) {
			request.addHeader(HttpHeaders.CONTENT_TYPE, headers.get(HttpHeaders.CONTENT_TYPE).toString());
		}

		return httpClient.execute(request, ResponseHandlers.REST);
	}


	private Response reqWithEntity(Method method, String url, Map<String, ? extends CharSequence> params,
                                                                   Map<String, ? extends CharSequence> headers, String json) throws IOException {
		HttpEntityEnclosingRequestBase request;
		if (method == Method.POST) {
			request = new HttpPost(buildUri(url, params));
		} else {
			request = new HttpPut(buildUri(url, params));
		}

		setHeaders(request, headers);

		if (json != null && !json.isEmpty()) {
			request.setEntity(new StringEntity(json, "utf-8"));
			if (null != headers && headers.get(HttpHeaders.CONTENT_TYPE) != null) {
				request.addHeader(HttpHeaders.CONTENT_TYPE, headers.get(HttpHeaders.CONTENT_TYPE).toString());
			} else {
				request.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
			}
		}

		return httpClient.execute(request, ResponseHandlers.REST);
	}

	private Response reqWithEntity(Method method, String url, Map<String, ? extends CharSequence> params,
                                                                   Map<String, ? extends CharSequence> headers, Map<String, Object> data)
			throws IOException {
		String json = null;
		if (data != null && !data.isEmpty()) {

			json = JSON.toJSONString(data);
		}
		return reqWithEntity(method, url, params, headers, json);
	}

	private Response reqNoEntity(Method method, String url, Map<String, ? extends CharSequence> params,
                                                                 Map<String, ? extends CharSequence> headers) throws IOException {
		HttpUriRequest request;
		if (method == Method.GET) {
			request = new HttpGet(buildUri(url, params));
		} else if (method == Method.DELETE) {
			request = new HttpDelete(buildUri(url, params));
		} else if (method == Method.HEAD) {
			request = new HttpHead(buildUri(url, params));
		} else if (method == Method.OPTIONS) {
			request = new HttpOptions(buildUri(url, params));
		} else {
			request = new HttpTrace(buildUri(url, params));
		}

		setHeaders(request, headers);
		return httpClient.execute(request, ResponseHandlers.REST);

	}

	public void close() throws IOException {
		httpClient.close();
	}
}
