package in.moreyeahs.livspace.delivery.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewSFThin extends TextView {
	public TextViewSFThin(Context context) {
		super(context);
		setFont();
	}
	public TextViewSFThin(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFont();
	}
	public TextViewSFThin(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setFont();
	}

	private void setFont() {
		Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/sf_ui_display_thin.ttf");
		setTypeface(font, Typeface.NORMAL);
	}
}
