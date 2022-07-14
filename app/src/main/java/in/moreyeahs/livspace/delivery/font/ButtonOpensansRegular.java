package in.moreyeahs.livspace.delivery.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by BT129-6 on 31/08/2016.
 */
public class ButtonOpensansRegular extends Button {
    public ButtonOpensansRegular(Context context) {
        super(context);
        setFont();
    }
    public ButtonOpensansRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }
    public ButtonOpensansRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/segoeui.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
