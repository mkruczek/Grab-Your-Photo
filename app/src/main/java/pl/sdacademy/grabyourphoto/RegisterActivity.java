package pl.sdacademy.grabyourphoto;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailRegister;
    private EditText passwordRegister;
    private EditText confirmPasswordRegister;
    private EditText nameRegister;
    private EditText surnameRegister;
    private EditText streetRegister;
    private EditText zipcodeRegister;
    private AutoCompleteTextView countryRegister;
    private EditText telephoneRegister;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();

    }



    public void register(View view) {
        String email = emailRegister.getEditableText().toString();
        String password = passwordRegister.getEditableText().toString();
        String confirmPassword = confirmPasswordRegister.getEditableText().toString();
        String name = nameRegister.getText().toString();
        String surname = surnameRegister.getEditableText().toString();
        String street = streetRegister.getEditableText().toString();
        String zipcode = zipcodeRegister.getEditableText().toString();
        String conutry = countryRegister.getEditableText().toString();
        String telephone = telephoneRegister.getEditableText().toString();

        validateByLength(email, emailRegister);

        validateByLength(password, passwordRegister);

        validateByLength(confirmPassword, confirmPasswordRegister);

        validateByLength(confirmPassword, confirmPasswordRegister);

        validateByLength(name, nameRegister);

        validateByLength(surname, surnameRegister);

        validateByLength(street, streetRegister);

        validateByLength(zipcode, zipcodeRegister);

        validateByLength(conutry, countryRegister);

        validateByLength(telephone, telephoneRegister);

    }

    private void validateByLength(String string, EditText editText) {
        if (string.length() == 0) editText.
                setError(getString(R.string.error_empty_field));
    }

    private void init() {
        emailRegister = (EditText) findViewById(R.id.emailRegister);
        passwordRegister = (EditText) findViewById(R.id.passwordRegister);
        confirmPasswordRegister = (EditText) findViewById(R.id.confirmPasswordRegister);
        nameRegister = (EditText) findViewById(R.id.nameRegister);
        surnameRegister = (EditText) findViewById(R.id.surnameRegister);
        streetRegister = (EditText) findViewById(R.id.streetRegister);
        zipcodeRegister = (EditText) findViewById(R.id.zipcodeRegister);
        countryRegister = (AutoCompleteTextView) findViewById(R.id.countryRegister);
        telephoneRegister = (EditText) findViewById(R.id.telephoneRegister);
        btnRegister = (Button) findViewById(R.id.btnRegister);
    }
}
