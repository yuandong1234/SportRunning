package com.yuong.running.common.activity;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.yuong.running.MyApplication;
import com.yuong.running.common.utils.LogUtils;
import com.yuong.running.common.widget.ProgressDialog;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public abstract class BaseActivity extends AppCompatActivity {

    protected String TAG = this.getClass().getSimpleName();
    protected boolean isActive = true; //是否活跃

    private InputMethodManager imm;

    private static final int DISMISS = 1001;
    private static final int SHOW = 1002;
    private ProgressDialog progressDialog = null;

    protected android.app.ProgressDialog mProgressDialog;

    protected InputMethodManager inputMethodManager;

    protected Context context;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW:
                    if (progressDialog != null) {
                        progressDialog.setTouchAble((Boolean) msg.obj);
                        progressDialog.show();
                    }
                    break;
                case DISMISS:
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        context = this;
        MyApplication.addActivity(this);

        progressDialog = new ProgressDialog(this);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        initView();
        initListener();
        initData(savedInstanceState);
    }

    public abstract int getLayoutId();

    public abstract void initData(Bundle savedInstanceState);

    public abstract void initView();

    public abstract void initListener();


    public void hideSoftKeyBoard() {
        View localView = getCurrentFocus();
        if (this.imm == null) {
            this.imm = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
        }
        if ((localView != null) && (this.imm != null)) {
            this.imm.hideSoftInputFromWindow(localView.getWindowToken(), 2);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        this.imm = null;

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = null;

        if (null != mHandler) {
            mHandler.removeMessages(DISMISS);
            mHandler.removeMessages(SHOW);
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }

        MyApplication.removeActivity(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    @Override
    protected void onStop() {
        super.onStop();
        hideProgress();
        mProgressDialog = null;
        if (!isAppOnForeground()) {
            //app 进入后台
            //全局变量 记录当前已经进入后台
            isActive = false;
            LogUtils.i(TAG, "进入后台");
        }
    }

    @SuppressLint("ShowToast")
    @Override
    public void onResume() {
        super.onResume();
        if (!isActive) {
            //app 从后台唤醒，进入前台
            isActive = true;
            LogUtils.i(TAG, "进入前台");
        }
    }

    @Override
    public void onPause() {

        hideSoftKeyBoard();

        super.onPause();

    }

    /**
     * 显示加载视图
     * <p>
     * true:可点击 false:不可点击 默认是可点击
     */
    protected void showLoadingView(boolean isTouchAble) {
        if (null != mHandler && !isShowingLoadingView()) {
            Message m = mHandler.obtainMessage(SHOW, isTouchAble);
            mHandler.sendMessage(m);
        }
    }

    protected void showLoadingView() {
        if (!isShowingLoadingView())
            showLoadingView(true);
    }

    /**
     * 关闭加载视图
     */
    protected void dismissLoadingView() {
        if (null != mHandler)
            mHandler.sendEmptyMessage(DISMISS);
    }

    protected boolean isShowingLoadingView() {
        if (progressDialog != null) {
            return progressDialog.isShowing();
        } else {
            return false;
        }
    }


    public android.app.ProgressDialog showProgress(String title, String message) {
        return showProgress(title, message, -1);
    }

    public android.app.ProgressDialog showProgress(String title, String message, int theme) {
        if (mProgressDialog == null) {
            if (theme > 0)
                mProgressDialog = new android.app.ProgressDialog(this, theme);
            else
                mProgressDialog = new android.app.ProgressDialog(this);
            mProgressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
            mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mProgressDialog.setCanceledOnTouchOutside(false);// 不能取消
            mProgressDialog.setIndeterminate(true);// 设置进度条是否不明确
        }

        if (!TextUtils.isEmpty(title))
            mProgressDialog.setTitle(title);
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
        return mProgressDialog;
    }

    public void hideProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    public void back(View v) {
        hideSoftKeyBoard();
        MyApplication.removeActivity(this);
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        hideSoftKeyBoard();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        LogUtils.i("memory -- info -->", level + "");
    }

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device

        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }
}
