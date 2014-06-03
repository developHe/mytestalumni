package com.hb.network;

import android.content.DialogInterface;
import android.util.Log;

import com.hb.activity.BaseActivity;
import com.hb.activity.LoginActivity;
import com.hb.client.ParseResponseData;
import com.hb.exception.ServiceErrorException;
import com.hb.view.LKAlertDialog;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.client.HttpResponseException;
import org.json.JSONObject;

public abstract class LKAsyncHttpResponseHandler extends
		JsonHttpResponseHandler {
	private LKHttpRequest request;

	private String getErrorMsg(String content) {
		if (content == null)
			return "服务器异常，请与管理员联系或稍候再试。";

		if ((content.contains("ConnectTimeoutException"))
				|| (content.contains("SocketTimeoutException"))) {
			return "连接服务器超时，请检查您的网络情况或稍候再试。";

		}
		if ((content.contains("HttpHostConnectException"))
				|| (content.contains("ConnectException"))) {
			return "连接服务器超时，请检查您的网络情况或稍候再试。";

		}
		if (content.contains("Bad Request")) {
			return "服务器地址发生更新，请与管理员联系或稍候再试。";

		}
		if (content.contains("time out")) {
			return "连接服务器超时，请重试。";

		}
		if ((content.contains("can't resolve host"))
				|| (content.contains("400 Bad Request"))) {
			return "连接服务器出错，请确定您的连接服务器地址是否正确。";

		}
		if (content.contains("UnknownHostException")) {
			return "网络异常，无法连接服务器。";
		}
		return "服务器响应异常,请重新操作。";

	}

	public void failureAction(Throwable paramThrowable,
			JSONObject paramJSONObject) {
	}

	public void onFailure(final Throwable error, final JSONObject errorResponse) {
		super.onFailure(error, errorResponse);
		try {
			Throwable exception = (HttpResponseException) error;

			Log.e("Status Code",
					"" + ((HttpResponseException) exception).getStatusCode());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Log.e("error content:", "onFailure TODO");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			String str2 = error.getCause().toString();
			Log.e("failure:", str2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			String str3 = error.getCause().getMessage();
			Log.e("failure message:", str3);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (this.request.getRequestQueue() != null)
				request.getRequestQueue().cancel();
		} catch (Exception e) {
			e.printStackTrace();
		}
		BaseActivity.getTopActivity().hideDialog(3);
		BaseActivity localBaseActivity = BaseActivity.getTopActivity();
		LKAlertDialog dialog = new LKAlertDialog(localBaseActivity);
		dialog.setTitle("提示");
		try {
			dialog.setMessage(getErrorMsg(error.toString()));
		} catch (Exception e) {
			dialog.setMessage(getErrorMsg(null));
		}
		dialog.setCancelable(false);

		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
				failureAction(error, errorResponse);
			}
		});
		dialog.create().show();
		Log.e("error:", error.toString());

	}

	public void onFinish() {
		super.onFinish();
		try {
			if (this.request.getRequestQueue() != null) {
				this.request.getRequestQueue();
				request.getRequestQueue().updataFinishedTag(request.getTag());
			}
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onStart() {
		super.onStart();
	}

	public void onSuccess(JSONObject response) {
		super.onSuccess(response);
		if (response == null) {
			BaseActivity.getTopActivity().showDialog(1, "系统异常，请重新操作。");
			return;
		}

		try {
			Object obj = ParseResponseData.parse(this.request.getRequestId(),
					response);

			Log.e("success", request.getRequestId());
			successAction(obj);
			try {
				if (this.request.getRequestQueue() != null)
					request.getRequestQueue().updateCompletedTag(
							request.getTag());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (ServiceErrorException e) {
			e.printStackTrace();
			if (e.getErrorCode() == -1) {
				LKAlertDialog kdialog = new LKAlertDialog(
						BaseActivity.getTopActivity());
				kdialog.setTitle("提示");
				kdialog.setMessage("系统超时，请您重新登录");
				kdialog.setCancelable(false);
				kdialog.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int arg1) {
								// TODO Auto-generated method stub
								dialog.dismiss();
								try {
									while (true) {
										if ((BaseActivity.getTopActivity() instanceof LoginActivity))
											return;
										BaseActivity.getTopActivity().finish();
									}
								} catch (Exception localException) {
									localException.printStackTrace();
								}
							}

						});
				kdialog.create().show();
			}
			BaseActivity.getTopActivity().hideDialog(BaseActivity.ALL_DIALOG);
			BaseActivity.getTopActivity().showDialog(BaseActivity.MODAL_DIALOG,
					e.getMessage());
		}

	}

	public void setRequest(LKHttpRequest paramLKHttpRequest) {
		this.request = paramLKHttpRequest;
	}

	public abstract void successAction(Object paramObject);
}
