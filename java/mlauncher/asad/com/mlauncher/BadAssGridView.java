package mlauncher.asad.com.mlauncher;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by bobby on 3/31/2016.
 */
public class BadAssGridView extends GridView {
    public BadAssGridView(Context context) {
        super(context);
    }

    public BadAssGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BadAssGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BadAssGridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setHorizontalSpacing(int horizontalSpacing) {
        super.setHorizontalSpacing(horizontalSpacing);
        super.setVerticalSpacing(horizontalSpacing);
    }

    @Override
    public void setVerticalSpacing(int verticalSpacing) {

    }

    @Override
    public void setNumColumns(int numColumns) {
        super.setNumColumns(5);
    }


}
