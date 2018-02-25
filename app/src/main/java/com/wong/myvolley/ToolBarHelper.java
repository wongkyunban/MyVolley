package com.wong.myvolley;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by wong on 18-2-25.
 */

public class ToolBarHelper {



    public static void setMiddleTitle(Context context,String title,Toolbar toolbar){

        TextView textView = new TextView(context);
        textView.setTextColor(0xffffffff);
        textView.setTextSize(18);
        TextPaint paint = textView.getPaint();
        paint.setFakeBoldText(true);
        textView.setText(title);
        setToolbar(toolbar,textView);

    }

    public static void setMiddleTitle(Context context,String title,Toolbar toolbar,int textColor,int textSize){

        TextView textView = new TextView(context);
        textView.setTextColor(textColor);
        textView.setTextSize(textSize);
        TextPaint paint = textView.getPaint();
        paint.setFakeBoldText(true);
        textView.setText(title);
        setToolbar(toolbar,textView);
    }
    private static void setToolbar(Toolbar toolbar,View view){
        Toolbar.LayoutParams params = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT,Toolbar.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        toolbar.addView(view,params);
    }
}
