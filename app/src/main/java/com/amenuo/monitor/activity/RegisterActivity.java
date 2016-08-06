package com.amenuo.monitor.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
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
import com.amenuo.monitor.action.CountDownAction;
import com.amenuo.monitor.action.PressHideKeyboardAction;
import com.amenuo.monitor.task.RegisterTask;
import com.amenuo.monitor.task.VerificationCodeTask;
import com.amenuo.monitor.utils.Constants;
import com.amenuo.monitor.utils.InputVerifyUtils;

public class RegisterActivity extends Activity implements OnClickListener, RegisterTask.Callback {


    private RegisterTask mAuthTask = null;
    private CountDownAction mCountDownAction;
    // UI references.
    private EditText mPhoneNumberView;
    private EditText mPasswordView;
    private EditText mVerificationCodeView;
    private View mProgressView;
    private View mRegisterFormView;
    private Button mGetVerificationCodeViewButton;

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == Constants.MSG_WHAT_COUNTDOWN) {
                int second = (int) mGetVerificationCodeViewButton.getTag();
                second--;
                if (second == -1) {
                    mCountDownAction.stop();
                    String text = getApplicationContext().getResources().getString(R.string.text_getVerificationCode);
                    mGetVerificationCodeViewButton.setText(text);
                    mGetVerificationCodeViewButton.setEnabled(true);
                } else {
                    mGetVerificationCodeViewButton.setTag(second);
                    mGetVerificationCodeViewButton.setText(second + "秒后重新获取");
                }
            }
            super.handleMessage(msg);
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_register);
        // Set up the register form.
        mPhoneNumberView = (EditText) findViewById(R.id.register_phoneNumber);
        mVerificationCodeView = (EditText) findViewById(R.id.register_verificationCode);

        mPasswordView = (EditText) findViewById(R.id.register_password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.register || id == EditorInfo.IME_NULL) {
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });

        Button registerButton = (Button) findViewById(R.id.register);
        registerButton.setOnClickListener(this);

        mGetVerificationCodeViewButton = (Button) findViewById(R.id.register_getVerificationCode);
        mGetVerificationCodeViewButton.setOnClickListener(this);
        mGetVerificationCodeViewButton.setTag(1);

        mRegisterFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.register_progress);

        mCountDownAction = new CountDownAction(mHandler);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCountDownAction.stop();
    }

    /**
     * Attempts to sign in or register the account specified by the register form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual register attempt is made.
     */
    private void attemptRegister() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mPhoneNumberView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the register attempt.
        String phoneNumber = mPhoneNumberView.getText().toString();
        String password = mPasswordView.getText().toString();
        String verificationCode = mVerificationCodeView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!InputVerifyUtils.verifyPassword(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (!InputVerifyUtils.verifyVerificationCode(verificationCode)) {
            mVerificationCodeView.setError(getString(R.string.error_invalid_password));
            focusView = mVerificationCodeView;
            cancel = true;
        }

        // Check for a valid email address.
        if (!InputVerifyUtils.verifyPhoneNumber(phoneNumber)) {
            mPhoneNumberView.setError(getString(R.string.error_invalid_phoneNumber));
            focusView = mPhoneNumberView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user register attempt.
            showProgress(true);
            mAuthTask = new RegisterTask(this);
            mAuthTask.execute(phoneNumber, password, verificationCode);
        }
        PressHideKeyboardAction.hideSoftInput(mPhoneNumberView.getWindowToken());
    }

    /**
     * Shows the progress UI and hides the register form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        int resId = v.getId();
        if (resId == R.id.register_getVerificationCode) {
            String phoneNumber = mPhoneNumberView.getText().toString();
            if (!InputVerifyUtils.verifyPhoneNumber(phoneNumber)) {
                mPhoneNumberView.setError(getString(R.string.error_invalid_phoneNumber));
                mPhoneNumberView.requestFocus();
                return;
            }
            mGetVerificationCodeViewButton.setTag(60);
            mGetVerificationCodeViewButton.setEnabled(false);
            mCountDownAction.start();
            new VerificationCodeTask(this).execute(phoneNumber);
        } else if (resId == R.id.register) {
            attemptRegister();
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
    public void onRegisterResult(boolean success) {
        if (success) {
            finish();
        } else {
            String errorString = getResources().getString(R.string.error_field_register);
            Toast.makeText(this, errorString, Toast.LENGTH_LONG).show();
        }
        showProgress(false);
    }
}
