package in.moreyeahs.livspace.delivery.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewSFBold extends TextView {

	public TextViewSFBold(Context context) {
		super(context);
		setFont();
	}
	public TextViewSFBold(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFont();
	}
	public TextViewSFBold(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setFont();
	}

	private void setFont() {
		Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/segoe_ui_bold.ttf");
		setTypeface(font, Typeface.NORMAL);
	}
}
