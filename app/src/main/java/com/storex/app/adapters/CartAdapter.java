package com.storex.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.storex.app.R;
import com.storex.app.models.CartItem;
import com.storex.app.models.Product;

import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    public interface OnRemoveListener {
        void onRemove(CartItem item);
    }

    public interface OnQuantityChangeListener {
        void onIncrease(Product product);
        void onDecrease(Product product);
    }

    private List<CartItem> items;
    private OnRemoveListener removeListener;
    private OnQuantityChangeListener quantityListener;

    public CartAdapter(List<CartItem> items, OnRemoveListener removeListener,
                        OnQuantityChangeListener quantityListener) {
        this.items = items;
        this.removeListener = removeListener;
        this.quantityListener = quantityListener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = items.get(position);
        Product product = item.getProduct();

        holder.name.setText(product.getName());
        holder.qtyPrice.setText(String.format(Locale.getDefault(), "%.0f LYD", item.getTotalPrice()));
        holder.qty.setText(String.valueOf(item.getQuantity()));

        if (product.getImageRes() != 0) {
            holder.image.setVisibility(View.VISIBLE);
            holder.image.setImageResource(product.getImageRes());
            holder.emoji.setVisibility(View.GONE);
        } else {
            holder.image.setVisibility(View.GONE);
            holder.emoji.setVisibility(View.VISIBLE);
            holder.emoji.setText(product.getEmoji());
        }

        holder.removeBtn.setOnClickListener(v -> {
            if (removeListener != null) removeListener.onRemove(item);
        });

        holder.increaseBtn.setOnClickListener(v -> {
            if (quantityListener != null) quantityListener.onIncrease(product);
        });

        holder.decreaseBtn.setOnClickListener(v -> {
            if (quantityListener != null) quantityListener.onDecrease(product);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView emoji, name, qtyPrice, qty;
        ImageView image;
        ImageButton removeBtn, increaseBtn, decreaseBtn;

        CartViewHolder(@NonNull View itemView) {
            super(itemView);
            emoji = itemView.findViewById(R.id.itemEmoji);
            image = itemView.findViewById(R.id.itemImage);
            name = itemView.findViewById(R.id.itemName);
            qtyPrice = itemView.findViewById(R.id.itemQtyPrice);
            qty = itemView.findViewById(R.id.itemQty);
            removeBtn = itemView.findViewById(R.id.btnRemoveItem);
            increaseBtn = itemView.findViewById(R.id.btnIncreaseQty);
            decreaseBtn = itemView.findViewById(R.id.btnDecreaseQty);
        }
    }
}
