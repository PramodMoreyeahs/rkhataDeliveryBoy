package in.moreyeahs.livspace.delivery.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class ButtonSFRegular extends Button {
	public ButtonSFRegular(Context context) {
		super(context);
		setFont();
	}
	public ButtonSFRegular(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFont();
	}
	public ButtonSFRegular(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setFont();
	}

	private void setFont() {
		Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/segoeui.ttf");
		setTypeface(font, Typeface.NORMAL);
	}
}
