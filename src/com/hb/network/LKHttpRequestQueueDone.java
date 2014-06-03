package com.hb.network;

import android.util.Log;
import com.hb.activity.BaseActivity;
import java.util.ArrayList;

public class LKHttpRequestQueueDone
{
  private LKHttpRequestQueue queue;

  public void onComplete()
  {
    int i = Log.e("QueueDone Complete", "队列内请求全部执行 成功。。。");
  }

  public void onFinish()
  {
    int i = Log.e("QueueDone Finish", "队列内请求全部执行 完成。。。");
    ArrayList localArrayList = LKHttpRequestQueue.queueList;
    LKHttpRequestQueue localLKHttpRequestQueue = this.queue;
    boolean bool = localArrayList.remove(localLKHttpRequestQueue);
    try
    {
      BaseActivity.getTopActivity().hideDialog(2);
      return;
    }
    catch (Exception localException)
    {
      while (true)
        localException.printStackTrace();
    }
  }

  public void setRequestQueue(LKHttpRequestQueue paramLKHttpRequestQueue)
  {
    this.queue = paramLKHttpRequestQueue;
  }
}
