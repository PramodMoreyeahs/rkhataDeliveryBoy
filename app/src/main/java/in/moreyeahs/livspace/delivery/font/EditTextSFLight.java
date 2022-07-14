package in.moreyeahs.livspace.delivery.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

/**
 * Created by BT129-6 on 31/08/2016.
 */
public class EditTextSFLight extends AppCompatEditText {
    public EditTextSFLight(Context context) {
        super(context);
        setFont();
    }

    public EditTextSFLight(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    public EditTextSFLight(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/segoeuil.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}