package com.storex.app;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.storex.app.utils.CartManager;

import java.util.Locale;

/**
 * شاشة الدفع - واجهة فقط (Mock) بدون بوابة دفع حقيقية.
 * تقرا المجموع الفرعي ورسوم التوصيل من MainActivity/CartFragment عبر Intent extras.
 */
public class PaymentActivity extends AppCompatActivity {

    public static final String EXTRA_SUBTOTAL = "extra_subtotal";
    public static final String EXTRA_DELIVERY = "extra_delivery";

    private EditText inputCardName, inputCardNumber, inputCardExpiry, inputCardCvc;
    private TextView cardNumberPreview, cardNamePreview, cardExpiryPreview;
    private TextView summaryTotal, summaryDelivery;
    private Button btnPayNow;

    private double subtotal;
    private double delivery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        subtotal = getIntent().getDoubleExtra(EXTRA_SUBTOTAL, 0);
        delivery = getIntent().getDoubleExtra(EXTRA_DELIVERY, CartManager.DELIVERY_FEE);

        ImageButton btnClose = findViewById(R.id.btnClosePayment);
        btnClose.setOnClickListener(v -> finish());

        inputCardName = findViewById(R.id.inputCardName);
        inputCardNumber = findViewById(R.id.inputCardNumber);
        inputCardExpiry = findViewById(R.id.inputCardExpiry);
        inputCardCvc = findViewById(R.id.inputCardCvc);

        cardNumberPreview = findViewById(R.id.cardNumberPreview);
        cardNamePreview = findViewById(R.id.cardNamePreview);
        cardExpiryPreview = findViewById(R.id.cardExpiryPreview);

        summaryTotal = findViewById(R.id.summaryTotal);
        summaryDelivery = findViewById(R.id.summaryDelivery);
        btnPayNow = findViewById(R.id.btnPayNow);

        summaryTotal.setText(String.format(Locale.getDefault(), "%.0f LYD", subtotal + delivery));
        summaryDelivery.setText(String.format(Locale.getDefault(), "%.0f LYD", delivery));

        setupLivePreview();

        btnPayNow.setOnClickListener(v -> handlePay());
    }

    /** تحديث شكل البطاقة مباشرة أثناء الكتابة */
    private void setupLivePreview() {
        inputCardName.addTextChangedListener(simpleWatcher(s ->
                cardNamePreview.setText(TextUtils.isEmpty(s) ? "---" : s.toUpperCase(Locale.getDefault()))
        ));

        inputCardNumber.addTextChangedListener(simpleWatcher(s -> {
            StringBuilder grouped = new StringBuilder();
            for (int i = 0; i < s.length(); i++) {
                if (i > 0 && i % 4 == 0) grouped.append(" ");
                grouped.append(s.charAt(i));
            }
            // نكمل بالنقاط لباقي الخانات باش تفضل الكرت شكلها ثابت
            StringBuilder display = new StringBuilder(grouped.toString());
            int remaining = 16 - s.length();
            for (int i = 0; i < remaining; i++) {
                if ((s.length() + i) > 0 && (s.length() + i) % 4 == 0) display.append(" ");
                display.append("•");
            }
            cardNumberPreview.setText(display.toString());
        }));

        inputCardExpiry.addTextChangedListener(new TextWatcher() {
    private boolean isEditing;

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        if (isEditing) return;
        isEditing = true;

        String digits = s.toString().replace("/", "");
        if (digits.length() > 4) digits = digits.substring(0, 4);

        StringBuilder formatted = new StringBuilder();
        for (int i = 0; i < digits.length(); i++) {
            if (i == 2) formatted.append("/");
            formatted.append(digits.charAt(i));
        }

        inputCardExpiry.setText(formatted.toString());
        inputCardExpiry.setSelection(formatted.length());

        cardExpiryPreview.setText(TextUtils.isEmpty(formatted) ? "••/••" : formatted.toString());
        isEditing = false;
    }
});
    }

    private void handlePay() {
        String name = inputCardName.getText().toString().trim();
        String number = inputCardNumber.getText().toString().trim();
        String expiry = inputCardExpiry.getText().toString().trim();
        String cvc = inputCardCvc.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            inputCardName.setError("أدخل اسم صاحب البطاقة");
            return;
        }
        if (number.length() < 16) {
            inputCardNumber.setError("رقم البطاقة لازم 16 رقم");
            return;
        }
        if (!expiry.matches("^(0[1-9]|1[0-2])/\\d{2}$")) {
            inputCardExpiry.setError("الصيغة MM/YY");
            return;
        }
        if (cvc.length() < 3) {
            inputCardCvc.setError("رمز الأمان 3 أرقام");
            return;
        }

        // ⚠️ هذا الجزء واجهة فقط (Mock) - ما فماش بوابة دفع حقيقية متصلة
        CartManager.getInstance().clearCart();
        Toast.makeText(this, "✅ تم الدفع بنجاح! شكراً لتسوقك من StoreX", Toast.LENGTH_LONG).show();
        finish();
    }

    private TextWatcher simpleWatcher(OnTextChanged callback) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                callback.onChanged(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };
    }

    private interface OnTextChanged {
        void onChanged(String value);
    }
}
