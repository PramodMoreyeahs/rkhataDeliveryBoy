package in.moreyeahs.livspace.delivery.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RadioButton;

/**
 * Created by BT129-6 on 31/08/2016.
 */
public class RadioButtonSFMedium extends RadioButton {
    public RadioButtonSFMedium(Context context) {
        super(context);
        setFont();
    }
    public RadioButtonSFMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }
    public RadioButtonSFMedium(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/sf_ui_display_medium.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
