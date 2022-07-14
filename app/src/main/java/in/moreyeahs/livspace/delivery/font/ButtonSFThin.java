package in.moreyeahs.livspace.delivery.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class ButtonSFThin extends Button {
	public ButtonSFThin(Context context) {
		super(context);
		setFont();
	}
	public ButtonSFThin(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFont();
	}
	public ButtonSFThin(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setFont();
	}

	private void setFont() {
		Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/sf_ui_display_thin.ttf");
		setTypeface(font, Typeface.NORMAL);
	}
}
