package com.delectable.mobile.ui.common.widget;

import com.delectable.mobile.R;
import com.delectable.mobile.util.FontUtil;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class FontTextView extends TextView {


    public FontTextView(Context context) {
        super(context);
    }

    public FontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (!isInEditMode()) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FontTextView);
            int fontIntValue = a
                    .getInt(R.styleable.FontTextView_fontName,
                            FontUtil.Font.WHITNEY_BLACK.ordinal());
            a.recycle();

            FontUtil.Font font = FontUtil.Font.values()[fontIntValue];
            setTypeface(font);
        }
    }

    /**
     * Convenience method that calls {@link #setTypeface(android.graphics.Typeface)}
     */
    public void setTypeface(FontUtil.Font font) {
        Typeface typeface = Typeface
                .createFromAsset(getContext().getAssets(), "fonts/" + font.getFileName());
        setTypeface(typeface);
    }
}
