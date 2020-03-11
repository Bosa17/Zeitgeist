package com.getcinderella.app.Utils.item;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.getcinderella.app.R;
import com.getcinderella.app.Utils.internal.Utils;

/**
 * 材料设计风格项
 */
public class MaterialItemView extends BaseTabItem {

    private final ImageView mIcon;

    private Drawable mDefaultDrawable;
    private Drawable mCheckedDrawable;

    private int mDefaultColor;
    private int mCheckedColor;

    private final float mTranslation;
    private final float mTranslationHideTitle;

    private final int mTopMargin;
    private final int mTopMarginHideTitle;

    private boolean mHideTitle;
    private boolean mChecked;

    private ValueAnimator mAnimator;
    private float mAnimatorValue = 1f;

    private boolean mIsMeasured = false;

    private boolean mTintIcon = true;

    public MaterialItemView(@NonNull Context context) {
        this(context, null);
    }

    public MaterialItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaterialItemView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final float scale = context.getResources().getDisplayMetrics().density;

        mTranslation = scale * 2;
        mTranslationHideTitle = scale * 10;
        mTopMargin = (int) (scale * 8);
        mTopMarginHideTitle = (int) (scale * 16);

        LayoutInflater.from(context).inflate(R.layout.item_material, this, true);

        mIcon = findViewById(R.id.icon);
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return MaterialItemView.class.getName();
    }

    public void initialization(String title, Drawable drawable, Drawable checkedDrawable, boolean tintIcon, int color, int checkedColor) {
        mTintIcon = tintIcon;

        mDefaultColor = color;
        mCheckedColor = checkedColor;

        if (mTintIcon) {
            mDefaultDrawable = Utils.tinting(drawable, mDefaultColor);
            mCheckedDrawable = Utils.tinting(checkedDrawable, mCheckedColor);
        } else {
            mDefaultDrawable = drawable;
            mCheckedDrawable = checkedDrawable;
        }


        mIcon.setImageDrawable(mDefaultDrawable);

        mAnimator = ValueAnimator.ofFloat(1f);
        mAnimator.setDuration(115L);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimatorValue = (float) animation.getAnimatedValue();
                if (mHideTitle) {
                    mIcon.setTranslationY(-mTranslationHideTitle * mAnimatorValue);
                } else {
                    mIcon.setTranslationY(-mTranslation * mAnimatorValue);
                }
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mIsMeasured = true;
    }

    @Override
    public void setChecked(boolean checked) {
        if (mChecked == checked) {
            return;
        }

        mChecked = checked;


        if (mIsMeasured) {
            // 切换动画
            if (mChecked) {
                mAnimator.start();
            } else {
                mAnimator.reverse();
            }
        } else if (mChecked) { // 布局还未测量时选中，直接转换到选中的最终状态
            if (mHideTitle) {
                mIcon.setTranslationY(-mTranslationHideTitle);
            } else {
                mIcon.setTranslationY(-mTranslation);
            }
        } else { // 布局还未测量并且未选中，保持未选中状态
            mIcon.setTranslationY(0);
        }

        // 切换颜色
        if (mChecked) {
            mIcon.setImageDrawable(mCheckedDrawable);
        } else {
            mIcon.setImageDrawable(mDefaultDrawable);
        }
    }


    @Override
    public void setTitle(String title) {

    }

    @Override
    public void setDefaultDrawable(Drawable drawable) {
        if (mTintIcon) {
            mDefaultDrawable = Utils.tinting(drawable, mDefaultColor);
        } else {
            mDefaultDrawable = drawable;
        }

        if (!mChecked) {
            mIcon.setImageDrawable(mDefaultDrawable);
        }
    }

    @Override
    public void setSelectedDrawable(Drawable drawable) {
        if (mTintIcon) {
            mCheckedDrawable = Utils.tinting(drawable, mCheckedColor);
        } else {
            mCheckedDrawable = drawable;
        }

        if (mChecked) {
            mIcon.setImageDrawable(mCheckedDrawable);
        }
    }

    @Override
    public String getTitle() {
       return "";
    }

    /**
     * 获取动画运行值[0,1]
     */
    public float getAnimValue() {
        return mAnimatorValue;
    }

    /**
     * 设置是否隐藏文字
     */
    public void setHideTitle(boolean hideTitle) {
        mHideTitle = hideTitle;

        LayoutParams iconParams = (LayoutParams) mIcon.getLayoutParams();

        if (mHideTitle) {
            iconParams.topMargin = mTopMarginHideTitle;
        } else {
            iconParams.topMargin = mTopMargin;
        }


        mIcon.setLayoutParams(iconParams);
    }

}