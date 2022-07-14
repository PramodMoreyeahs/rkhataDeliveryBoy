package in.moreyeahs.livspace.delivery.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class ButtonSFHeavy extends Button {
	public ButtonSFHeavy(Context context) {
		super(context);
		setFont();
	}
	public ButtonSFHeavy(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFont();
	}
	public ButtonSFHeavy(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setFont();
	}

	private void setFont() {
		Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/sf_ui_display_heavy.ttf");
		setTypeface(font, Typeface.NORMAL);
	}
}
