package in.moreyeahs.livspace.delivery.font;

import android.content.Context;
import android.graphics.Typeface;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;


public class TextViewSFRegular extends AppCompatTextView {
	public TextViewSFRegular(Context context) {
		super(context);
		setFont();
	}
	public TextViewSFRegular(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFont();
	}
	public TextViewSFRegular(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setFont();
	}

	private void setFont() {
		Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/segoeui.ttf");
		setTypeface(font, Typeface.NORMAL);
	}
}
