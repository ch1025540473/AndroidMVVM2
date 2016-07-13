package com.mx.engine.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mx.engine.R;
import com.mx.engine.utils.ScreenUtils;


/**
 * 通用标题栏
 * <p/>
 * android:fitsSystemWindows="true"
 * android:clipToPadding="false"
 * android:clipToChild="false"
 * <p/>
 * <declare-styleable name="GCommonTitleBar">
 * <attr name="titleBarBg" format="reference" /> <!-- 标题栏背景 图片或颜色 -->
 * <attr name="titleBarHeight" format="dimension" /> <!-- 标题栏高度 -->
 * <attr name="showBottomLine" format="boolean" /> <!-- 显示标题栏分割线 -->
 * <attr name="bottomLineColor" format="color" /> <!-- 标题栏分割线颜色 -->
 * <attr name="bottomElevation" format="dimension" /> <!-- 显示elevation效果 默认根据系统版本加入 -->
 * <p/>
 * <!-- 左边视图类型 -->
 * <attr name="leftType">
 * <enum name="none" value="0" />
 * <enum name="textView" value="1" />
 * <enum name="imageBotton" value="2" />
 * <enum name="customView" value="3" />
 * </attr>
 * <p/>
 * <attr name="leftText" format="string" /> <!-- TextView 文字, 对应leftType_TextView -->
 * <attr name="leftTextColor" format="color" /> <!-- TextView 颜色, 对应leftType_TextView -->
 * <attr name="leftTextSize" format="dimension" /> <!-- TextView 字体大小, 对应leftType_TextView -->
 * <attr name="leftDrawable" format="reference" /> <!-- TextView 左边图片, 对应leftType_TextView -->
 * <attr name="leftDrawablePadding" format="dimension" /> <!-- TextView 左边padding, 对应leftType_TextView -->
 * <attr name="leftImageResource" format="reference" /> <!-- ImageView 资源, 对应leftType_ImageBotton -->
 * <attr name="leftCustomView" format="reference" /> <!-- 左边自定义布局, 对应leftType_CustomView -->
 * <p/>
 * <!-- 右边视图类型 -->
 * <attr name="rightType">
 * <enum name="none" value="0" />
 * <enum name="textView" value="1" />
 * <enum name="imageBotton" value="2" />
 * <enum name="customView" value="3" />
 * </attr>
 * <p/>
 * <attr name="rightText" format="string" /> <!-- TextView 文字, 对应rightType_TextView -->
 * <attr name="rightTextColor" format="color" /> <!-- TextView 颜色, 对应rightType_TextView -->
 * <attr name="rightTextSize" format="dimension" /> <!-- TextView 字体大小, 对应rightType_TextView -->
 * <attr name="rightImageResource" format="reference" /> <!-- ImageView 资源, 对应rightType_ImageBotton -->
 * <attr name="rightCustomView" format="reference" /> <!-- 右边自定义布局, 对应rightType_CustomView -->
 * <p/>
 * <!-- 中间视图类型 -->
 * <enum name="none" value="0" />
 * <enum name="textView" value="1" />
 * <enum name="searchView" value="2" />
 * <enum name="customView" value="3" />
 * </attr>
 * <p/>
 * <attr name="centerText" format="string" /> <!-- TextView 文字, 对应centerType_TextView -->
 * <attr name="centerTextColor" format="color" /> <!-- TextView 颜色, 对应centerType_TextView -->
 * <attr name="centerTextSize" format="dimension" /> <!-- TextView 字体大小, 对应centerType_TextView -->
 * <attr name="centerSubText" format="string" /> <!-- 子标题TextView 文字, 对应centerType_TextView -->
 * <attr name="centerSubTextColor" format="color" /> <!-- 子标题TextView 颜色, 对应centerType_TextView -->
 * <attr name="centerSubTextSize" format="dimension" /> <!-- 子标题TextView 字体大小, 对应centerType_TextView -->
 * <attr name="centerSearchEdiable" format="boolean" /> <!-- 搜索输入框是否可输入 -->
 * <attr name="centerCustomView" format="reference" /> <!-- 中间自定义布局, 对应centerType_CustomView -->
 * </declare-styleable>
 * <p/>
 * Created by wuhenzhizao on 16/1/12.
 */
@SuppressWarnings("ResourceType")
public class TitleBar extends RelativeLayout implements View.OnClickListener {
    private View viewStatusBarFill;                     // 状态栏填充视图
    private View viewBottomLine;                        // 分隔线视图
    private RelativeLayout rlMain;                      // 主视图

    private TextView tvLeft;                            // 左边TextView
    private ImageButton btnLeft;                        // 左边ImageButton
    private View viewCustomLeft;
    private TextView tvRight;                           // 右边TextView
    private ImageButton btnRight;                       // 右边ImageButton
    private View viewCustomRight;
    private LinearLayout llMainCenter;
    private TextView tvCenter;                          // 标题栏文字
    private TextView tvCenterSub;                       // 副标题栏文字
    private ProgressBar progressCenter;                 // 中间进度条,默认隐藏
    private EditText etSearchHint;
    private ImageView ivSearch;
    private ImageView ivVoice;
    private View centerCustomView;                      // 中间自定义视图

    private boolean fillStatusBar;                      // 是否撑起状态栏, true时,标题栏浸入状态栏
    private int titleBarColor;                          // 标题栏背景颜色
    private int titleBarHeight;                         // 标题栏高度

    private boolean showBottomLine;                     // 是否显示底部分割线
    private int bottomLineColor;                        // 分割线颜色
    private float bottomElevation;                      // elevation效果深度

    private int leftType;                               // 左边视图类型
    private String leftText;                            // 左边TextView文字
    private int leftTextColor;                          // 左边TextView颜色
    private float leftTextSize;                         // 左边TextView文字大小
    private int leftDrawable;                           // 左边TextView drawableLeft资源
    private float leftDrawablePadding;                  // 左边TextView drawablePadding
    private int leftImageResource;                      // 左边图片资源
    private int leftCustomViewRes;                      // 左边自定义视图布局资源

    private int rightType;                              // 右边视图类型
    private String rightText;                           // 右边TextView文字
    private int rightTextColor;                         // 右边TextView颜色
    private float rightTextSize;                        // 右边TextView文字大小
    private int rightImageResource;                     // 右边图片资源
    private int rightCustomViewRes;                     // 右边自定义视图布局资源

    private int centerType;                             // 中间视图类型
    private String centerText;                          // 中间TextView文字
    private int centerTextColor;                        // 中间TextView字体颜色
    private float centerTextSize;                       // 中间TextView字体大小
    private String centerSubText;                       // 中间subTextView文字
    private int centerSubTextColor;                     // 中间subTextView字体颜色
    private float centerSubTextSize;                    // 中间subTextView字体大小
    private boolean centerSearchEdiable;                // 搜索框是否可输入
    private int centerSearchRightType;                  // 搜索框右边按钮类型  0: voice 1: delete
    private int centerCustomViewRes;                    // 中间自定义布局资源

    private int MARGIN_10;
    private int PADDING_5;
    private int PADDING_10;

    private OnTitleBarListener listener;
    private OnTitleBarDoubleClickListener doubleClickListener;

    private static final int TYPE_LEFT_NONE = 0;
    private static final int TYPE_LEFT_TEXTVIEW = 1;
    private static final int TYPE_LEFT_IMAGEBUTTON = 2;
    private static final int TYPE_LEFT_CUSTOM_VIEW = 3;
    private static final int TYPE_RIGHT_NONE = 0;
    private static final int TYPE_RIGHT_TEXTVIEW = 1;
    private static final int TYPE_RIGHT_IMAGEBUTTON = 2;
    private static final int TYPE_RIGHT_CUSTOM_VIEW = 3;
    private static final int TYPE_CENTER_NONE = 0;
    private static final int TYPE_CENTER_TEXTVIEW = 1;
    private static final int TYPE_CENTER_SEARCHVIEW = 2;
    private static final int TYPE_CENTER_CUSTOM_VIEW = 3;

    private static final int TYPE_CENTER_SEARCH_RIGHT_VOICE = 0;
    private static final int TYPE_CENTER_SEARCH_RIGHT_DELETE = 1;

    private static final int ID_STATUS_LAYOUT = 0x0001;
    private static final int ID_MAIN_LAYOUT = 0x0002;
    private static final int ID_LEFT_TEXTVIEW = 0x0011;
    private static final int ID_LEFT_IMAGEBUTTON = 0x0012;
    private static final int ID_RIGHT_TEXTVIEW = 0x0021;
    private static final int ID_RIGHT_IMAGEBUTTON = 0x0022;
    private static final int ID_SEARCH_ICON = 0x0031;
    private static final int ID_SEARCH_VOICE = 0x0032;

    private static final int ID_CENTER_LAYOUT = 0x0101;

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadAttributes(context, attrs);
        initGlobalViews(context);
        initMainViews(context);
    }

    private void loadAttributes(Context context, AttributeSet attrs) {
        MARGIN_10 = ScreenUtils.dp2PxInt(context, 10);
        PADDING_5 = ScreenUtils.dp2PxInt(context, 5);
        PADDING_10 = ScreenUtils.dp2PxInt(context, 10);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // notice 未引入沉浸式标题栏之前,隐藏标题栏撑起布局
//            fillStatusBar = true;
        }
        titleBarColor = array.getColor(R.styleable.TitleBar_titleBarColor, Color.parseColor("#f9f9f9"));
        titleBarHeight = (int) array.getDimension(R.styleable.TitleBar_titleBarHeight, ScreenUtils.dp2PxInt(context, 47));

        showBottomLine = array.getBoolean(R.styleable.TitleBar_showBottomLine, true);
        bottomLineColor = array.getColor(R.styleable.TitleBar_bottomLineColor, Color.parseColor("#cdcdcd"));
        bottomElevation = array.getDimension(R.styleable.TitleBar_bottomElevation, ScreenUtils.dp2PxInt(context, 5));

        leftType = array.getInt(R.styleable.TitleBar_leftType, TYPE_LEFT_NONE);
        if (leftType == TYPE_LEFT_TEXTVIEW) {
            leftText = array.getString(R.styleable.TitleBar_leftText);
            leftTextColor = array.getColor(R.styleable.TitleBar_leftTextColor, getResources().getColor(R.color.comm_title_text_selector));
            leftTextSize = array.getDimension(R.styleable.TitleBar_leftTextSize, ScreenUtils.dp2PxInt(context, 16));
            leftDrawable = array.getResourceId(R.styleable.TitleBar_leftDrawable, R.drawable.comm_titlebar_reback_selector);
            leftDrawablePadding = array.getDimension(R.styleable.TitleBar_leftDrawablePadding, 5);
        } else if (leftType == TYPE_LEFT_IMAGEBUTTON) {
            leftImageResource = array.getResourceId(R.styleable.TitleBar_leftImageResource, R.drawable.comm_titlebar_reback_selector);
        } else if (leftType == TYPE_LEFT_CUSTOM_VIEW) {
            leftCustomViewRes = array.getResourceId(R.styleable.TitleBar_leftCustomView, 0);
        }

        rightType = array.getInt(R.styleable.TitleBar_rightType, TYPE_RIGHT_NONE);
        if (rightType == TYPE_RIGHT_TEXTVIEW) {
            rightText = array.getString(R.styleable.TitleBar_rightText);
            rightTextColor = array.getColor(R.styleable.TitleBar_rightTextColor, getResources().getColor(R.color.comm_title_text_selector));
            rightTextSize = array.getDimension(R.styleable.TitleBar_rightTextSize, ScreenUtils.dp2PxInt(context, 16));
        } else if (rightType == TYPE_RIGHT_IMAGEBUTTON) {
            rightImageResource = array.getResourceId(R.styleable.TitleBar_rightImageResource, 0);
        } else if (rightType == TYPE_RIGHT_CUSTOM_VIEW) {
            rightCustomViewRes = array.getResourceId(R.styleable.TitleBar_rightCustomView, 0);
        }

        centerType = array.getInt(R.styleable.TitleBar_centerType, TYPE_CENTER_NONE);
        if (centerType == TYPE_CENTER_TEXTVIEW) {
            centerText = array.getString(R.styleable.TitleBar_centerText);
            centerTextColor = array.getColor(R.styleable.TitleBar_centerTextColor, Color.parseColor("#333333"));
            centerTextSize = array.getDimension(R.styleable.TitleBar_centerTextSize, ScreenUtils.dp2PxInt(context, 18));
            centerSubText = array.getString(R.styleable.TitleBar_centerSubText);
            centerSubTextColor = array.getColor(R.styleable.TitleBar_centerSubTextColor, Color.parseColor("#666666"));
            centerSubTextSize = array.getDimension(R.styleable.TitleBar_centerSubTextSize, ScreenUtils.dp2PxInt(context, 12));
        } else if (centerType == TYPE_CENTER_SEARCHVIEW) {
            centerSearchEdiable = array.getBoolean(R.styleable.TitleBar_centerSearchEdiable, true);
            centerSearchRightType = array.getInt(R.styleable.TitleBar_centerSearchRightType, TYPE_CENTER_SEARCH_RIGHT_VOICE);
        } else if (centerType == TYPE_CENTER_CUSTOM_VIEW) {
            centerCustomViewRes = array.getResourceId(R.styleable.TitleBar_centerCustomView, 0);
        }

        array.recycle();
    }

    private final int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;
    private final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;

    /**
     * 初始化全局视图
     *
     * @param context
     */
    private void initGlobalViews(Context context) {
        ViewGroup.LayoutParams globalParams = new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        setLayoutParams(globalParams);

        // 构建标题栏填充视图
        if (fillStatusBar) {
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            int statusBarHeight = (int) getResources().getDimension(resourceId);
            viewStatusBarFill = new View(context);
            viewStatusBarFill.setId(ID_STATUS_LAYOUT);
            viewStatusBarFill.setBackgroundColor(Color.parseColor("#ccf9f9f9"));
            LayoutParams statusBarParams = new LayoutParams(MATCH_PARENT, statusBarHeight);
            statusBarParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);

            addView(viewStatusBarFill, statusBarParams);
        }

        // 构建主视图
        rlMain = new RelativeLayout(context);
        rlMain.setId(ID_MAIN_LAYOUT);
        rlMain.setBackgroundColor(titleBarColor);
        LayoutParams mainParams = new LayoutParams(MATCH_PARENT, titleBarHeight);
        if (fillStatusBar) {
            mainParams.addRule(RelativeLayout.BELOW, ID_STATUS_LAYOUT);
        } else {
            mainParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        }

        mainParams.height = titleBarHeight - Math.max(1, ScreenUtils.dp2PxInt(context, 0.4f));

        // Notice 此处暂时屏蔽阴影效果
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        // 5.0及以后机型,标题栏下方显示阴影效果
//            rlMain.setElevation(bottomElevation);
//            mainParams.bottomMargin = (int) bottomElevation;
//        }
        // 计算主布局高度
//        if (showBottomLine && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//            mainParams.height = titleBarHeight - Math.max(1, ScreenUtils.dp2PxInt(context, 0.4f));
//        } else {
//            mainParams.height = titleBarHeight;
//        }
        addView(rlMain, mainParams);

        // 构建分割线视图
//        if (showBottomLine && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        // 已设置显示标题栏分隔线,5.0以下机型,显示分隔线
        viewBottomLine = new View(context);
        viewBottomLine.setBackgroundColor(bottomLineColor);
        LayoutParams bottomLineParams = new LayoutParams(MATCH_PARENT, Math.max(1, ScreenUtils.dp2PxInt(context, 0.4f)));
        bottomLineParams.addRule(RelativeLayout.BELOW, ID_MAIN_LAYOUT);

        addView(viewBottomLine, bottomLineParams);
//        }
    }

    /**
     * 初始化主视图
     *
     * @param context
     */
    private void initMainViews(Context context) {
        if (leftType != TYPE_LEFT_NONE) {
            initMainLeftViews(context);
        }
        if (rightType != TYPE_RIGHT_NONE) {
            initMainRightViews(context);
        }
        if (centerType != TYPE_CENTER_NONE) {
            initMainCenterViews(context);
        }
    }

    /**
     * 初始化主视图左边部分
     *
     * @param context
     */
    private void initMainLeftViews(Context context) {
        LayoutParams leftInnerParams = new LayoutParams(WRAP_CONTENT, MATCH_PARENT);
        leftInnerParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        leftInnerParams.addRule(RelativeLayout.CENTER_VERTICAL);

        if (leftType == TYPE_LEFT_TEXTVIEW) {
            // 初始化左边TextView
            tvLeft = new TextView(context);
            tvLeft.setId(ID_LEFT_TEXTVIEW);
            tvLeft.setText(leftText);
            tvLeft.setTextColor(leftTextColor);
            tvLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, leftTextSize);
            tvLeft.setBackgroundResource(R.drawable.comm_titlebar_layout_selector);
            tvLeft.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            tvLeft.setSingleLine(true);
            tvLeft.setOnClickListener(this);
            // 设置DrawableLeft及DrawablePadding
            if (leftDrawable != 0) {
                tvLeft.setCompoundDrawablePadding((int) leftDrawablePadding);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    tvLeft.setCompoundDrawablesRelativeWithIntrinsicBounds(leftDrawable, 0, 0, 0);
                } else {
                    tvLeft.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, 0, 0, 0);
                }
                tvLeft.setPadding(0, 0, PADDING_5, 0);
            } else {
                tvLeft.setPadding(PADDING_5, 0, PADDING_5, 0);
            }

            rlMain.addView(tvLeft, leftInnerParams);

        } else if (leftType == TYPE_LEFT_IMAGEBUTTON) {
            // 初始化左边ImageButton
            btnLeft = new ImageButton(context);
            btnLeft.setId(ID_LEFT_IMAGEBUTTON);
            btnLeft.setBackgroundResource(R.drawable.comm_titlebar_layout_selector);
            btnLeft.setImageResource(leftImageResource);
//            btnLeft.setMinimumWidth(ScreenUtils.dp2PxInt(context, 40));
//            btnLeft.setScaleType(ImageView.ScaleType.CENTER);
            btnLeft.setPadding(PADDING_10, 0, ScreenUtils.dp2PxInt(context, 15), 0);
            btnLeft.setOnClickListener(this);

            rlMain.addView(btnLeft, leftInnerParams);

        } else if (leftType == TYPE_LEFT_CUSTOM_VIEW) {
            // 初始化自定义View
            viewCustomLeft = LayoutInflater.from(context).inflate(leftCustomViewRes, null);
            rlMain.addView(viewCustomLeft, leftInnerParams);
        }
    }

    /**
     * 初始化主视图右边部分
     *
     * @param context
     */
    private void initMainRightViews(Context context) {
        LayoutParams rightInnerParams = new LayoutParams(WRAP_CONTENT, MATCH_PARENT);
        rightInnerParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        rightInnerParams.addRule(RelativeLayout.CENTER_VERTICAL);

        if (rightType == TYPE_RIGHT_TEXTVIEW) {
            // 初始化右边TextView
            tvRight = new TextView(context);
            tvRight.setId(ID_RIGHT_TEXTVIEW);
            tvRight.setText(rightText);
            tvRight.setTextColor(rightTextColor);
            tvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, rightTextSize);
            tvRight.setBackgroundResource(R.drawable.comm_titlebar_layout_selector);
            tvRight.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            tvRight.setSingleLine(true);
            tvRight.setPadding(PADDING_10, 0, PADDING_10, 0);
            tvRight.setOnClickListener(this);

            rlMain.addView(tvRight, rightInnerParams);

        } else if (rightType == TYPE_RIGHT_IMAGEBUTTON) {
            // 初始化右边ImageBtn
            btnRight = new ImageButton(context);
            btnRight.setId(ID_RIGHT_IMAGEBUTTON);
            btnRight.setBackgroundResource(R.drawable.comm_titlebar_layout_selector);
            btnRight.setImageResource(rightImageResource);
            btnRight.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            btnRight.setPadding(PADDING_10, 0, PADDING_10, 0);
            btnRight.setOnClickListener(this);

            rlMain.addView(btnRight, rightInnerParams);

        } else if (rightType == TYPE_RIGHT_CUSTOM_VIEW) {
            // 初始化自定义view
            viewCustomRight = LayoutInflater.from(context).inflate(rightCustomViewRes, null);
            rlMain.addView(viewCustomRight, rightInnerParams);
        }
    }

    /**
     * 初始化主视图中间部分
     *
     * @param context
     */
    private void initMainCenterViews(Context context) {
        if (centerType == TYPE_CENTER_TEXTVIEW) {
            // 初始化中间子布局
            llMainCenter = new LinearLayout(context);
            llMainCenter.setId(ID_CENTER_LAYOUT);
            llMainCenter.setGravity(Gravity.CENTER);
            llMainCenter.setOrientation(LinearLayout.VERTICAL);
            llMainCenter.setMinimumWidth(ScreenUtils.dp2PxInt(context, 200));
            llMainCenter.setOnClickListener(this);
            LayoutParams centerParams = new LayoutParams(WRAP_CONTENT, MATCH_PARENT);
            centerParams.leftMargin = MARGIN_10;
            centerParams.rightMargin = MARGIN_10;
            centerParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            rlMain.addView(llMainCenter, centerParams);

            // 初始化进度条, 显示于标题栏左边
            progressCenter = new ProgressBar(context);
            progressCenter.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress_draw));
            progressCenter.setVisibility(View.GONE);
            int progressWidth = ScreenUtils.dp2PxInt(context, 18);
            LayoutParams progressParams = new LayoutParams(progressWidth, progressWidth);
            progressParams.addRule(RelativeLayout.CENTER_VERTICAL);
            progressParams.addRule(RelativeLayout.LEFT_OF, ID_CENTER_LAYOUT);
            rlMain.addView(progressCenter, progressParams);

            // 初始化标题栏TextView
            tvCenter = new TextView(context);
            tvCenter.setText(centerText);
            tvCenter.setTextColor(centerTextColor);
            tvCenter.setTextSize(TypedValue.COMPLEX_UNIT_PX, centerTextSize);
            tvCenter.setGravity(Gravity.CENTER);
            tvCenter.setSingleLine(true);
            // 设置跑马灯效果
            tvCenter.setMaxWidth((int) (ScreenUtils.getScreenPixelSize(context)[0] * 3 / 5.0));
            tvCenter.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            tvCenter.setMarqueeRepeatLimit(-1);
            tvCenter.setFocusable(true);
            tvCenter.setFocusableInTouchMode(true);
            tvCenter.requestFocus();

            LinearLayout.LayoutParams centerTextParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
            llMainCenter.addView(tvCenter, centerTextParams);

            // 初始化副标题栏
            tvCenterSub = new TextView(context);
            tvCenterSub.setText(centerSubText);
            tvCenterSub.setTextColor(centerSubTextColor);
            tvCenterSub.setTextSize(TypedValue.COMPLEX_UNIT_PX, centerSubTextSize);
            tvCenterSub.setGravity(Gravity.CENTER);
            tvCenterSub.setSingleLine(true);
            if (TextUtils.isEmpty(centerSubText)) {
                tvCenterSub.setVisibility(View.GONE);
            }

            LinearLayout.LayoutParams centerSubTextParams = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
            llMainCenter.addView(tvCenterSub, centerSubTextParams);
        } else if (centerType == TYPE_CENTER_SEARCHVIEW) {
            // 初始化通用搜索框
            RelativeLayout rlMainCenter = new RelativeLayout(context);
            rlMainCenter.setBackgroundResource(R.drawable.shape_light_gray_border_corner);
            LayoutParams centerParams = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
            // 设置边距
            centerParams.topMargin = ScreenUtils.dp2PxInt(context, 7);
            centerParams.bottomMargin = ScreenUtils.dp2PxInt(context, 7);
            // 根据左边的布局类型来设置边距,布局依赖规则
            if (leftType == TYPE_LEFT_TEXTVIEW) {
                centerParams.addRule(RelativeLayout.RIGHT_OF, ID_LEFT_TEXTVIEW);
                centerParams.leftMargin = PADDING_5;
            } else if (leftType == TYPE_LEFT_IMAGEBUTTON) {
                centerParams.addRule(RelativeLayout.RIGHT_OF, ID_LEFT_IMAGEBUTTON);
                centerParams.leftMargin = PADDING_5;
            } else if (leftType == TYPE_LEFT_CUSTOM_VIEW) {
                centerParams.addRule(RelativeLayout.RIGHT_OF, viewCustomLeft.getId());
                centerParams.leftMargin = PADDING_5;
            } else {
                centerParams.leftMargin = MARGIN_10;
            }
            // 根据右边的布局类型来设置边距,布局依赖规则
            if (rightType == TYPE_RIGHT_TEXTVIEW) {
                centerParams.addRule(RelativeLayout.LEFT_OF, ID_RIGHT_TEXTVIEW);
                centerParams.rightMargin = PADDING_5;
            } else if (rightType == TYPE_RIGHT_IMAGEBUTTON) {
                centerParams.addRule(RelativeLayout.LEFT_OF, ID_RIGHT_IMAGEBUTTON);
                centerParams.rightMargin = PADDING_5;
            } else if (rightType == TYPE_RIGHT_CUSTOM_VIEW) {
                centerParams.addRule(RelativeLayout.LEFT_OF, viewCustomRight.getId());
                centerParams.rightMargin = PADDING_5;
            } else {
                centerParams.rightMargin = MARGIN_10;
            }
            rlMain.addView(rlMainCenter, centerParams);

            // 初始化搜索框搜索ImageView
            ivSearch = new ImageView(context);
            ivSearch.setId(ID_SEARCH_ICON);
            ivSearch.setOnClickListener(this);
            int searchIconWidth = ScreenUtils.dp2PxInt(context, 18);
            LayoutParams searchParams = new LayoutParams(searchIconWidth, searchIconWidth);
            searchParams.addRule(RelativeLayout.CENTER_VERTICAL);
            searchParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            searchParams.leftMargin = MARGIN_10;
            rlMainCenter.addView(ivSearch, searchParams);
            ivSearch.setImageResource(R.drawable.comm_search_normal);

            // 初始化搜索框语音ImageView
            ivVoice = new ImageView(context);
            ivVoice.setId(ID_SEARCH_VOICE);
            ivVoice.setOnClickListener(this);
            LayoutParams voiceParams = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
            voiceParams.addRule(RelativeLayout.CENTER_VERTICAL);
            voiceParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            voiceParams.rightMargin = MARGIN_10;
            rlMainCenter.addView(ivVoice, voiceParams);
            if (centerSearchRightType == TYPE_CENTER_SEARCH_RIGHT_VOICE) {
                ivVoice.setImageResource(R.drawable.comm_voice_selector);
            } else {
                ivVoice.setImageResource(R.drawable.comm_delete_selector);
                ivVoice.setVisibility(View.GONE);
            }

            // 初始化文字输入框
            etSearchHint = new EditText(context);
            etSearchHint.setBackgroundColor(Color.TRANSPARENT);
            etSearchHint.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            etSearchHint.setHint("搜索");
            etSearchHint.setTextColor(Color.parseColor("#666666"));
            etSearchHint.setHintTextColor(Color.parseColor("#999999"));
            etSearchHint.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenUtils.dp2PxInt(context, 14));
            etSearchHint.setPadding(PADDING_5, 0, PADDING_5, 0);
            if (!centerSearchEdiable) {
                etSearchHint.setOnClickListener(this);
            }
            etSearchHint.setSingleLine(true);
            etSearchHint.setEllipsize(TextUtils.TruncateAt.END);
            etSearchHint.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
            etSearchHint.addTextChangedListener(centerSearchWatcher);
            etSearchHint.setOnEditorActionListener(editorActionListener);
            LayoutParams searchHintParams = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
            searchHintParams.addRule(RelativeLayout.RIGHT_OF, ID_SEARCH_ICON);
            searchHintParams.addRule(RelativeLayout.LEFT_OF, ID_SEARCH_VOICE);
            searchHintParams.addRule(RelativeLayout.CENTER_VERTICAL);
            searchHintParams.leftMargin = PADDING_5;
            searchHintParams.rightMargin = PADDING_5;
            rlMainCenter.addView(etSearchHint, searchHintParams);

        } else if (centerType == TYPE_CENTER_CUSTOM_VIEW) {
            // 初始化中间自定义布局
            centerCustomView = LayoutInflater.from(context).inflate(centerCustomViewRes, null);
            LayoutParams centerCustomParams = new LayoutParams(WRAP_CONTENT, MATCH_PARENT);
            centerCustomParams.leftMargin = MARGIN_10;
            centerCustomParams.rightMargin = MARGIN_10;
            centerCustomParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            rlMain.addView(centerCustomView, centerCustomParams);
        }
    }

    private TextWatcher centerSearchWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (centerSearchRightType == TYPE_CENTER_SEARCH_RIGHT_VOICE) {
                if (TextUtils.isEmpty(s)) {
                    ivVoice.setImageResource(R.drawable.comm_voice_selector);
                } else {
                    ivVoice.setImageResource(R.drawable.comm_delete_selector);
                }
            } else {
                if (TextUtils.isEmpty(s)) {
                    ivVoice.setVisibility(View.GONE);
                } else {
                    ivVoice.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (listener != null && actionId == EditorInfo.IME_ACTION_SEARCH) {
                listener.onClicked(v, ACTION_SEARCH_SUBMIT, etSearchHint.getText().toString());
            }
            return false;
        }
    };

    private long lastClickMillis = 0;     // 双击事件中，上次被点击时间

    @Override
    public void onClick(View v) {
        if (listener == null) return;

        if (v.equals(llMainCenter) && doubleClickListener != null) {
            long currentClickMillis = System.currentTimeMillis();
            if (currentClickMillis - lastClickMillis < 500) {
                doubleClickListener.onClicked(v);
            }
            lastClickMillis = currentClickMillis;
        } else if (v.equals(tvLeft)) {
            listener.onClicked(v, ACTION_LEFT_TEXT, null);
        } else if (v.equals(btnLeft)) {
            listener.onClicked(v, ACTION_LEFT_BUTTON, null);
        } else if (v.equals(tvRight)) {
            listener.onClicked(v, ACTION_RIGHT_TEXT, null);
        } else if (v.equals(btnRight)) {
            listener.onClicked(v, ACTION_RIGHT_BUTTON, null);
        } else if (v.equals(etSearchHint) || v.equals(ivSearch)) {
            listener.onClicked(v, ACTION_SEARCH, null);
        } else if (v.equals(ivVoice)) {
            etSearchHint.setText("");
            if (centerSearchRightType == TYPE_CENTER_SEARCH_RIGHT_VOICE) {
                // 语音按钮被点击
                listener.onClicked(v, ACTION_SEARCH_VOICE, null);
            } else {
                listener.onClicked(v, ACTION_SEARCH_DELETE, null);
            }
        } else if (v.equals(tvCenter)) {
            listener.onClicked(v, ACTION_CENTER_TEXT, null);
        }
    }

    /**
     * 设置背景颜色
     *
     * @param color
     */
    public void setBackgroundColor(int color) {
        rlMain.setBackgroundColor(color);
    }

    /**
     * 设置状态栏颜色
     *
     * @param color
     */
    public void setStatusBarColor(int color) {
        if (viewStatusBarFill != null) {
            viewStatusBarFill.setBackgroundColor(color);
        }
    }

    /**
     * 是否填充状态栏
     *
     * @param show
     */
    public void showStatusBar(boolean show) {
        if (viewStatusBarFill != null) {
            viewStatusBarFill.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 获取标题栏底部横线
     *
     * @return
     */
    public View getButtomLine() {
        return viewBottomLine;
    }

    /**
     * 获取标题栏左边TextView，对应leftType = textView
     *
     * @return
     */
    public TextView getLeftTextView() {
        return tvLeft;
    }

    /**
     * 获取标题栏左边ImageButton，对应leftType = imageButton
     *
     * @return
     */
    public ImageButton getLeftImageButton() {
        return btnLeft;
    }

    /**
     * 获取标题栏右边TextView，对应rightType = textView
     *
     * @return
     */
    public TextView getRightTextView() {
        return tvRight;
    }

    /**
     * 获取标题栏右边ImageButton，对应rightType = imageButton
     *
     * @return
     */
    public ImageButton getRightImageButton() {
        return btnRight;
    }

    /**
     * 获取标题栏中间TextView，对应centerType = textView
     *
     * @return
     */
    public TextView getCenterTextView() {
        return tvCenter;
    }

    public TextView getCenterSubTextView() {
        return tvCenterSub;
    }

    /**
     * 获取搜索框内部输入框，对应centerType = searchView
     *
     * @return
     */
    public EditText getCenterSearchEditText() {
        return etSearchHint;
    }

    /**
     * 获取搜索框右边图标ImageView，对应centerType = searchView
     *
     * @return
     */
    public ImageView getCenterSearchRightImageView() {
        return ivVoice;
    }

    /**
     * 获取左边自定义布局
     *
     * @return
     */
    public View getLeftCustomView() {
        return viewCustomLeft;
    }

    /**
     * 获取右边自定义布局
     *
     * @return
     */
    public View getRightCustomView() {
        return viewCustomRight;
    }

    /**
     * 获取中间自定义布局视图
     *
     * @return
     */
    public View getCenterCustomView() {
        return centerCustomView;
    }

    /**
     * @param leftView
     * @TODO 设置左边自定义布局
     */
    public void setLeftView(View leftView) {
    }

    /**
     * @param centerView
     * @TODO 设置中间自定义布局
     */
    public void setCenterView(View centerView) {
    }

    /**
     * @param rightView
     * @TODO 设置右边自定义布局
     */
    public void setRightView(View rightView) {
    }

    /**
     * 显示中间进度条
     */
    public void showCenterProgress() {
        progressCenter.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏中间进度条
     */
    public void dismissCenterProgress() {
        progressCenter.setVisibility(View.GONE);
    }

    /**
     * 显示或隐藏输入法,centerType="searchView"模式下有效
     *
     * @return
     */
    public void showSoftInputKeyboard(boolean show) {
        if (centerSearchEdiable && show) {
            etSearchHint.setFocusable(true);
            etSearchHint.setFocusableInTouchMode(true);
            etSearchHint.requestFocus();
            ScreenUtils.showSoftInputKeyBoard(getContext(), etSearchHint);
        } else {
            ScreenUtils.hideSoftInputKeyBoard(getContext(), etSearchHint);
        }
    }

    /**
     * 设置搜索框右边图标
     *
     * @param res
     */
    public void setSearchRightImageResource(int res) {
        if (ivVoice != null) {
            ivVoice.setImageResource(res);
        }
    }

    /**
     * 获取SearchView输入结果
     */
    public String getSearchKey() {
        if (etSearchHint != null) {
            return etSearchHint.getText().toString();
        }
        return "";
    }

    /**
     * 设置点击事件监听
     *
     * @param listener
     */

    public void setListener(OnTitleBarListener listener) {
        this.listener = listener;
    }

    public void setDoubleClickListener(OnTitleBarDoubleClickListener doubleClickListener) {
        this.doubleClickListener = doubleClickListener;
    }

    /**
     * 设置双击监听
     */


    public static final int ACTION_LEFT_TEXT = 1;        // 左边TextView被点击
    public static final int ACTION_LEFT_BUTTON = 2;      // 左边ImageBtn被点击
    public static final int ACTION_RIGHT_TEXT = 3;       // 右边TextView被点击
    public static final int ACTION_RIGHT_BUTTON = 4;     // 右边ImageBtn被点击
    public static final int ACTION_SEARCH = 5;           // 搜索框被点击,搜索框不可输入的状态下会被触发
    public static final int ACTION_SEARCH_SUBMIT = 6;    // 搜索框输入状态下,键盘提交触发
    public static final int ACTION_SEARCH_VOICE = 7;     // 语音按钮被点击
    public static final int ACTION_SEARCH_DELETE = 8;    // 搜索删除按钮被点击
    public static final int ACTION_CENTER_TEXT = 9;     // 中间文字点击

    /**
     * 点击事件
     */
    public interface OnTitleBarListener {
        /**
         * @param v
         * @param action 对应ACTION_XXX, 如ACTION_LEFT_TEXT
         * @param extra  中间为搜索框时,如果可输入,点击键盘的搜索按钮,会返回输入关键词
         */
        void onClicked(View v, int action, String extra);
    }

    /**
     * 标题栏双击事件监听
     */
    public interface OnTitleBarDoubleClickListener {
        void onClicked(View v);
    }
}
