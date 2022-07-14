package in.moreyeahs.livspace.delivery.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewOpensansBold extends TextView {
	public TextViewOpensansBold(Context context) {
		super(context);
		setFont();
	}
	public TextViewOpensansBold(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFont();
	}
	public TextViewOpensansBold(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setFont();
	}

	private void setFont() {
		Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/segoeuib.ttf");
		setTypeface(font, Typeface.NORMAL);
	}
}
