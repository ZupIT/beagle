/*
 * Copyright 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * This file is part of https://github.com/facebook/yoga which is originally released under MIT License.
 * See file https://github.com/facebook/yoga/blob/master/android/src/main/java/com/facebook/yoga/android/YogaLayout.java
 * or go to https://github.com/facebook/yoga/blob/master/LICENSE for full license details.
 */

package br.com.zup.beagle.android.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.facebook.yoga.YogaConstants;
import com.facebook.yoga.YogaMeasureFunction;
import com.facebook.yoga.YogaMeasureMode;
import com.facebook.yoga.YogaMeasureOutput;
import com.facebook.yoga.YogaNode;

import java.util.HashMap;
import java.util.Map;

import br.com.zup.beagle.android.view.custom.BeagleFlexView;

@SuppressLint("ViewConstructor")
public class YogaLayout extends ViewGroup {

    private final String ADD_VIEW_EXCEPTION_MESSAGE = "You should call addView(view, yogaNode) or addView(view)";

    private final Map<View, YogaNode> mYogaNodes;
    private final YogaNode mYogaNode;

    public YogaLayout(Context context) {
        this(context, null);
    }

    public YogaLayout(Context context, @Nullable YogaNode node) {
        super(context);

        if (node != null) {
            mYogaNode = node;
        } else {
            mYogaNode = YogaNode.create();
        }

        mYogaNodes = new HashMap<>();

        mYogaNode.setData(this);
        mYogaNode.setMeasureFunction(new ViewMeasureFunction());
    }

    public YogaNode getYogaNode() {
        return mYogaNode;
    }

    public void addView(View child, @Nullable YogaNode node, int index) {
        // Internal nodes (which this is now) cannot have measure functions
        mYogaNode.setMeasureFunction(null);

        super.addView(child, generateDefaultLayoutParams());

        // It is possible that addView is being called as part of a transferal of children, in which
        // case we already know about the YogaNode and only need the Android View tree to be aware
        // that we now own this child.  If so, we don't need to do anything further
        if (mYogaNodes.containsKey(child)) {
            return;
        }

        YogaNode childNode;

        if (child instanceof YogaLayout) {
            childNode = ((YogaLayout) child).getYogaNode();
        } else {
            if (mYogaNodes.containsKey(child)) {
                childNode = mYogaNodes.get(child);
            } else if (node != null) {
                childNode = node;
            } else {
                childNode = YogaNode.create();
            }

            childNode.setData(child);
            childNode.setMeasureFunction(new ViewMeasureFunction());
        }

        mYogaNodes.put(child, childNode);
        if (index == -1)
            index = mYogaNode.getChildCount();

        mYogaNode.addChildAt(childNode, index);

    }

    public void addView(View child, @Nullable YogaNode node){
        addView(child, node, -1);
    }

    @Override
    public void addView(View child) {
        YogaNode node = null;
        addView(child, node, -1);
    }

    @Override
    public void addView(View child, int index) {
        YogaNode node = null;
        addView(child, node,index);
    }

    @Override
    public void addView(View child, int width, int height) {
        throw new RuntimeException(ADD_VIEW_EXCEPTION_MESSAGE);
    }

    @Override
    public void addView(View child, LayoutParams params) {
        throw new RuntimeException(ADD_VIEW_EXCEPTION_MESSAGE);
    }

    @Override
    public void removeView(View view) {
        removeViewFromYogaTree(view, false);
        super.removeView(view);
    }

    @Override
    public void removeViewAt(int index) {
        removeViewFromYogaTree(getChildAt(index), false);
        super.removeViewAt(index);
    }

    @Override
    public void removeViewInLayout(View view) {
        removeViewFromYogaTree(view, true);
        super.removeViewInLayout(view);
    }

    @Override
    public void removeViews(int start, int count) {
        for (int i = start; i < start + count; i++) {
            removeViewFromYogaTree(getChildAt(i), false);
        }
        super.removeViews(start, count);
    }

    @Override
    public void removeViewsInLayout(int start, int count) {
        for (int i = start; i < start + count; i++) {
            removeViewFromYogaTree(getChildAt(i), true);
        }
        super.removeViewsInLayout(start, count);
    }

    @Override
    public void removeAllViews() {
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            removeViewFromYogaTree(getChildAt(i), false);
        }
        super.removeAllViews();
    }

    @Override
    public void removeAllViewsInLayout() {
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            removeViewFromYogaTree(getChildAt(i), true);
        }
        super.removeAllViewsInLayout();
    }

    /**
     * Marks a particular view as "dirty" and to be relaid out.  If the view is not a child of this
     * {@link YogaLayout}, the entire tree is traversed to find it.
     *
     * @param view the view to mark as dirty
     */
    public void invalidate(View view) {
        if (mYogaNodes.containsKey(view) && !(view instanceof BeagleFlexView)) {
            mYogaNodes.get(view).dirty();
            return;
        }

        final int childCount = mYogaNode.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final YogaNode yogaNode = mYogaNode.getChildAt(i);
            if (yogaNode.getData() instanceof YogaLayout) {
                ((YogaLayout) yogaNode.getData()).invalidate(view);
            }
        }
        invalidate();
    }

    private void removeViewFromYogaTree(View view, boolean inLayout) {
        final YogaNode node = mYogaNodes.get(view);
        if (node == null) {
            return;
        }

        final YogaNode owner = node.getOwner();

        for (int i = 0; i < owner.getChildCount(); i++) {
            if (owner.getChildAt(i).equals(node)) {
                owner.removeChildAt(i);
                break;
            }
        }

        node.setData(null);
        mYogaNodes.remove(view);

        if (inLayout) {
            mYogaNode.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED);
        }
    }

    private void applyLayoutRecursive(YogaNode node, float xOffset, float yOffset) {
        View view = (View) node.getData();

        if (view != null && view != this) {
            if (view.getVisibility() == GONE) {
                return;
            }
            int left = Math.round(xOffset + node.getLayoutX());
            int top = Math.round(yOffset + node.getLayoutY());
            view.measure(
                MeasureSpec.makeMeasureSpec(
                    Math.round(node.getLayoutWidth()),
                    MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(
                    Math.round(node.getLayoutHeight()),
                    MeasureSpec.EXACTLY));
            view.layout(left, top, left + view.getMeasuredWidth(), top + view.getMeasuredHeight());
        }

        final int childrenCount = node.getChildCount();
        for (int i = 0; i < childrenCount; i++) {
            if (this.equals(view)) {
                applyLayoutRecursive(node.getChildAt(i), xOffset, yOffset);
            } else if (view instanceof YogaLayout) {
                continue;
            } else {
                applyLayoutRecursive(
                    node.getChildAt(i),
                    xOffset + node.getLayoutX(),
                    yOffset + node.getLayoutY());
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // Either we are a root of a tree, or this function is called by our owner's onLayout, in which
        // case our r-l and b-t are the size of our node.
        if (!(getParent() instanceof YogaLayout)) {
            createLayout(
                MeasureSpec.makeMeasureSpec(r - l, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(b - t, MeasureSpec.EXACTLY));
        }

        applyLayoutRecursive(mYogaNode, 0, 0);
    }

    /**
     * This function is mostly unneeded, because Yoga is doing the measuring.  Hence we only need to
     * return accurate results if we are the root.
     *
     * @param widthMeasureSpec  the suggested specification for the width
     * @param heightMeasureSpec the suggested specification for the height
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!(getParent() instanceof YogaLayout)) {
            createLayout(widthMeasureSpec, heightMeasureSpec);
        }

        setMeasuredDimension(
            Math.round(mYogaNode.getLayoutWidth()),
            Math.round(mYogaNode.getLayoutHeight()));
    }

    private void createLayout(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (heightMode == MeasureSpec.EXACTLY) {
            mYogaNode.setHeight(heightSize);
        }
        if (widthMode == MeasureSpec.EXACTLY) {
            mYogaNode.setWidth(widthSize);
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            mYogaNode.setMaxHeight(heightSize);
        }
        if (widthMode == MeasureSpec.AT_MOST) {
            mYogaNode.setMaxWidth(widthSize);
        }
        mYogaNode.calculateLayout(YogaConstants.UNDEFINED, YogaConstants.UNDEFINED);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT);
    }

    /**
     * Wrapper around measure function for yoga leaves.
     */
    private static class ViewMeasureFunction implements YogaMeasureFunction {

        /**
         * A function to measure leaves of the Yoga tree.  Yoga needs some way to know how large
         * elements want to be.  This function passes that question directly through to the relevant
         * {@code View}'s measure function.
         *
         * @param node       The yoga node to measure
         * @param width      The suggested width from the owner
         * @param widthMode  The type of suggestion for the width
         * @param height     The suggested height from the owner
         * @param heightMode The type of suggestion for the height
         * @return A measurement output ({@code YogaMeasureOutput}) for the node
         */
        public long measure(
            YogaNode node,
            float width,
            YogaMeasureMode widthMode,
            float height,
            YogaMeasureMode heightMode) {
            final View view = (View) node.getData();
            if (view == null || view instanceof YogaLayout) {
                return YogaMeasureOutput.make(0, 0);
            }

            final int widthMeasureSpec = MeasureSpec.makeMeasureSpec(
                (int) width,
                viewMeasureSpecFromYogaMeasureMode(widthMode));
            final int heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                (int) height,
                viewMeasureSpecFromYogaMeasureMode(heightMode));

            view.measure(widthMeasureSpec, heightMeasureSpec);

            return YogaMeasureOutput.make(view.getMeasuredWidth(), view.getMeasuredHeight());
        }

        private int viewMeasureSpecFromYogaMeasureMode(YogaMeasureMode mode) {
            if (mode == YogaMeasureMode.AT_MOST) {
                return MeasureSpec.AT_MOST;
            } else if (mode == YogaMeasureMode.EXACTLY) {
                return MeasureSpec.EXACTLY;
            } else {
                return MeasureSpec.UNSPECIFIED;
            }
        }
    }
}
