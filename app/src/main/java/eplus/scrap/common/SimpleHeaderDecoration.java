package eplus.scrap.common;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
/**
 * Created by nals-anhdv on 1/9/18.
 */

public class SimpleHeaderDecoration extends RecyclerView.ItemDecoration {
    private final int headerHeight;
    private final int footerHeight;
    private final int spacing;
    private int displayMode;

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    public static final int GRID = 2;


    public SimpleHeaderDecoration(int headerHeight, int footerHeight,int spacing,int displayMode) {
        this.headerHeight = headerHeight;
        this.footerHeight = footerHeight;
        this.spacing = spacing;
        this.displayMode = displayMode;
    }

    @Override public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                         RecyclerView.State state) {
        int childAdapterPosition = parent.getChildAdapterPosition(view);
        if (childAdapterPosition == 0) {
            outRect.top = headerHeight;
        } else if (childAdapterPosition == parent.getAdapter().getItemCount() - 1) {
            outRect.bottom = footerHeight;
        } else {
            outRect.set(0, 0, 0, 0);
        }
        int position = parent.getChildViewHolder(view).getAdapterPosition();
        int itemCount = state.getItemCount();
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        setSpacingForDirection(outRect, layoutManager, position, itemCount);
    }
    private void setSpacingForDirection(Rect outRect,
                                        RecyclerView.LayoutManager layoutManager,
                                        int position,
                                        int itemCount) {

        // Resolve display mode automatically
        if (displayMode == -1) {
            displayMode = resolveDisplayMode(layoutManager);
        }

        switch (displayMode) {
            case HORIZONTAL:
                outRect.left = spacing;
                outRect.right = position == itemCount - 1 ? spacing : 0;
                outRect.top = spacing;
                //outRect.bottom = spacing;
                break;
            case VERTICAL:
                outRect.left = spacing;
                outRect.right = spacing;
                outRect.top = spacing;
                outRect.bottom = position == itemCount - 1 ? spacing : 0;
                break;
            case GRID:
                if (layoutManager instanceof GridLayoutManager) {
                    GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                    int cols = gridLayoutManager.getSpanCount();
                    int rows = itemCount / cols;

                    outRect.left = spacing;
                    outRect.right = position % cols == cols - 1 ? spacing : 0;
                    outRect.top = spacing;
                    //outRect.bottom = position / cols == rows - 1 ? spacing : 0;
                }
                break;
        }
    }

    private int resolveDisplayMode(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof GridLayoutManager) return GRID;
        if (layoutManager.canScrollHorizontally()) return HORIZONTAL;
        return VERTICAL;
    }
}
