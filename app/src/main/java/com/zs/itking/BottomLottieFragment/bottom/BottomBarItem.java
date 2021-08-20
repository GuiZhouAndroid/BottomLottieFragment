package com.zs.itking.BottomLottieFragment.bottom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zs.itking.BottomLottieFragment.R;

import java.util.Locale;

/**
 * created by on 2021/8/20
 * 描述：
 *
 * @author ZSAndroid
 * @create 2021-08-20-15:52
 */
public class BottomBarItem extends LinearLayout {

    private Context context;
    private Drawable normalIcon;//普通状态图标的资源id
    private Drawable selectedIcon;//选中状态图标的资源id
    private String title;//文本
    private boolean titleTextBold = true;//文字加粗
    private int titleTextSize = 12;//文字大小 默认为12sp
    private int titleNormalColor;    //描述文本的默认显示颜色
    private int titleSelectedColor;  //述文本的默认选中显示颜色
    private int marginTop = 0;//文字和图标的距离,默认0dp
    private boolean openTouchBg = false;// 是否开启触摸背景，默认关闭
    private Drawable touchDrawable;//触摸时的背景
    private int iconWidth;//图标的宽度
    private int iconHeight;//图标的高度
    private int itemPadding;//BottomBarItem的padding
    private int unreadTextSize = 9; //未读数默认字体大小10sp
    private int unreadNumThreshold = 99;//未读数阈值
    private int unreadTextColor;//未读数字体颜色
    private Drawable unreadTextBg;//未读数字体背景
    private int msgTextSize = 8; //消息默认字体大小8sp
    private int msgTextColor;//消息文字颜色
    private Drawable msgTextBg;//消息文字背景
    private Drawable notifyPointBg;//小红点背景
    private String lottieJson; //lottie文件名
    private boolean useLottie;


    private ImageView mImageView;
    private BottomLottieAnimationView mLottieView;
    private TextView mTvUnread;
    private TextView mTvNotify;
    private TextView mTvMsg;
    private TextView mTextView;

    public BottomBarItem(Context context) {
        super(context);
    }

    public BottomBarItem(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomBarItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BottomBarItem);

        initAttrs(ta); //初始化属性

        ta.recycle();

        checkValues();//检查值是否合法

        init();//初始化相关操作
    }

    private void initAttrs(TypedArray ta) {
        normalIcon = ta.getDrawable(R.styleable.BottomBarItem_iconNormal);
        selectedIcon = ta.getDrawable(R.styleable.BottomBarItem_iconSelected);

        title = ta.getString(R.styleable.BottomBarItem_itemText);
        titleTextBold = ta.getBoolean(R.styleable.BottomBarItem_itemTextBold, titleTextBold);
        titleTextSize = ta.getDimensionPixelSize(R.styleable.BottomBarItem_itemTextSize, BottomUIUtils.sp2px(context, titleTextSize));

        titleNormalColor = ta.getColor(R.styleable.BottomBarItem_textColorNormal, BottomUIUtils.getColor(context, R.color.bbl_999999));
        titleSelectedColor = ta.getColor(R.styleable.BottomBarItem_textColorSelected, BottomUIUtils.getColor(context, R.color.bbl_ff0000));

        marginTop = ta.getDimensionPixelSize(R.styleable.BottomBarItem_itemMarginTop, BottomUIUtils.dip2Px(context, marginTop));

        openTouchBg = ta.getBoolean(R.styleable.BottomBarItem_openTouchBg, openTouchBg);
        touchDrawable = ta.getDrawable(R.styleable.BottomBarItem_touchDrawable);

        iconWidth = ta.getDimensionPixelSize(R.styleable.BottomBarItem_iconWidth, 0);
        iconHeight = ta.getDimensionPixelSize(R.styleable.BottomBarItem_iconHeight, 0);
        itemPadding = ta.getDimensionPixelSize(R.styleable.BottomBarItem_itemPadding, 0);

        unreadTextSize = ta.getDimensionPixelSize(R.styleable.BottomBarItem_unreadTextSize, BottomUIUtils.sp2px(context, unreadTextSize));
        unreadTextColor = ta.getColor(R.styleable.BottomBarItem_unreadTextColor, BottomUIUtils.getColor(context, R.color.white));
        unreadTextBg = ta.getDrawable(R.styleable.BottomBarItem_unreadTextBg);

        msgTextSize = ta.getDimensionPixelSize(R.styleable.BottomBarItem_msgTextSize, BottomUIUtils.sp2px(context, msgTextSize));
        msgTextColor = ta.getColor(R.styleable.BottomBarItem_msgTextColor, BottomUIUtils.getColor(context, R.color.white));
        msgTextBg = ta.getDrawable(R.styleable.BottomBarItem_msgTextBg);

        notifyPointBg = ta.getDrawable(R.styleable.BottomBarItem_notifyPointBg);

        unreadNumThreshold = ta.getInteger(R.styleable.BottomBarItem_unreadThreshold, unreadNumThreshold);

        lottieJson = ta.getString(R.styleable.BottomBarItem_lottieJson);
        useLottie = !TextUtils.isEmpty(lottieJson);
    }

    /**
     * 检查传入的值是否完善
     */
    private void checkValues() {
        if (!useLottie && normalIcon == null) {
            throw new IllegalStateException("You have not set the normal icon");
        }

        if (!useLottie && selectedIcon == null) {
            throw new IllegalStateException("You have not set the selected icon");
        }

        if (openTouchBg && touchDrawable == null) {
            //如果有开启触摸背景效果但是没有传对应的drawable
            throw new IllegalStateException("Touch effect is turned on, but touchDrawable is not specified");
        }

        if (unreadTextBg == null) {
            unreadTextBg = getResources().getDrawable(R.drawable.bottom_shape_unread);
        }

        if (msgTextBg == null) {
            msgTextBg = getResources().getDrawable(R.drawable.bottom_shape_msg);
        }

        if (notifyPointBg == null) {
            notifyPointBg = getResources().getDrawable(R.drawable.bottom_shape_notify_point);
        }
    }

    private void init() {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);

        View view = initView();

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mImageView.getLayoutParams();
        if (iconWidth != 0 && iconHeight != 0) {
            //如果有设置图标的宽度和高度，则设置ImageView的宽高
            layoutParams.width = iconWidth;
            layoutParams.height = iconHeight;
        }

        if (useLottie) {
            mLottieView.setLayoutParams(layoutParams);
            mLottieView.setAnimation(lottieJson);
            mLottieView.setRepeatCount(0);
        } else {
            mImageView.setImageDrawable(normalIcon);
            mImageView.setLayoutParams(layoutParams);
        }

        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);//设置底部文字字体大小
        mTextView.getPaint().setFakeBoldText(titleTextBold);
        mTvUnread.setTextSize(TypedValue.COMPLEX_UNIT_PX, unreadTextSize);//设置未读数的字体大小
        mTvUnread.setTextColor(unreadTextColor);//设置未读数字体颜色
        mTvUnread.setBackground(unreadTextBg);//设置未读数背景

        mTvMsg.setTextSize(TypedValue.COMPLEX_UNIT_PX, msgTextSize);//设置提示文字的字体大小
        mTvMsg.setTextColor(msgTextColor);//设置提示文字的字体颜色
        mTvMsg.setBackground(msgTextBg);//设置提示文字的背景颜色

        mTvNotify.setBackground(notifyPointBg);//设置提示点的背景颜色

        mTextView.setTextColor(titleNormalColor);//设置底部文字字体颜色
        mTextView.setText(title);//设置标签文字

        LinearLayout.LayoutParams textLayoutParams = (LayoutParams) mTextView.getLayoutParams();
        textLayoutParams.topMargin = marginTop;
        mTextView.setLayoutParams(textLayoutParams);

        if (openTouchBg) {
            //如果有开启触摸背景
            setBackground(touchDrawable);
        }

        addView(view);
    }

    @NonNull
    private View initView() {
        View view = View.inflate(context, R.layout.bottom_bar_item, null);
        if (itemPadding != 0) {
            //如果有设置item的padding
            view.setPadding(itemPadding, itemPadding, itemPadding, itemPadding);
        }
        mImageView = view.findViewById(R.id.iv_icon);
        mLottieView = view.findViewById(R.id.lottieView);
        mTvUnread = view.findViewById(R.id.tv_unred_num);
        mTvMsg = view.findViewById(R.id.tv_msg);
        mTvNotify = view.findViewById(R.id.tv_point);
        mTextView = view.findViewById(R.id.tv_text);

        mImageView.setVisibility(useLottie ? GONE : VISIBLE);
        mLottieView.setVisibility(useLottie ? VISIBLE : GONE);

        return view;
    }

    public ImageView getImageView() {
        return mImageView;
    }

    public TextView getTextView() {
        return mTextView;
    }

    public void setNormalIcon(Drawable normalIcon) {
        this.normalIcon = normalIcon;
        refreshTab();
    }

    public void setNormalIcon(int resId) {
        setNormalIcon(BottomUIUtils.getDrawable(context, resId));
    }

    public void setSelectedIcon(Drawable selectedIcon) {
        this.selectedIcon = selectedIcon;
        refreshTab();
    }

    public void setSelectedIcon(int resId) {
        setSelectedIcon(BottomUIUtils.getDrawable(context, resId));
    }

    public void refreshTab(boolean isSelected) {
        setSelected(isSelected);
        refreshTab();
    }

    public void refreshTab() {
        if (useLottie) {
            if (isSelected()) {
                mLottieView.playAnimation();
            } else {
                //取消动画 进度设置为0
                mLottieView.cancelAnimation();
                mLottieView.setProgress(0);
            }
        } else {
            mImageView.setImageDrawable(isSelected() ? selectedIcon : normalIcon);
        }

        mTextView.setTextColor(isSelected() ? titleSelectedColor : titleNormalColor);
    }

    private void setTvVisible(TextView tv) {
        //都设置为不可见
        mTvUnread.setVisibility(GONE);
        mTvMsg.setVisibility(GONE);
        mTvNotify.setVisibility(GONE);

        tv.setVisibility(VISIBLE);//设置为可见
    }

    public int getUnreadNumThreshold() {
        return unreadNumThreshold;
    }

    public void setUnreadNumThreshold(int unreadNumThreshold) {
        this.unreadNumThreshold = unreadNumThreshold;
    }

    public void setUnreadNum(int unreadNum) {
        setTvVisible(mTvUnread);
        if (unreadNum <= 0) {
            mTvUnread.setVisibility(GONE);
        } else if (unreadNum <= unreadNumThreshold) {
            mTvUnread.setText(String.valueOf(unreadNum));
        } else {
            mTvUnread.setText(String.format(Locale.CHINA, "%d+", unreadNumThreshold));
        }
    }

    public void setMsg(String msg) {
        setTvVisible(mTvMsg);
        mTvMsg.setText(msg);
    }

    public void hideMsg() {
        mTvMsg.setVisibility(GONE);
    }

    public void showNotify() {
        setTvVisible(mTvNotify);
    }

    public void hideNotify() {
        mTvNotify.setVisibility(GONE);
    }

    public com.zs.itking.BottomLottieFragment.bottom.BottomBarItem create(com.zs.itking.BottomLottieFragment.bottom.BottomBarItem.Builder builder) {
        this.context = builder.context;
        this.normalIcon = builder.normalIcon;
        this.selectedIcon = builder.selectedIcon;
        this.title = builder.title;
        this.titleTextBold = builder.titleTextBold;
        this.titleTextSize = builder.titleTextSize;
        this.titleNormalColor = builder.titleNormalColor;
        this.titleSelectedColor = builder.titleSelectedColor;
        this.marginTop = builder.marginTop;
        this.openTouchBg = builder.openTouchBg;
        this.touchDrawable = builder.touchDrawable;
        this.iconWidth = builder.iconWidth;
        this.iconHeight = builder.iconHeight;
        this.itemPadding = builder.itemPadding;
        this.unreadTextSize = builder.unreadTextSize;
        this.unreadTextColor = builder.unreadTextColor;
        this.unreadTextBg = builder.unreadTextBg;
        this.unreadNumThreshold = builder.unreadNumThreshold;
        this.msgTextSize = builder.msgTextSize;
        this.msgTextColor = builder.msgTextColor;
        this.msgTextBg = builder.msgTextBg;
        this.notifyPointBg = builder.notifyPointBg;
        this.lottieJson = builder.lottieJson;

        checkValues();
        init();
        return this;
    }

    public static final class Builder {
        private Context context;
        private Drawable normalIcon;//普通状态图标的资源id
        private Drawable selectedIcon;//选中状态图标的资源id
        private String title;//标题
        private boolean titleTextBold;//文字加粗
        private int titleTextSize;//字体大小
        private int titleNormalColor;    //描述文本的默认显示颜色
        private int titleSelectedColor;  //述文本的默认选中显示颜色
        private int marginTop;//文字和图标的距离
        private boolean openTouchBg;// 是否开启触摸背景，默认关闭
        private Drawable touchDrawable;//触摸时的背景
        private int iconWidth;//图标的宽度
        private int iconHeight;//图标的高度
        private int itemPadding;//BottomBarItem的padding
        private int unreadTextSize; //未读数字体大小
        private int unreadNumThreshold;//未读数阈值
        private int unreadTextColor;//未读数字体颜色
        private Drawable unreadTextBg;//未读数文字背景
        private int msgTextSize; //消息字体大小
        private int msgTextColor;//消息文字颜色
        private Drawable msgTextBg;//消息提醒背景颜色
        private Drawable notifyPointBg;//小红点背景颜色
        private String lottieJson; //lottie文件名

        public Builder(Context context) {
            this.context = context;
            titleTextBold = false;
            titleTextSize = BottomUIUtils.sp2px(context, 12);
            titleNormalColor = getColor(R.color.bbl_999999);
            titleSelectedColor = getColor(R.color.bbl_ff0000);
            unreadTextSize = BottomUIUtils.sp2px(context, 10);
            msgTextSize = BottomUIUtils.sp2px(context, 6);
            unreadTextColor = getColor(R.color.white);
            unreadNumThreshold = 99;
            msgTextColor = getColor(R.color.white);
        }

        /**
         * 设置默认图标的resourceId
         */
        public BottomBarItem.Builder normalIcon(Drawable normalIcon) {
            this.normalIcon = normalIcon;
            return this;
        }

        /**
         * 设置选定图标的resourceId
         */
        public BottomBarItem.Builder selectedIcon(Drawable selectedIcon) {
            this.selectedIcon = selectedIcon;
            return this;
        }

        /**
         * 设置标题的resourceId
         */
        public BottomBarItem.Builder title(int titleId) {
            this.title = context.getString(titleId);
            return this;
        }

        /**
         * 设置标题字符串
         */
        public BottomBarItem.Builder title(String title) {
            this.title = title;
            return this;
        }

        /**
         * 设置标题的文本粗体
         */
        public BottomBarItem.Builder titleTextBold(boolean titleTextBold) {
            this.titleTextBold = titleTextBold;
            return this;
        }

        /**
         * 设置标题的文本大小
         */
        public BottomBarItem.Builder titleTextSize(int titleTextSize) {
            this.titleTextSize = BottomUIUtils.sp2px(context, titleTextSize);
            return this;
        }

        /**
         * 设置标题的正常颜色resourceId
         */
        public BottomBarItem.Builder titleNormalColor(int titleNormalColor) {
            this.titleNormalColor = getColor(titleNormalColor);
            return this;
        }

        /**
         * 设置标题的选定颜色resourceId
         */
        public BottomBarItem.Builder titleSelectedColor(int titleSelectedColor) {
            this.titleSelectedColor = getColor(titleSelectedColor);
            return this;
        }

        /**
         * 设置项目的边距顶部
         */
        public BottomBarItem.Builder marginTop(int marginTop) {
            this.marginTop = marginTop;
            return this;
        }

        /**
         * 设置是否打开触摸背景效果
         */
        public BottomBarItem.Builder openTouchBg(boolean openTouchBg) {
            this.openTouchBg = openTouchBg;
            return this;
        }

        /**
         * 设置触摸背景
         */
        public BottomBarItem.Builder touchDrawable(Drawable touchDrawable) {
            this.touchDrawable = touchDrawable;
            return this;
        }

        /**
         * 设置图标的宽度
         */
        public BottomBarItem.Builder iconWidth(int iconWidth) {
            this.iconWidth = iconWidth;
            return this;
        }

        /**
         * 设置图标的高度
         */
        public BottomBarItem.Builder iconHeight(int iconHeight) {
            this.iconHeight = iconHeight;
            return this;
        }


        /**
         * 设置项目的填充
         */
        public BottomBarItem.Builder itemPadding(int itemPadding) {
            this.itemPadding = itemPadding;
            return this;
        }

        /**
         * 设置未读字体大小
         */
        public BottomBarItem.Builder unreadTextSize(int unreadTextSize) {
            this.unreadTextSize = BottomUIUtils.sp2px(context, unreadTextSize);
            return this;
        }

        /**
         * 设置未读阵列阈值的个数大于要显示为n + n为设置的阈值
         */
        public BottomBarItem.Builder unreadNumThreshold(int unreadNumThreshold) {
            this.unreadNumThreshold = unreadNumThreshold;
            return this;
        }

        /**
         * 设置字体大小
         */
        public BottomBarItem.Builder msgTextSize(int msgTextSize) {
            this.msgTextSize = BottomUIUtils.sp2px(context, msgTextSize);
            return this;
        }

        /**
         * 设置消息字体背景
         */
        public BottomBarItem.Builder unreadTextBg(Drawable unreadTextBg) {
            this.unreadTextBg = unreadTextBg;
            return this;
        }

        /**
         * 设置未读字体颜色
         */
        public BottomBarItem.Builder unreadTextColor(int unreadTextColor) {
            this.unreadTextColor = getColor(unreadTextColor);
            return this;
        }

        /**
         * 设置消息字体颜色
         */
        public BottomBarItem.Builder msgTextColor(int msgTextColor) {
            this.msgTextColor = getColor(msgTextColor);
            return this;
        }

        /**
         * 设置消息字体背景
         */
        public BottomBarItem.Builder msgTextBg(Drawable msgTextBg) {
            this.msgTextBg = msgTextBg;
            return this;
        }

        /**
         * 设置消息提示点背景
         */
        public BottomBarItem.Builder notifyPointBg(Drawable notifyPointBg) {
            this.notifyPointBg = notifyPointBg;
            return this;
        }

        /**
         * 设置lottie json文件的名称
         */
        public BottomBarItem.Builder lottieJson(String lottieJson) {
            this.lottieJson = lottieJson;
            return this;
        }

        /**
         * 创建一个BottomBarItem对象
         */
        public com.zs.itking.BottomLottieFragment.bottom.BottomBarItem create(Drawable normalIcon, Drawable selectedIcon, String text) {
            this.normalIcon = normalIcon;
            this.selectedIcon = selectedIcon;
            title = text;

            BottomBarItem bottomBarItem = new BottomBarItem(context);
            return bottomBarItem.create(this);
        }

        public BottomBarItem create(int normalIconId, int selectedIconId, String text) {
            return create(BottomUIUtils.getDrawable(context, normalIconId), BottomUIUtils.getDrawable(context, selectedIconId), text);
        }

        private int getColor(int colorId) {
            return context.getResources().getColor(colorId);
        }
    }
}

