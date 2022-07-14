package in.moreyeahs.livspace.delivery.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

/**
 * Created by BT129-6 on 31/08/2016.
 */
public class EditTextSFMedium extends AppCompatEditText {

    public EditTextSFMedium(Context context) {
        super(context);
        setFont();
    }

    public EditTextSFMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public EditTextSFMedium(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/segoeuisl.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}