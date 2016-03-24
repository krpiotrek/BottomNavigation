/**
 * Copyright 2016 Piotr Krystyniak <krpiotrek@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.krpiotrek.library;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class BottomNavigation extends LinearLayout {

    private class ScrollAnimation {
        private ViewPropertyAnimator mViewPropertyAnimator;
        private boolean previousDown;

        public void handleScroll(int dy) {
            final boolean down = dy > 0;
            if (mViewPropertyAnimator != null) {
                if (down != previousDown) {
                    mViewPropertyAnimator.cancel();
                } else {
                    return;
                }
            }

            if (down) {
                mViewPropertyAnimator = animate().translationY(getHeight());
            } else {
                mViewPropertyAnimator = animate().translationY(0);

            }

            mViewPropertyAnimator.start();
            mViewPropertyAnimator.withEndAction(new Runnable() {
                @Override
                public void run() {
                    mViewPropertyAnimator = null;
                }
            });
            previousDown = down;
        }
    }


    public interface OnItemSelectedListener {
        void itemSelected(int id);
    }

    public class ConfigurationBuilder {

        private boolean mBuilderHideOnScroll = false;
        private boolean mBuilderWithColorChanges = false;
        private int mBuilderStartingPosition = 0;
        private NavigationItem[] mBuilderItems;

        public ConfigurationBuilder hideOnScroll(boolean hideOnScroll) {
            mBuilderHideOnScroll = hideOnScroll;
            return this;
        }

        public ConfigurationBuilder withColorChanges(boolean withColorChanges) {
            mBuilderWithColorChanges = withColorChanges;
            return this;
        }

        public ConfigurationBuilder items(NavigationItem... items) {
            mBuilderItems = items;
            return this;
        }

        public ConfigurationBuilder startingPosition(int startingPosition) {
            mBuilderStartingPosition = startingPosition;
            return this;
        }

        public void build() {
            if (mBuilderItems == null) {
                throw new IllegalArgumentException("Navigation items cannot be null.");
            }

            mNavigationItems = Arrays.asList(mBuilderItems);
            mHideOnScroll = mBuilderHideOnScroll;
            mWithColorChanges = mBuilderWithColorChanges;
            mStartingPosition = mBuilderStartingPosition;

            init();
        }
    }

    public static class NavigationItem {

        private final int id;
        private final Drawable icon;
        private final String text;
        private final int color;

        public NavigationItem(int id, @DrawableRes @NonNull Drawable icon, @NonNull String text, int color) {
            this.id = id;
            this.icon = icon;
            this.text = text;
            this.color = color;
        }

        public NavigationItem(int id, @DrawableRes @NonNull Drawable icon, @NonNull String text) {
            this.id = id;
            this.icon = icon;
            this.text = text;
            this.color = -1;
        }
    }

    private static final int ANIMATION_DURATION = 175;

    private List<NavigationItem> mNavigationItems;
    private OnItemSelectedListener mOnItemSelectedListener;
    private View selectedView;
    private BackgroundDrawable mBackgroundDrawable;
    private ScrollAnimation mScrollAnimation = new ScrollAnimation();
    private boolean mHideOnScroll;
    private int mMaskColor;
    private boolean mWithColorChanges;
    private int mStartingPosition;
    private boolean mSelectedAnimationPending;
    private boolean mUnselectedAnimationPending;
    private int mTranslationX;
    private int mIconTranslationY;
    private int mTextTranslationY;

    public BottomNavigation(Context context) {
        super(context);
    }

    public BottomNavigation(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomNavigation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @NonNull
    public ConfigurationBuilder builder() {
        return new ConfigurationBuilder();
    }

    public void setOnItemSelectedListener(@Nullable OnItemSelectedListener onItemSelectedListener) {
        mOnItemSelectedListener = onItemSelectedListener;
    }

    private void init() {
        final Resources resources = getResources();
        mTranslationX = resources.getDimensionPixelSize(R.dimen.translation_x);
        mIconTranslationY = resources.getDimensionPixelSize(R.dimen.icon_translation_y);
        mTextTranslationY = resources.getDimensionPixelSize(R.dimen.text_translation_y);

        mMaskColor = resources.getColor(R.color.darkerWhite);

        initViews();

        select(mStartingPosition);
    }

    private void initViews() {
        for (final NavigationItem item : mNavigationItems) {
            final View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_layout, this, false);

            final TextView text = (TextView) itemView.findViewById(android.R.id.text1);
            final ImageView image = (ImageView) itemView.findViewById(android.R.id.icon);

            text.setText(item.text);
            image.setImageDrawable(item.icon);
            image.setColorFilter(mMaskColor, PorterDuff.Mode.SRC_IN);

            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view == selectedView) return;

                    if (mOnItemSelectedListener != null) {
                        mOnItemSelectedListener.itemSelected(item.id);
                    }
                    if (selectedView != null) {
                        animateUnselected(selectedView);
                    }
                    animateSelected(view);
                    selectedView = view;

                    if (mWithColorChanges) {
                        mBackgroundDrawable.animate(
                                (int) view.getX() + view.getWidth() / 2,
                                (int) view.getY() + view.getHeight() / 2,
                                item.color);
                    }
                }
            });

            addView(itemView, new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
        }
    }

    private void select(int position) {
        final NavigationItem navigationItem = mNavigationItems.get(position);
        final View child = getChildAt(position);
        final TextView text = (TextView) child.findViewById(android.R.id.text1);
        final ImageView image = (ImageView) child.findViewById(android.R.id.icon);
        final int xValue = position == 0 ? mTranslationX : -mTranslationX;

        text.setVisibility(View.VISIBLE);
        text.setTranslationX(xValue);
        text.setTranslationY(-mTextTranslationY);
        text.setAlpha(1f);

        image.setTranslationX(xValue);
        image.setTranslationY(-mIconTranslationY);

        if (mWithColorChanges) {
            mBackgroundDrawable = new BackgroundDrawable(navigationItem.color);
            setBackground(mBackgroundDrawable);
        }

        selectedView = child;

        if (mOnItemSelectedListener != null) {
            mOnItemSelectedListener.itemSelected(navigationItem.id);
        }
    }

    private void animateUnselected(View view) {
        if (mUnselectedAnimationPending) return;

        mUnselectedAnimationPending = true;

        final TextView text = (TextView) view.findViewById(android.R.id.text1);
        final ImageView icon = (ImageView) view.findViewById(android.R.id.icon);

        text.animate()
                .alpha(0f)
                .translationY(0)
                .translationX(0)
                .setDuration(ANIMATION_DURATION)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        text.setVisibility(View.INVISIBLE);
                    }
                })
                .setInterpolator(new AccelerateInterpolator())
                .start();

        icon.animate()
                .translationY(0)
                .translationX(0)
                .setDuration(ANIMATION_DURATION)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        mUnselectedAnimationPending = false;
                        icon.setColorFilter(mMaskColor, PorterDuff.Mode.SRC_IN);
                    }
                })
                .setInterpolator(new AccelerateInterpolator())
                .start();
    }

    private void animateSelected(View view) {
        if (mSelectedAnimationPending) return;

        mSelectedAnimationPending = true;

        final boolean isFirst = getChildAt(0) == view;
        final TextView text = (TextView) view.findViewById(android.R.id.text1);
        final ImageView icon = (ImageView) view.findViewById(android.R.id.icon);

        text.setVisibility(View.VISIBLE);
        icon.setColorFilter(null);

        final int xValue = isFirst ? mTranslationX : -mTranslationX;

        text.animate()
                .alpha(1f)
                .translationY(-mTextTranslationY)
                .translationX(xValue)
                .setDuration(ANIMATION_DURATION)
                .setInterpolator(new AccelerateInterpolator())
                .start();

        icon.animate()
                .translationY(-mIconTranslationY)
                .translationX(xValue)
                .setDuration(ANIMATION_DURATION)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        mSelectedAnimationPending = false;
                    }
                })
                .setInterpolator(new AccelerateInterpolator());
    }

    public void passScrollY(int dy) {
        if (mHideOnScroll) {
            mScrollAnimation.handleScroll(dy);
        }
    }
}
