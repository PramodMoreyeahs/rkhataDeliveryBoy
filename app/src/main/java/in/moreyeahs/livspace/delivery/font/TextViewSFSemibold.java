package in.moreyeahs.livspace.delivery.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class TextViewSFSemibold extends androidx.appcompat.widget.AppCompatTextView {

	public TextViewSFSemibold(Context context) {
		super(context);
		setFont();
	}
	public TextViewSFSemibold(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFont();
	}
	public TextViewSFSemibold(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setFont();
	}

	private void setFont() {
		Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/segoe_ui_semibold.ttf");
		setTypeface(font, Typeface.NORMAL);
	}
}
