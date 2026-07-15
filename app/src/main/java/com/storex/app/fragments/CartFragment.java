package com.storex.app.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.storex.app.PaymentActivity;
import com.storex.app.R;
import com.storex.app.adapters.CartAdapter;
import com.storex.app.utils.CartManager;

import java.util.Locale;

public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView emptyText, cartSubtotal, cartDeliveryFee, cartTotal;
    private Button btnClear, btnCheckout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.cartRecycler);
        emptyText = view.findViewById(R.id.emptyCartText);
        cartSubtotal = view.findViewById(R.id.cartSubtotal);
        cartDeliveryFee = view.findViewById(R.id.cartDeliveryFee);
        cartTotal = view.findViewById(R.id.cartTotal);
        btnClear = view.findViewById(R.id.btnClearCart);
        btnCheckout = view.findViewById(R.id.btnCheckout);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        btnClear.setOnClickListener(v -> {
            CartManager.getInstance().clearCart();
            refresh();
        });

        btnCheckout.setOnClickListener(v -> {
            if (CartManager.getInstance().getItems().isEmpty()) return;
            Intent intent = new Intent(getActivity(), PaymentActivity.class);
            intent.putExtra(PaymentActivity.EXTRA_SUBTOTAL, CartManager.getInstance().getSubtotal());
            intent.putExtra(PaymentActivity.EXTRA_DELIVERY, CartManager.DELIVERY_FEE);
            startActivity(intent);
        });

        refresh();
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        boolean empty = CartManager.getInstance().getItems().isEmpty();
        emptyText.setVisibility(empty ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(empty ? View.GONE : View.VISIBLE);

        CartAdapter adapter = new CartAdapter(
                CartManager.getInstance().getItems(),
                item -> {
                    CartManager.getInstance().removeItem(item.getProduct());
                    refresh();
                },
                new CartAdapter.OnQuantityChangeListener() {
                    @Override
                    public void onIncrease(com.storex.app.models.Product product) {
                        CartManager.getInstance().increaseQuantity(product);
                        refresh();
                    }

                    @Override
                    public void onDecrease(com.storex.app.models.Product product) {
                        CartManager.getInstance().decreaseQuantity(product);
                        refresh();
                    }
                }
        );
        recyclerView.setAdapter(adapter);

        double subtotal = CartManager.getInstance().getSubtotal();
        double delivery = empty ? 0 : CartManager.DELIVERY_FEE;
        double total = CartManager.getInstance().getGrandTotal();

        cartSubtotal.setText(String.format(Locale.getDefault(), "%.0f LYD", subtotal));
        cartDeliveryFee.setText(String.format(Locale.getDefault(), "%.0f LYD", delivery));
        cartTotal.setText(String.format(Locale.getDefault(), "%.0f LYD", total));

        btnCheckout.setEnabled(!empty);
        btnCheckout.setAlpha(empty ? 0.5f : 1f);
    }
}
