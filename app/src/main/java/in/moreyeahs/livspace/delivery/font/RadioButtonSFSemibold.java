package in.moreyeahs.livspace.delivery.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RadioButton;

/**
 * Created by BT129-6 on 31/08/2016.
 */
public class RadioButtonSFSemibold extends RadioButton {
    public RadioButtonSFSemibold(Context context) {
        super(context);
        setFont();
    }
    public RadioButtonSFSemibold(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }
    public RadioButtonSFSemibold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/sf_ui_display_semibold.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
