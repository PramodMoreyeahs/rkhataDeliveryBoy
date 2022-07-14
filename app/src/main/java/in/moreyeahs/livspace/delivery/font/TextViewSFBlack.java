package in.moreyeahs.livspace.delivery.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewSFBlack extends TextView {

	public TextViewSFBlack(Context context) {
		super(context);
		setFont();
	}
	public TextViewSFBlack(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFont();
	}
	public TextViewSFBlack(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setFont();
	}

	private void setFont() {
		Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/seguibl.ttf");
		setTypeface(font, Typeface.NORMAL);
	}
}
