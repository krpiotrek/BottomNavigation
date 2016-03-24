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

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.animation.AccelerateInterpolator;

public class BackgroundDrawable extends Drawable {

    private static final int EMPTY_VALUE = -1;

    private Paint backgroundPaint;
    private Paint newBackgroundPaint;

    private Integer mAnimatedRadiusValue;
    private int mViewX;
    private int mViewY;
    private boolean mAnimationPending;

    public BackgroundDrawable(int backgroundColor) {
        backgroundPaint = getPaint(backgroundColor);
    }

    public void animate(int viewX, int viewY, final int newBackgroundColor) {
        if (mAnimationPending) return;

        mAnimationPending = true;

        newBackgroundPaint = getPaint(newBackgroundColor);

        mViewX = viewX;
        mViewY = viewY;
        final Rect bounds = getBounds();
        final int left = bounds.left;
        final int right = bounds.right;
        final int radius = Math.max(viewX - left, right - viewX);

        final ValueAnimator radiusSizeAnimator = ObjectAnimator.ofInt(0, radius);

        radiusSizeAnimator.setInterpolator(new AccelerateInterpolator());
        radiusSizeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimatedRadiusValue = (Integer) animation.getAnimatedValue();
                invalidateSelf();
            }
        });

        radiusSizeAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimatedRadiusValue = null;
                mViewX = EMPTY_VALUE;
                mViewY = EMPTY_VALUE;
                backgroundPaint = newBackgroundPaint;
                mAnimationPending = false;
            }
        });
        radiusSizeAnimator.setDuration(200);
        radiusSizeAnimator.start();
    }

    private Paint getPaint(int newBackgroundColor) {
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(newBackgroundColor);
        return paint;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRect(getBounds(), backgroundPaint);
        if (mAnimatedRadiusValue != null) {
            canvas.drawCircle(mViewX, mViewY, mAnimatedRadiusValue, newBackgroundPaint);
        }
    }

    @Override
    public void setAlpha(int alpha) {
        // noop
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        // noop
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }
}
