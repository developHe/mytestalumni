package com.hb.network;

import android.app.Application;
import android.util.Log;
import android.util.Xml;

import com.hb.R;
import com.hb.activity.BaseActivity;
import com.hb.client.ApplicationEnvironment;
import com.hb.client.Constants;
import com.hb.model.HttpRequestModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class LKHttpRequest {

	private static ArrayList<HttpRequestModel> httpRequestTypeList = null;
	private AsyncHttpClient client;
	private HttpRequestModel httpRequestModel;
	private String[] httpRequestParms;
	private LKHttpRequestQueue queue;
	private HashMap<String, Object> requestDataMap;
	private String requestId;
	private LKAsyncHttpResponseHandler responseHandler;
	private int tag;

	public LKHttpRequest(String requestId,
			HashMap<String, Object> requestDataMap,
			LKAsyncHttpResponseHandler handler, String... params) {
		this.requestId = requestId;
		this.httpRequestParms = params;
		if (requestDataMap == null)
			requestDataMap = new HashMap<String, Object>();
		this.requestDataMap = requestDataMap;
		this.responseHandler = handler;
		this.httpRequestModel = getHttpRequestModel(requestId);
		AsyncHttpClient localAsyncHttpClient = new AsyncHttpClient();
		this.client = localAsyncHttpClient;
		this.responseHandler.setRequest(this);
	}

	private String getHttpGetEntity(HashMap<String, Object> paramHashMap) {
		StringBuffer requestURL = new StringBuffer(getRequestURL());
		requestURL.append("&");
		Iterator localIterator = paramHashMap.keySet().iterator();
		while (true) {
			if (!localIterator.hasNext()) {
				int i = requestURL.length() + -1;
				requestURL.deleteCharAt(i);
				Log.d("request body:", requestURL.toString());
				return requestURL.toString();
			}
			String next = (String) localIterator.next();
			StringBuffer localStringBuffer4 = requestURL.append(next).append("=");
			Object localObject = paramHashMap.get(next);
			localStringBuffer4.append(localObject).append("&");
		}
	}

	private HttpEntity getHttpPostEntity(HashMap<String, Object> paramHashMap) {
		try {
			JSONObject localJSONObject1 = new JSONObject();
			Iterator<String> localObject1 = paramHashMap.keySet().iterator();
			while (true) {
				if (!((Iterator) localObject1).hasNext()) {
					String str1 = localJSONObject1.toString();
					Log.d("request body:", str1);
//					String str2 = localJSONObject1.toString();
					HttpEntity localObject = new StringEntity(str1, "UTF-8");
					return localObject;
				}
				String str3 = (String) ((Iterator) localObject1).next();
				Object localObject2 = paramHashMap.get(str3);
				localJSONObject1.put(str3, localObject2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private HttpRequestModel getHttpRequestModel(String requstId) {
		if (httpRequestTypeList == null)
			httpRequestTypeList = parseHttpRequestXML();
		Iterator localIterator = httpRequestTypeList.iterator();
		HttpRequestModel model;
		if (!localIterator.hasNext()) {
			Log.e("LKHTTPREQUEST", "http_request_param.xml 有误！请检查配置文件");
			return null;
		}
		while (true) {

			model = (HttpRequestModel) localIterator.next();
			if (model.toString().equalsIgnoreCase(requstId))
				break;
		}
		return model;
	}

	private RequestParams getRequestParams(HashMap<String, Object> map) {
		RequestParams localRequestParams = new RequestParams();
		Object content = null;
		String key = null;
		try {
			Iterator localIterator = map.keySet().iterator();
			while (true) {
				if (!localIterator.hasNext())
					return localRequestParams;
				key = (String) localIterator.next();
				content = map.get(key);
				if (!(content instanceof String))
					break;
				localRequestParams.put(key, (String) content);
			}
		} catch (Exception e) {

			e.printStackTrace();

			if (content instanceof File) {
				localRequestParams.put(key, content);
			}

		}
		return localRequestParams;
	}

	/**
	 * 解析http请求配置表(raw/http_request_param.xml)
	 * 
	 * @return
	 */
	private ArrayList<HttpRequestModel> parseHttpRequestXML() {
		InputStream in = null;
		ArrayList<HttpRequestModel> arrModels = new ArrayList<HttpRequestModel>();
		HttpRequestModel model = null;
		try {
			in = BaseActivity.getTopActivity().getResources()
					.openRawResource(R.raw.http_request_param);
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(in, "utf-8");
			int eventType = parser.getEventType();
			// 一直解析，直到文件结尾
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				// 文档标签
				case XmlPullParser.START_TAG: {
					String tag = parser.getName();
					if (tag == null)
						continue;
					if (tag.equals("request")) {
						model = new HttpRequestModel();
					} else if (tag.equals("requestId")) {
						model.setRequestId(parser.nextText());
					} else if (tag.equals("url")) {
						model.setUrl(parser.nextText());
					} else if (tag.equals("method")) {
						model.setMethod(parser.nextText());
					} else if (tag.equals("contentType")) {
						model.setContentType(parser.nextText());
					}
				}
					break;
				// 标签结束
				case XmlPullParser.END_TAG: {
					String tag = parser.getName();
					if (tag == null)
						continue;
					if (tag.equals("request")) {
						arrModels.add(model);
					}
					model = null;
				}
					break;
				default:
					break;
				}
				// 获取下一个
				eventType = parser.nextTag();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return arrModels;
	}

	public AsyncHttpClient getClient() {
		return this.client;
	}

	public HashMap<String, Object> getRequestDataMap() {
		return this.requestDataMap;
	}

	public String getRequestId() {
		return this.requestId;
	}

	public LKHttpRequestQueue getRequestQueue() {
		return this.queue;
	}

	public String getRequestURL() {
		StringBuilder localStringBuilder = new StringBuilder(
				"http://115.47.56.228/alumni/service");
		String str2 = localStringBuilder + httpRequestModel.getUrl();
		String[] arrayOfString = this.httpRequestParms;
		int i = 0;
		if(arrayOfString != null)
			i = arrayOfString.length;
		int j = 0;
		while (true) {
			if (j >= i) {
				StringBuffer localStringBuffer1 = new StringBuffer(str2);
				localStringBuffer1.append("?");
				localStringBuffer1.append("v=").append("1");
				localStringBuffer1.append("&").append("cid=").append("2");
				localStringBuffer1.append("&").append("sid=");
				localStringBuffer1.append(Constants.SESSION_ID);
				Log.i("url:", localStringBuffer1.toString());
				return localStringBuffer1.toString();
			}
			String str5 = arrayOfString[j];
			str2 = str2.replaceFirst("\\$\\{\\w*\\}", str5);
			j += 1;
		}
	}

	public LKAsyncHttpResponseHandler getResponseHandler() {
		return this.responseHandler;
	}

	public int getTag() {
		return this.tag;
	}

	public void send() {
		Application app = ApplicationEnvironment
				.getInstance().getApplication();
		if (this.httpRequestModel.getMethod().equalsIgnoreCase("POST")) {
			AsyncHttpClient localAsyncHttpClient1 = this.client;
			
			HttpEntity localHttpEntity = getHttpPostEntity(this.requestDataMap);
			String str2 = this.httpRequestModel.getContentType();
//			LKAsyncHttpResponseHandler localLKAsyncHttpResponseHandler1 = this.responseHandler;
			localAsyncHttpClient1.post(app, getRequestURL(),
					localHttpEntity, str2, this.responseHandler);
		} else{
			this.client.get(app, getHttpGetEntity(this.requestDataMap),
					this.responseHandler);
		}
			
	}

	public void setRequestId(String paramString) {
		this.requestId = paramString;
	}

	public void setRequestQueue(LKHttpRequestQueue paramLKHttpRequestQueue) {
		this.queue = paramLKHttpRequestQueue;
	}

	public void setTag(int paramInt) {
		this.tag = paramInt;
	}
}
