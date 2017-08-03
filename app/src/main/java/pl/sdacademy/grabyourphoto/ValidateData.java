package pl.sdacademy.grabyourphoto;

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

}
