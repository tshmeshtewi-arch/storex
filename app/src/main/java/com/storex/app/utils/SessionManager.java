package com.storex.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * إدارة الحسابات وتسجيل الدخول محلياً (بدون سيرفر)
 * كل حساب يتخزن في SharedPreferences بصيغة: user_<email> = "الاسم||كلمة السر"
 */
public class SessionManager {

    private static final String PREF_NAME = "storex_session";
    private static final String KEY_LOGGED_EMAIL = "logged_in_email";

    private SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // ==== إنشاء حساب ====
    public boolean register(String name, String email, String password) {
        String key = "user_" + email.trim().toLowerCase();
        if (prefs.contains(key)) {
            return false; // الحساب موجود مسبقاً
        }
        prefs.edit().putString(key, name + "||" + password).apply();
        return true;
    }

    // ==== تسجيل الدخول ====
    public boolean login(String email, String password) {
        String key = "user_" + email.trim().toLowerCase();
        String data = prefs.getString(key, null);
        if (data == null) return false;

        String[] parts = data.split("\\|\\|");
        if (parts.length != 2) return false;

        if (parts[1].equals(password)) {
            prefs.edit().putString(KEY_LOGGED_EMAIL, email.trim().toLowerCase()).apply();
            return true;
        }
        return false;
    }

    public void logout() {
        prefs.edit().remove(KEY_LOGGED_EMAIL).apply();
    }

    public boolean isLoggedIn() {
        return prefs.getString(KEY_LOGGED_EMAIL, null) != null;
    }

    public String getLoggedInEmail() {
        return prefs.getString(KEY_LOGGED_EMAIL, "");
    }

    public String getLoggedInName() {
        String email = getLoggedInEmail();
        if (email.isEmpty()) return "مستخدم";
        String data = prefs.getString("user_" + email, null);
        if (data == null) return "مستخدم";
        String[] parts = data.split("\\|\\|");
        return parts.length > 0 ? parts[0] : "مستخدم";
    }
}
