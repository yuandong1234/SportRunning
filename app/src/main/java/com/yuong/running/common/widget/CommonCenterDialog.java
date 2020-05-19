package com.yuong.running.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.yuong.running.R;

import androidx.annotation.DimenRes;

/**
 * 通用居中显示dialog
 */
public class CommonCenterDialog extends Dialog {
    private Context mContext;
    private int height, width;
    private boolean cancelTouchOut;
    private View view;


    private CommonCenterDialog(Builder builder) {
        this(builder, 0);
    }

    private CommonCenterDialog(Builder builder, int resStyle) {
        super(builder.mContext, resStyle);
        mContext = builder.mContext;
        height = builder.height;
        width = builder.width;
        cancelTouchOut = builder.cancelTouchOut;
        view = builder.view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(view);
        setCanceledOnTouchOutside(cancelTouchOut);
        setCancelable(cancelTouchOut);
        Window window = getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.dialog_common_window_anim); //设置窗口弹出动画
            WindowManager.LayoutParams lp = window.getAttributes();
            int screenHeight = getScreenHeight(mContext);//屏幕高度
            int screenWidth = getScreenWidth(mContext);//屏幕宽度
            if (height != 0) {
                lp.height = height;
            } else {
                lp.height = screenHeight;
            }
            if (width != 0) {
                lp.width = width;
            } else {
                lp.width = screenWidth;
            }
            lp.gravity = Gravity.CENTER;
            window.setAttributes(lp);
        }
    }

    public static final class Builder {
        private Context mContext;
        private int height, width;
        private boolean cancelTouchOut;
        private View view;
        private int resStyle = -1;


        public Builder(Context context) {
            mContext = context;
        }

        public Builder view(View view) {
            this.view = view;
            return this;
        }

        public Builder view(int layoutID) {
            view = LayoutInflater.from(mContext).inflate(layoutID, null);
            return this;
        }

        /**
         * @param val 单位dp
         * @return
         */
        public Builder height(int val) {
            height = dp2px(mContext, val);
            return this;
        }

        /**
         * @param val 单位dp
         * @return
         */
        public Builder width(int val) {
            width = dp2px(mContext, val);
            return this;
        }

        public Builder heightDimenRes(@DimenRes int dimenRes) {
            height = mContext.getResources().getDimensionPixelOffset(dimenRes);
            return this;
        }

        public Builder widthDimenRes(@DimenRes int dimenRes) {
            width = mContext.getResources().getDimensionPixelOffset(dimenRes);
            return this;
        }

        public Builder style(int resStyle) {
            this.resStyle = resStyle;
            return this;
        }

        public Builder cancelTouchOut(boolean cancelTouchOut) {
            this.cancelTouchOut = cancelTouchOut;
            return this;
        }

        public Builder addViewOnclick(int viewRes, View.OnClickListener listener) {
            view.findViewById(viewRes).setOnClickListener(listener);
            return this;
        }


        public CommonCenterDialog build() {
            if (resStyle != -1) {
                return new CommonCenterDialog(this, resStyle);
            } else {
                return new CommonCenterDialog(this);
            }
        }
    }

    /**
     * dp转px
     */
    private static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }


    /**
     * 获得屏幕高度
     */
    private int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        if (wm != null) {
            wm.getDefaultDisplay().getMetrics(outMetrics);
        }
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕宽度
     */
    private int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        if (wm != null) {
            wm.getDefaultDisplay().getMetrics(outMetrics);
        }
        return outMetrics.heightPixels;
    }
}
