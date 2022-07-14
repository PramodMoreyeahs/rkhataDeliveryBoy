package in.moreyeahs.livspace.delivery.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewOpensansLight extends TextView {
	public TextViewOpensansLight(Context context) {
		super(context);
		setFont();
	}
	public TextViewOpensansLight(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFont();
	}
	public TextViewOpensansLight(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setFont();
	}

	private void setFont() {
		Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/segoeuil.ttf");
		setTypeface(font, Typeface.NORMAL);
	}
}
