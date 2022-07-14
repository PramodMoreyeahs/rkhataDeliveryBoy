package in.moreyeahs.livspace.delivery.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RadioButton;

/**
 * Created by BT129-6 on 31/08/2016.
 */
public class RadioButtonSFThin extends RadioButton {

    public RadioButtonSFThin(Context context) {
        super(context);
        setFont();
    }
    public RadioButtonSFThin(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }
    public RadioButtonSFThin(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/sf_ui_display_thin.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
