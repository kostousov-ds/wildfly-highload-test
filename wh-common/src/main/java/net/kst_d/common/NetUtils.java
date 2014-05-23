package net.kst_d.common;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;

import net.kst_d.common.log.Logger;
import net.kst_d.common.log.LoggerFactory;
import net.kst_d.common.log.MethodLogger;

public class NetUtils {
    private static final Logger LOG = LoggerFactory.getLogger(NetUtils.class);
    public static final int TIMEOUT = 5000;


    private static String encode(String val, TicSessionID ticSessionID) {
	try {
	    return URLEncoder.encode(val, StandardCharsets.UTF_8.name());
	} catch (UnsupportedEncodingException e) {
	    LOG.error(ticSessionID, "encode", "Can't encode \"{}\". Exception {}", val, e.toString());
	}
	return "";
    }

    public static String get(final int expectedCode, final String url, final TicSessionID ticSessionID, final List<Two<String, String>> params) throws HttpInteractionException {

	final String paramString;
	if (params != null && params.size() > 0) {
	    paramString = "?" + params.stream().map(p -> encode(p._1, ticSessionID) + "=" + encode(p._2, ticSessionID)).collect(Collectors.joining("&"));
	} else {
	    paramString = "";
	}
	return retrieveHtml(expectedCode, new HttpGet(url + paramString), ticSessionID);
    }

    public static String post(final int expectedCode, final String url, final TicSessionID ticSessionID, final List<Two<String, String>> params) throws HttpInteractionException {
	final MethodLogger logger = LOG.silentEnter(ticSessionID, "post");

	logger.trace("url {}", url);

	HttpPost httpPost = new HttpPost(url);
	if (params != null && params.size() > 0) {
	    List<BasicNameValuePair> httpParams = params.parallelStream().map(pair -> new BasicNameValuePair(pair._1, pair._2)).collect(Collectors.toList());

	    try {
//		httpPost.setEntity(new UrlEncodedFormEntity(httpParams, Constants.SYSTEM_CHARSET));
		httpPost.setEntity(new UrlEncodedFormEntity(httpParams));
	    } catch (UnsupportedEncodingException e) {
		logger.error("{}", e.getMessage());
	    }

	}
	return retrieveHtml(expectedCode, httpPost, ticSessionID);
    }

    public static String retrieveHtml(int expectedCode, HttpUriRequest httpPost, TicSessionID ticSessionID) throws HttpInteractionException {
	RequestConfig cfg = RequestConfig.custom()
	    .setSocketTimeout(TIMEOUT)
	    .setConnectTimeout(TIMEOUT)
	    .setConnectionRequestTimeout(TIMEOUT)
	    .setStaleConnectionCheckEnabled(true)
	    .build();

	try (CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(cfg).setRedirectStrategy(new LaxRedirectStrategy()).build()) {
	    return retrieveHtml(client, expectedCode, httpPost, ticSessionID);
	} catch (IOException e) {
	    throw new HttpInteractionException(e);
	}


    }

    public static String retrieveHtml(HttpClient client, int expectedCode, HttpUriRequest httpPost, TicSessionID ticSessionID) throws HttpInteractionException {
	final MethodLogger logger = LOG.silentEnter(ticSessionID, "retrieveHtml");

	String ret;
	try {
	    HttpResponse httpResponse = client.execute(httpPost);
	    logger.trace("http response {}", httpResponse);
	    final int code = httpResponse.getStatusLine().getStatusCode();
	    if (code != expectedCode) {
		throw new HttpInteractionException("invalid code received during card sending: " + code + ", expected: " + expectedCode);
	    }
	    ret = IOUtils.toString(httpResponse.getEntity().getContent());
	} catch (HttpInteractionException e) {
	    throw e;
	} catch (Exception e) {
	    logger.error("", e);
	    throw new HttpInteractionException(e);
	}
	return ret;
    }
}
