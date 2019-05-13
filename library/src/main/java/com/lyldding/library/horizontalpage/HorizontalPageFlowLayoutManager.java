package com.lyldding.library.horizontalpage;

import android.graphics.Rect;
import android.support.annotation.IntRange;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * 每页显示瀑布流布局
 *
 * @author https://github.com/lyldding/HorizontalPageFlow
 */
public class HorizontalPageFlowLayoutManager extends RecyclerView.LayoutManager {
    private static final String TAG = "HorizontalPageFlowLayou";
    /**
     * 缓存显示区域
     */
    private SparseArray<Rect> allItemFrames = new SparseArray<>();
    /**
     * 可滑动的最大宽度
     */
    private int totalWidth = 0;
    /**
     * 垂直偏移量
     */
    private int offsetY = 0;
    /**
     * 水平偏移量
     */
    private int offsetX = 0;
    /**
     * 每页最大行数
     */
    private int maxRows = 0;
    /**
     * 每行最大列数
     */
    private int maxColumns = 0;
    /**
     * 总共页数
     */
    private int pageSize = 0;
    /**
     * 当前页所用宽度
     */
    private int itemWidthUsed;
    /**
     * 当前页所用高度
     */
    private int itemHeightUsed;

    public HorizontalPageFlowLayoutManager() {
        this.maxRows = 0;
        this.maxColumns = 0;
    }

    /**
     * @param maxRows    每页最大行数（=0，填充至最大高度）
     * @param maxColumns 每行最大列数（=0，填充至最大宽度）
     */
    public HorizontalPageFlowLayoutManager(@IntRange(from = 0) int maxRows, @IntRange(from = 0) int maxColumns) {
        this.maxRows = maxRows;
        this.maxColumns = maxColumns;
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public boolean canScrollHorizontally() {
        return true;
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        detachAndScrapAttachedViews(recycler);
        int newX = offsetX + dx;
        int result = dx;
        if (newX > totalWidth) {
            result = totalWidth - offsetX;
        } else if (newX < 0) {
            result = 0 - offsetX;
        }
        offsetX += result;
        offsetChildrenHorizontal(-result);
        recycleAndFillItems(recycler, state);
        return result;
    }

    private int getUsableWidth() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    private int getUsableHeight() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() == 0) {
            removeAndRecycleAllViews(recycler);
            return;
        }
        if (state.isPreLayout()) {
            return;
        }

        itemWidthUsed = getPaddingLeft();
        itemHeightUsed = getPaddingTop();
        pageSize = 0;

        //分离view
        detachAndScrapAttachedViews(recycler);
        int count = getItemCount();
        //每行个数
        int itemCount = 0;
        //每页行数
        int lineCount = 1;
        for (int index = 0; index < count; index++) {
            View view = recycler.getViewForPosition(index);
            addView(view);
            //测量item
            measureChildWithMargins(view, 0, 0);
            int childWidth = getDecoratedMeasuredWidth(view);
            int childHeight = getDecoratedMeasuredHeight(view);

            boolean notChangeLine = (maxColumns == 0 || itemCount < maxColumns)
                    && (childWidth + itemWidthUsed <= getUsableWidth());

            if (notChangeLine) {
                cacheItemRect(index, childWidth, childHeight);
                itemCount++;
            } else {//换行
                itemCount = 1;
                itemHeightUsed += childHeight;
                boolean notChangePage = (maxRows == 0 || lineCount < maxRows)
                        && (itemHeightUsed + childHeight <= getUsableHeight());
                if (notChangePage) {
                    itemWidthUsed = getPaddingLeft();
                    cacheItemRect(index, childWidth, childHeight);
                    lineCount++;
                } else {//换页
                    //每一页循环以后就回收一页的View用于下一页的使用
                    removeAndRecycleAllViews(recycler);
                    lineCount = 1;
                    pageSize++;
                    itemWidthUsed = getPaddingLeft();
                    itemHeightUsed = getPaddingTop();
                    cacheItemRect(index, childWidth, childHeight);
                }
            }
        }
        //回收最后一页
        removeAndRecycleAllViews(recycler);
        totalWidth = (pageSize) * getWidth();

        recycleAndFillItems(recycler, state);
    }

    /**
     * 记录显示范围
     */
    private void cacheItemRect(int index, int childWidth, int childHeight) {
        Rect rect = allItemFrames.get(index);
        if (rect == null) {
            rect = new Rect();
        }
        int x = pageSize * getWidth() + itemWidthUsed;
        int y = itemHeightUsed;
        rect.set(x, y, childWidth + x, childHeight + y);
        allItemFrames.put(index, rect);
        itemWidthUsed += childWidth;
    }


    @Override
    public void onDetachedFromWindow(RecyclerView view, RecyclerView.Recycler recycler) {
        super.onDetachedFromWindow(view, recycler);
        offsetX = 0;
        offsetY = 0;
    }


    private void recycleAndFillItems(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (state.isPreLayout()) {
            return;
        }

        Rect displayRect = new Rect(getPaddingLeft() + offsetX, getPaddingTop(), getWidth() - getPaddingLeft() - getPaddingRight() + offsetX, getHeight() - getPaddingTop() - getPaddingBottom());
        Rect childRect = new Rect();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            childRect.left = getDecoratedLeft(child);
            childRect.top = getDecoratedTop(child);
            childRect.right = getDecoratedRight(child);
            childRect.bottom = getDecoratedBottom(child);
            if (!Rect.intersects(displayRect, childRect)) {
                removeAndRecycleView(child, recycler);
            }
        }


        for (int i = 0; i < getItemCount(); i++) {
            if (Rect.intersects(displayRect, allItemFrames.get(i))) {
                View view = recycler.getViewForPosition(i);
                addView(view);
                measureChildWithMargins(view, 0, 0);
                Rect rect = allItemFrames.get(i);
                layoutDecorated(view, rect.left - offsetX, rect.top, rect.right - offsetX, rect.bottom);
            }
        }
    }


    @Override
    public int computeHorizontalScrollRange(RecyclerView.State state) {
        return pageSize * getWidth();
    }


    @Override
    public int computeHorizontalScrollOffset(RecyclerView.State state) {
        return offsetX;
    }


    @Override
    public int computeHorizontalScrollExtent(RecyclerView.State state) {
        return getWidth();
    }

    public int getPageSize() {
        return pageSize + 1;
    }
}
