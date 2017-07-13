package pl.sdacademy.grabyourphoto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private EditText emailLogin;
    private EditText passwordLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailLogin = (EditText) findViewById(R.id.emailLogin);
        passwordLogin = (EditText) findViewById(R.id.passwordLogin);
    }

    public void toRegister(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    public void logIn(View view) {
        String email = emailLogin.getEditableText().toString();
        String password = passwordLogin.getEditableText().toString();

        if(email.length() == 0) {
            emailLogin.setError(getString(R.string.error_enter_email));
        }else if (!ValidateData.validateEmail(email)){
            emailLogin.setError(getString(R.string.error_wrong_email));
        }

        if(password.length() == 0) {
            passwordLogin.setError(getString(R.string.error_enter_password));
        }else if (!ValidateData.validatePassword(password)){
            passwordLogin.setError(getString(R.string.error_wrong_password));
        }

    }
}
