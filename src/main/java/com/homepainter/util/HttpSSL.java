package com.homepainter.util;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class HttpSSL {

//    private CloseableHttpClient setProxy(HttpRequestBase http, String proxyIp, int proxyPort) {
//        //绕过ssl认证
//        SSLContext sslContext = null;
//        try {
//            sslContext = SSLContextBuilder.create().loadTrustMaterial(null, new TrustStrategy() {
//                @Override
//                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
//                    return true;
//                }
//            }).build();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (KeyManagementException e) {
//            e.printStackTrace();
//        } catch (KeyStoreException e) {
//            e.printStackTrace();
//        }
//        // 创建httpClient实例
//        CloseableHttpClient httpClient = HttpClientBuilder.create().setSSLContext(sslContext).
//                setSSLHostnameVerifier(new NoopHostnameVerifier()).build();;
//        //设置代理IP、端口
//        HttpHost proxy = new HttpHost(proxyIp, proxyPort, "http");
//        //也可以设置超时时间   RequestConfig requestConfig = RequestConfig.custom().setProxy(proxy).setConnectTimeout(3000).setSocketTimeout(3000).setConnectionRequestTimeout(3000).build();
//        RequestConfig requestConfig = RequestConfig.custom().setProxy(proxy).build();
//        http.setConfig(requestConfig);
//        return httpClient;
//    }

}
