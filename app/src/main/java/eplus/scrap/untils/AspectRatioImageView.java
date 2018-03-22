package eplus.scrap.untils;

/**
 * Created by nals-anhdv on 11/8/17.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

@SuppressLint("AppCompatCustomView")
public class AspectRatioImageView extends ImageView {

    public AspectRatioImageView(Context context) {
        super(context);
    }

    public AspectRatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AspectRatioImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = widthMeasureSpec;
        int height = heightMeasureSpec;
        if(getDrawable() != null){
             width = MeasureSpec.getSize(widthMeasureSpec);
             height = width * getDrawable().getIntrinsicHeight() / getDrawable().getIntrinsicWidth();
        }

        setMeasuredDimension(width, height);
    }
}
