package com.storex.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.storex.app.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private View loginForm, signupForm;
    private EditText loginEmail, loginPassword;
    private EditText signupName, signupEmail, signupPassword;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new SessionManager(this);

        // إذا كان المستخدم مسجل دخول مسبقاً، انتقل مباشرة للتطبيق
        if (session.isLoggedIn()) {
            goToMain();
            return;
        }

        loginForm = findViewById(R.id.loginForm);
        signupForm = findViewById(R.id.signupForm);

        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);

        signupName = findViewById(R.id.signupName);
        signupEmail = findViewById(R.id.signupEmail);
        signupPassword = findViewById(R.id.signupPassword);

        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnSignup = findViewById(R.id.btnSignup);
        TextView goToSignup = findViewById(R.id.goToSignup);
        TextView goToLogin = findViewById(R.id.goToLogin);

        btnLogin.setOnClickListener(v -> handleLogin());
        btnSignup.setOnClickListener(v -> handleSignup());

        goToSignup.setOnClickListener(v -> switchForm(loginForm, signupForm));
        goToLogin.setOnClickListener(v -> switchForm(signupForm, loginForm));
    }

    private void handleLogin() {
        String email = loginEmail.getText().toString().trim();
        String password = loginPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginEmail.setError("أدخل بريد إلكتروني صحيح");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            loginPassword.setError("أدخل كلمة السر");
            return;
        }

        boolean success = session.login(email, password);
        if (success) {
            Toast.makeText(this, "مرحباً بعودتك 👋", Toast.LENGTH_SHORT).show();
            goToMain();
        } else {
            Toast.makeText(this, "❌ البريد الإلكتروني أو كلمة السر غير صحيحة", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleSignup() {
        String name = signupName.getText().toString().trim();
        String email = signupEmail.getText().toString().trim();
        String password = signupPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            signupName.setError("أدخل الاسم كامل");
            return;
        }
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signupEmail.setError("أدخل بريد إلكتروني صحيح");
            return;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            signupPassword.setError("أقل شيء 6 أحرف");
            return;
        }

        boolean created = session.register(name, email, password);
        if (!created) {
            Toast.makeText(this, "⚠️ هذا البريد الإلكتروني مسجل مسبقاً", Toast.LENGTH_SHORT).show();
            return;
        }

        // تسجيل دخول مباشر بعد إنشاء الحساب
        session.login(email, password);
        Toast.makeText(this, "✅ تم إنشاء الحساب بنجاح", Toast.LENGTH_SHORT).show();
        goToMain();
    }

    /** تبديل بين نموذج الدخول ونموذج إنشاء الحساب بحركة انسيابية (fade) */
    private void switchForm(View from, View to) {
        from.animate()
                .alpha(0f)
                .setDuration(180)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        from.setVisibility(View.GONE);
                        from.setAlpha(1f);

                        to.setAlpha(0f);
                        to.setVisibility(View.VISIBLE);
                        to.animate().alpha(1f).setDuration(220).start();
                    }
                })
                .start();
    }

    private void goToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
