package com.niezhiliang.common.utils.req;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ResponseHandlers {


	private final static Logger log = LoggerFactory.getLogger(ResponseHandlers.class);

	public static final ResponseHandler<Response> REST = new ResponseHandler<Response>() {
		@Override
		public Response handleResponse(HttpResponse response) throws IOException {
			try{

				StatusLine status = response.getStatusLine();
				byte[] body = EntityUtils.toByteArray(response.getEntity());
				Header[] allHeaders = response.getAllHeaders();
				if(null!=allHeaders && allHeaders.length>0){
					Map<String, String> headers = new HashMap<>();
					for(Header header: allHeaders){
						headers.put(header.getName(), header.getValue());
					}
					return new Response(status.getStatusCode(), body, headers);
				}else{
					return new Response(status.getStatusCode(), body);
				}
			}catch (Exception e){
				throw e;
			}finally {
				close(response);
			}
		}

		private void close(HttpResponse resp) {
			try {
				if(resp == null) return;
				if(CloseableHttpResponse.class.isAssignableFrom(resp.getClass())){
					((CloseableHttpResponse)resp).close();
				}
			} catch (IOException e) {
				log.error("关闭HttpResponse 失败 ",e);
			}
		}
	};
}
