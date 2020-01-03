package pub.devrel.easypermissions;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;

/**
 * @author JamesGZM
 * @description: 构造权限Dialog
 */
public class PermissionDialog extends Dialog {

    private Context mContext;
    private String content;
    private String positiveButton;
    private String negativeButton;
    private OnClickListener listener;

    public PermissionDialog(@NonNull Context context, String positiveButton, String negativeButton, String content, OnClickListener listener) {
        super(context);
        this.mContext = context;
        this.positiveButton = positiveButton;
        this.negativeButton = negativeButton;
        this.content = content;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();

        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    private void init() {
        final View view = getLayoutInflater().inflate(R.layout.dialog_permission, null, false);

        setContentView(view);

        Window window = getWindow();

        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = (int) (getScreenWidth() * 0.8); // 宽度设置为屏幕的0.6
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);

        FrameLayout.LayoutParams p = new FrameLayout.LayoutParams((int) (getScreenWidth() * 0.8),
                FrameLayout.LayoutParams.WRAP_CONTENT); // 获取对话框当前的参数值
        p.gravity = Gravity.CENTER;
        view.setLayoutParams(p);
        view.setMinimumWidth((int) (getScreenWidth() * 0.8));

        //内容
        TextView content_tv = view.findViewById(R.id.content_tv);
        content_tv.setText(content + "");
        //确定
        TextView i_got_it_tv = view.findViewById(R.id.i_got_it_tv);
        i_got_it_tv.setText(positiveButton + "");
        //取消
        TextView i_not_got_it_tv = view.findViewById(R.id.i_not_got_it_tv);
        i_not_got_it_tv.setText(negativeButton + "");
        i_got_it_tv.setOnClickListener(mOnClickListener);
        i_not_got_it_tv.setOnClickListener(mOnClickListener);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                float mLayerHeight = (float) view.getHeight();
                float maxHeight = getScreenHeight() * 0.8f;
                if (mLayerHeight > maxHeight) {
                    //如果大于屏幕高度的0.8
                    FrameLayout.LayoutParams p = (FrameLayout.LayoutParams) view.getLayoutParams();
                    p.height = (int) maxHeight;
                    view.setLayoutParams(p);
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
    }

    public int getScreenWidth() {
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public int getScreenHeight() {
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
            int vid = v.getId();
            if (vid == R.id.i_got_it_tv) {
                //点击确定
                if (listener != null) {
                    listener.onClick(PermissionDialog.this, Dialog.BUTTON_POSITIVE);
                }
            } else if (vid == R.id.i_not_got_it_tv) {
                //点击取消
                if (listener != null) {
                    listener.onClick(PermissionDialog.this, Dialog.BUTTON_NEGATIVE);
                }
            }

        }
    };
}
