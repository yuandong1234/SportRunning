package com.yuong.running.common.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.yuong.running.R;
import com.yuong.running.common.widget.CommonCenterDialog;

public class DialogUtils {

    public static Dialog showCommonDialog(Context context, String title, String content, boolean cancelable, CommonCallBack callBack) {
        return showOneButtonCommonDialog(context, title, content, "确定", cancelable, callBack);
    }

    public static Dialog showCommonDialog2(Context context, String title, String content, boolean cancelable, CommonCallBack2 callBack) {
        return showTwoButtonCommonDialog(context, title, content, "确定", "取消", cancelable, callBack);
    }

    /**
     * 通用弹框(1个按钮)
     */
    private static Dialog showOneButtonCommonDialog(Context context, String title, String content, String confirmTitle, boolean cancelable, final CommonCallBack callBack) {
        CommonCenterDialog.Builder builder = new CommonCenterDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_dialog_one, null);

        TextView tvTitle = view.findViewById(R.id.tv_title);
        TextView tvContent = view.findViewById(R.id.tv_content);
        TextView tvConfirm = view.findViewById(R.id.tv_confirm);

        tvTitle.setText(title);
        tvContent.setText(content);
        tvConfirm.setText(confirmTitle);
        final CommonCenterDialog dialog = builder.view(view)
                //.height(200)
                .style(R.style.theme_common_dialog)
                .cancelTouchOut(cancelable)
                .build();

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (callBack != null) {
                    callBack.onClick();
                }
            }
        });


        dialog.show();
        return dialog;
    }

    /**
     * 通用弹框(2个按钮)
     */
    public static Dialog showTwoButtonCommonDialog(Context context, String title, String content, String cancelTitle, String confirmTitle, boolean cancelable, final CommonCallBack2 callBack) {
        CommonCenterDialog.Builder builder = new CommonCenterDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_dialog_two, null);

        TextView tvTitle = view.findViewById(R.id.tv_title);
        TextView tvContent = view.findViewById(R.id.tv_content);
        TextView tvCancel = view.findViewById(R.id.tv_cancel);
        TextView tvConfirm = view.findViewById(R.id.tv_confirm);

        tvTitle.setText(title);
        tvContent.setText(content);
        tvCancel.setText(cancelTitle);
        tvConfirm.setText(confirmTitle);
        final CommonCenterDialog dialog = builder.view(view)
                //.height(200)
                .style(R.style.theme_common_dialog)
                .cancelTouchOut(cancelable)
                .build();
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (callBack != null) {
                    callBack.onLeftClick();
                }
            }
        });

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (callBack != null) {
                    callBack.onRightClick();
                }
            }
        });


        dialog.show();
        return dialog;
    }


    public interface CommonCallBack {
        void onClick();
    }

    public interface CommonCallBack2 {
        void onLeftClick();

        void onRightClick();
    }
}
