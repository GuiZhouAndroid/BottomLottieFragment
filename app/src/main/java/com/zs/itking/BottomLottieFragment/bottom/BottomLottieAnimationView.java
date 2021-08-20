package com.zs.itking.BottomLottieFragment.bottom;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;

import com.airbnb.lottie.LottieAnimationView;

/**
 * created by on 2021/8/20
 * 描述：去除LottieAnimationView的缓存
 *
 * @author ZSAndroid
 * @create 2021-08-20-15:53
 */
public class BottomLottieAnimationView extends LottieAnimationView {

    public BottomLottieAnimationView(Context context) {
        super(context);
    }

    public BottomLottieAnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomLottieAnimationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 重写此方法将LottieAnimationView的缓存去除
     * 解决因异常情况或旋转方向后页面重新加载
     * 导致lottie文件读取成最后一个tab文件的bug
     *
     * @return
     */
    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable parcelable = super.onSaveInstanceState();
        parcelable = null;
        return null;
    }
}