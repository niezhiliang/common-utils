package com.niezhiliang.common.utils.ip;

import com.alibaba.fastjson.JSONObject;
import com.niezhiliang.common.utils.constant.IPURL;
import com.niezhiliang.common.utils.req.Response;
import com.niezhiliang.common.utils.req.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author NieZhiLiang
 * @Email nzlsgg@163.com
 * @Date 2018/11/27 上午9:01
 */
public class IpUtils {
    private final static Logger logger = LoggerFactory.getLogger(IpUtils.class);
    /**
     * 通过request对象获取ip地址
     * @return
     */
    public static String getIpAddress(HttpServletRequest request){
        String ipAddress = request.getHeader("x-forwarded-for");

        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if(("127.0.0.1").equals(ipAddress) || ("0:0:0:0:0:0:0:1").equals(ipAddress)){
                //根据网卡取本机配置的IP
                InetAddress inet=null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress= inet.getHostAddress();
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if(ipAddress!=null && ipAddress.length()>15){ //"***.***.***.***".length() = 15
            if(ipAddress.indexOf(",")>0){
                ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }

    /**
     * 获取ip归属地
     * @param request
     * @return
     */
    public static String getOwnerShip (HttpServletRequest request) {
        String ip = getIpAddress(request);
        return getOwnerShip(ip);
    }


    /**
     * 获取ip归属地
     * @param ip
     * @return
     */
    public static String getOwnerShip (String ip) {
        StringBuffer ownerShip = new StringBuffer();
        Response response = null;
        try {
            response = RestClient.getDefault().get(IPURL.GAODO_URL+ip);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        if (response.getCode() == 200) {
            Map<String,Object> resMap = new HashMap<>();
            resMap = JSONObject.parseObject(response.getBody(),resMap.getClass());
            if("1".equals(resMap.get("status").toString())){
                ownerShip.append(resMap.get("province"))
                        .append(resMap.get("city"));
            } else {
                ownerShip.append("归属地未知");
            }
        }
        return ownerShip.toString();
    }

    /**
     * 获取ip归属省
     * @param ip
     * @return
     */
    public static String getOwnerShipProvince (String ip) {
        StringBuffer ownerShip = new StringBuffer();
        Response response = null;
        try {
            response = RestClient.getDefault().get(IPURL.GAODO_URL+ip);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        if (response.getCode() == 200) {
            Map<String,Object> resMap = new HashMap<>();
            resMap = JSONObject.parseObject(response.getBody(),resMap.getClass());
            if("1".equals(resMap.get("status").toString())){
                ownerShip.append(resMap.get("province"));
            } else {
                ownerShip.append("归属地未知");
            }
        }
        return ownerShip.toString();
    }

    /**
     * 获取ip归属市
     * @param ip
     * @return
     */
    public static String getOwnerShipCity (String ip) {
        StringBuffer ownerShip = new StringBuffer();
        Response response = null;
        try {
            response = RestClient.getDefault().get(IPURL.GAODO_URL+ip);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        if (response.getCode() == 200) {
            Map<String,Object> resMap = new HashMap<>();
            resMap = JSONObject.parseObject(response.getBody(),resMap.getClass());
            if("1".equals(resMap.get("status").toString())){
                ownerShip.append(resMap.get("city"));
            } else {
                ownerShip.append("归属地未知");
            }
        }
        return ownerShip.toString();
    }
}
