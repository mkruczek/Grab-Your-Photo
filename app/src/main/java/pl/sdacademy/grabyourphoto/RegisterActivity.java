package pl.sdacademy.grabyourphoto;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private static final String URL_FOR_REGISTRATION = "http://logapi.grabyourphotos.pl/register.php";
    private ProgressDialog progressDialog;
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
    private RadioGroup genderRadioGroup;
    private boolean valid = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        progressDialog = new ProgressDialog(this);
        init();
        confirmPasswordRegister.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    validatePassword();
                }
            }
        });
    }

    private void validatePassword() {
        if(!confirmPasswordRegister.getText().toString().equals(passwordRegister.getText().toString())){
            confirmPasswordRegister.setError("Confirmation faild");
            btnRegister.setEnabled(false);
            valid = false;
        } else {
            btnRegister.setEnabled(true);
        }
    }

    public void register(View view) {
        String email = emailRegister.getEditableText().toString();
        String password = passwordRegister.getEditableText().toString();
        String confirmPassword = confirmPasswordRegister.getEditableText().toString();
        String name = nameRegister.getText().toString();
        String surname = surnameRegister.getEditableText().toString();
        String street = streetRegister.getEditableText().toString();
        String zipcode = zipcodeRegister.getEditableText().toString();
        String country = countryRegister.getEditableText().toString();
        String telephone = telephoneRegister.getEditableText().toString();

        validateByLength(email, emailRegister);

        validateByLength(password, passwordRegister);

        validateByLength(confirmPassword, confirmPasswordRegister);

        validateByLength(confirmPassword, confirmPasswordRegister);

        validateByLength(name, nameRegister);

        validateByLength(surname, surnameRegister);

        validateByLength(street, streetRegister);

        validateByLength(zipcode, zipcodeRegister);

        validateByLength(country, countryRegister);

        validateByLength(telephone, telephoneRegister);
        validatePassword();
        if(valid) submitForm();

    }

    private void validateByLength(String string, EditText editText) {
        if (string.length() == 0) {
            editText.setError(getString(R.string.error_empty_field));
            valid = false;
        }else {
            valid = true;
        }
    }
    //textwatcher lub onfocusedchanhed
    private void validatePassword(String password, String confirmedPassword, EditText editText){
        if(!password.equals(confirmedPassword)){

        }

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
        genderRadioGroup = (RadioGroup) findViewById(R.id.gender_radio_group);
    }

    private void submitForm() {

        int selectedId = genderRadioGroup.getCheckedRadioButtonId();
        String gender;
        if(selectedId == R.id.female_radio_btn)
            gender = "Female";
        else
            gender = "Male";

        registerUser(nameRegister.getText().toString(),
                emailRegister.getText().toString(),
                passwordRegister.getText().toString(),
                surnameRegister.getEditableText().toString(),
                streetRegister.getEditableText().toString(),
                zipcodeRegister.getEditableText().toString(),
                countryRegister.getEditableText().toString(),
                telephoneRegister.getEditableText().toString(),
                gender);
                //signupInputAge.getText().toString());
    }

    private void registerUser(final String name,  final String email, final String password,
                              final String surname, final String street, final String zipcode,
                              final String country, final String telephone, final String gender) {
        // Tag used to cancel the request
        String cancel_req_tag = "register";

        progressDialog.setMessage("Adding you ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                URL_FOR_REGISTRATION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        String user = jObj.getJSONObject("user").getString("name");
                        Toast.makeText(getApplicationContext(), "Hi " + user +", You are successfully Added!", Toast.LENGTH_SHORT).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                RegisterActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("gender", gender);
                params.put("age", "22");
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }




}
