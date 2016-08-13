package com.amenuo.monitor.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amenuo.monitor.R;
import com.amenuo.monitor.action.PressHideKeyboardAction;
import com.amenuo.monitor.task.LoginTask;
import com.amenuo.monitor.utils.InputVerifyUtils;
import com.amenuo.monitor.utils.NetUtils;
import com.amenuo.monitor.utils.PToast;

public class LoginActivity extends Activity implements OnClickListener, LoginTask.Callback{

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private LoginTask mAuthTask = null;

    // UI references.
    private EditText mPhoneNumberView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button mRegisterButton;
    private Button mFindPwdButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_login);
        // Set up the login form.
        mPhoneNumberView = (EditText) findViewById(R.id.login_phoneNumber);

        mPasswordView = (EditText) findViewById(R.id.login_password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mSignInButton = (Button) findViewById(R.id.login);
        mSignInButton.setOnClickListener(this);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mRegisterButton = (Button) findViewById(R.id.login_register_btn);
        mRegisterButton.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG ); //下划线
        mRegisterButton.getPaint().setAntiAlias(true);//抗锯齿
        mRegisterButton.setOnClickListener(this);

        mFindPwdButton = (Button)findViewById(R.id.login_findpwd_btn);
        mFindPwdButton.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG ); //下划线
        mFindPwdButton.getPaint().setAntiAlias(true);//抗锯齿
        mFindPwdButton.setOnClickListener(this);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mPhoneNumberView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String phoneNumber = mPhoneNumberView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!InputVerifyUtils.verifyPassword(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (!InputVerifyUtils.verifyPhoneNumber(phoneNumber)) {
            mPhoneNumberView.setError(getString(R.string.error_invalid_phoneNumber));
            focusView = mPhoneNumberView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            if (NetUtils.isAvailable(this)){
//                onLoginResult(true);
                mAuthTask = new LoginTask(this);
                mAuthTask.execute(phoneNumber, password);
            }else {
                PToast.show("网络连接失败，请稍后重试");
            }
        }
        PressHideKeyboardAction.hideSoftInput(mPhoneNumberView.getWindowToken());
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();

            if (PressHideKeyboardAction.isShouldHideInput(v, ev)) {
                PressHideKeyboardAction.hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View v) {
        int resId = v.getId();
        if (resId == R.id.login_register_btn){
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        }else if(resId == R.id.login){
            attemptLogin();
        }else if (resId == R.id.login_findpwd_btn){

        }
    }

    @Override
    public void onLoginResult(boolean success) {
        if (success){
            Intent intent = new Intent();
            intent.setClass(this, MainPageActivity.class);
            startActivity(intent);
            finish();
        }else{
            String errorString = getResources().getString(R.string.error_field_login);
            Toast.makeText(this, errorString,Toast.LENGTH_LONG).show();
            showProgress(false);
        }
        mAuthTask = null;
    }
}
