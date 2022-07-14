package in.moreyeahs.livspace.delivery.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class ButtonSFMedium extends Button {

	public ButtonSFMedium(Context context) {
		super(context);
		setFont();
	}
	public ButtonSFMedium(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFont();
	}
	public ButtonSFMedium(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setFont();
	}

	private void setFont() {
		Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/segoeuisl.ttf");
		setTypeface(font, Typeface.NORMAL);
	}
}
