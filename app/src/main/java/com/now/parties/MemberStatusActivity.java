package com.now.parties;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MemberStatusActivity extends AppCompatActivity {

    private Button mButtonToSignIn;
    private Button mButtonToSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_status);

        mButtonToSignIn = (Button) findViewById(R.id.buttonToSignIn);
        mButtonToSignUp = (Button) findViewById(R.id.buttonToSignUp);

        mButtonToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sign_in_intent = new Intent(MemberStatusActivity.this, SignInActivity.class);
                startActivity(sign_in_intent);
                overridePendingTransition(R.anim.right_in_animation, R.anim.not_move_animation);
            }
        });

        mButtonToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sign_up_intent = new Intent(MemberStatusActivity.this, SignUpActivity.class);
                startActivity(sign_up_intent);
                overridePendingTransition(R.anim.right_in_animation, R.anim.not_move_animation);
            }
        });



    }
}
