package fit.lifecare.lifecare;

import android.content.Context;
import android.graphics.Typeface;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;

public class MontserratTextView extends AppCompatTextView {
    private static Typeface mTypeface;

    public MontserratTextView(Context context) {
        super(context);
        if (mTypeface == null) {
            mTypeface = ResourcesCompat.getFont(getContext(), R.font.montserrat);
        }
        setTypeface(mTypeface);
        setText("-");
    }
}
