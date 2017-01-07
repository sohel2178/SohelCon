package net.optimusbs.sohelcon.Utility;

import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Sohel on 10/26/2016.
 */

public class FontUtils {

    public static void setFont(String typeface, TextView textView){
        Typeface tf = Typeface.createFromAsset(textView.getContext().getAssets(),typeface);
        textView.setTypeface(tf);
    }

    public static void setFont(String typeface, EditText editText){
        Typeface tf = Typeface.createFromAsset(editText.getContext().getAssets(),typeface);
        editText.setTypeface(tf);
    }

    public static void setFont(String typeface, Button button){
        Typeface tf = Typeface.createFromAsset(button.getContext().getAssets(),typeface);
        button.setTypeface(tf);
    }



    public static void setFonts(List<View> views,String typeFace){

        for (View x: views){

            if(x instanceof TextView){
                setFont(typeFace, (TextView) x);
            }else if(x instanceof EditText){
                setFont(typeFace, (EditText) x);
            }else if(x instanceof Button){
                setFont(typeFace, (Button) x);
            }
        }
    }


}
