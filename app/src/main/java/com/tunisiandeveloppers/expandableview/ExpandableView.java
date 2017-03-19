package com.tunisiandeveloppers.expandableview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MED on 16/03/2017.
 */

public class ExpandableView extends LinearLayout implements ExpandableLayout, View.OnClickListener {

    public static final int DURATION = 200;
    @BindView(R2.id.lib_header)
    protected LinearLayout header;

    @BindView(R2.id.lib_content)
    protected LinearLayout content;

    @BindView(R2.id.lib_content_text)
    protected TextView contentText;

    @BindView(R2.id.lib_header_text)
    protected TextView headerText;

    private View rootView;

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    private boolean expanded;

    public void setOnExpandableViewChangeStatusListener(@Nullable OnExpandableViewChangeStatusListener onExpandableViewChangeStatusListener) {
        this.onExpandableViewChangeStatusListener = onExpandableViewChangeStatusListener;
    }

    @Nullable
    private OnExpandableViewChangeStatusListener onExpandableViewChangeStatusListener;

    public ExpandableView(Context context) {
        super(context);
        initView(context, null);
    }

    public ExpandableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, @Nullable AttributeSet attrs) {
        rootView = inflate(context, R.layout.lib_expandable_view, this);
        ButterKnife.bind(this, rootView);
        header.setOnClickListener(this);
    }

    @Override
    public void expand() {
        if (onExpandableViewChangeStatusListener != null) {
            onExpandableViewChangeStatusListener.onExpand();
        }

        LayoutParams parms = (LayoutParams) content.getLayoutParams();
        final int width = this.getWidth() - parms.leftMargin - parms.rightMargin;

        content.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

        final int targetHeight = content.getMeasuredHeight() + content.getPaddingTop() + content.getPaddingBottom();

        ValueAnimator a = ValueAnimator.ofInt(0, targetHeight);

        a.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                LayoutParams parms = (LayoutParams) content.getLayoutParams();
                parms.height = (int) valueAnimator.getAnimatedValue();
                content.setLayoutParams(parms);
            }
        });

        a.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                LayoutParams parms = (LayoutParams) content.getLayoutParams();
                parms.height = LayoutParams.WRAP_CONTENT;
                content.setLayoutParams(parms);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        a.setDuration(DURATION);
        a.setInterpolator(new LinearInterpolator());
        a.start();

        expanded = true;
    }

    @Override
    public void collapse() {
        if (onExpandableViewChangeStatusListener != null) {
            onExpandableViewChangeStatusListener.onCollapse();
        }

        ValueAnimator a = ValueAnimator.ofInt(content.getHeight(), 0);

        a.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                LayoutParams parms = (LayoutParams) content.getLayoutParams();
                parms.height = (int) valueAnimator.getAnimatedValue();
                content.setLayoutParams(parms);
            }
        });

        a.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        a.setDuration(DURATION);
        a.setInterpolator(new LinearInterpolator());
        a.start();

        expanded = false;
    }

    @Override
    public void onClick(View view) {
        if (expanded) {
            collapse();
        } else {
            expand();
        }
    }

    public void setContentText(String contentText) {
        this.contentText.setText(contentText);
    }

    public void setHeaderText(String headerText) {
        this.headerText.setText(headerText);
    }
}
