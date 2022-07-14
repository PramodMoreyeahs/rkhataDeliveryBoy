package in.moreyeahs.livspace.delivery.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RadioButton;

/**
 * Created by BT129-6 on 31/08/2016.
 */
public class RadioButtonOpensansLight extends RadioButton {
    public RadioButtonOpensansLight(Context context) {
        super(context);
        setFont();
    }
    public RadioButtonOpensansLight(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }
    public RadioButtonOpensansLight(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/opensans_light_0.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
