package edu.psu.sweng888.nightout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private Button mBtnLoginFacebook = null;
    private Button mBtnLoginPaypal = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mBtnLoginFacebook = (Button) findViewById(R.id.btn_login_facebook);
        mBtnLoginPaypal = (Button) findViewById(R.id.btn_login_google);

        mBtnLoginFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Calling Facebook External API To Login", Toast.LENGTH_SHORT).show();
            }
        });
        mBtnLoginPaypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this,"Calling Google External API to Login", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
