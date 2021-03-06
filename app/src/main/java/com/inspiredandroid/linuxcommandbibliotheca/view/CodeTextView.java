package com.inspiredandroid.linuxcommandbibliotheca.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import com.inspiredandroid.linuxcommandbibliotheca.R;
import com.inspiredandroid.linuxcommandbibliotheca.misc.FragmentCoordinator;

/**
 * Created by Simon Schubert
 * <p/>
 * This View makes it very easy to highlightQueryInsideText defined mCommands in an normal textview. Define the
 * mCommands which should be highlighted in an string array and link it in the layout resource as
 * "command".
 */
public class CodeTextView extends TextView {

    private String[] mCommands;

    public CodeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CodeTextView);
        int resID = ta.getResourceId(R.styleable.CodeTextView_commands, R.array.default_codetextview_commands);
        mCommands = context.getResources().getStringArray(resID);
        ta.recycle();

        if (isInEditMode()) {
            return;
        }

        updateLinks();
        setMovementMethod(LinkMovementMethod.getInstance());
        setHighlightColor(Color.TRANSPARENT);
    }

    /**
     * Set clickable man pages(mCommands)
     *
     * @param commands
     */
    public void setCommands(String[] commands) {
        mCommands = commands;
        updateLinks();
    }

    /**
     * Mark man pages(mCommands) clickable
     */
    private void updateLinks() {
        setText(createSpannable(getText().toString(), mCommands));
    }

    /**
     * Highlights Commands of the text and make them clickable
     *
     * @param text     spannable content
     * @param commands list of mCommands to highlightQueryInsideText
     * @return
     */
    private SpannableString createSpannable(String text, String[] commands) {
        SpannableString ss = new SpannableString(text);

        for (final String command : commands) {
            ClickableTextView.addClickableSpanToPhrases(ss, text, command, () ->
                    FragmentCoordinator.startCommandManActivity((FragmentActivity) getContext(), command));
        }

        addItalicSpans(ss, text);

        return ss;
    }

    /**
     * Make placeholder text italic
     *
     * @param ss
     * @param text
     */
    private void addItalicSpans(SpannableString ss, String text) {
        int indexStart = 0;
        while (text.indexOf("[", indexStart) != -1) {
            int start = text.indexOf("[", indexStart);
            int end = text.indexOf("]", indexStart);
            if (start == -1 || end == -1 || start >= end) {
                break;
            }
            ss.setSpan(new StyleSpan(Typeface.ITALIC), start, end + 1, 0);
            indexStart = end + 1;
        }
    }
}