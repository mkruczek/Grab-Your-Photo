package pl.sdacademy.grabyourphoto;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Grzegorz on 2017-07-13.
 */

public final class ValidateData {
    String string;

    public String getString() {
        return string;
    }

    public static boolean validatePassword(String password) {
        return password.length() > 6;
    }

    public static boolean validateEmail(String email) {
        final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher;
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void validZipCode(final EditText text){
        text.setFilters(new InputFilter[] { new InputFilter.LengthFilter(6) });
        text.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == android.view.KeyEvent.KEYCODE_DEL) {
                    text.setText("");
                }
                return false;
            }
        });

        text.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 2 ){
                    String convert = s + "-";
                    text.setText(convert);
                    text.setSelection(3);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
    }

    public static void validPhoneNumber(final EditText text){
        text.setFilters(new InputFilter[] {new InputFilter.LengthFilter(9) });
    }

}
