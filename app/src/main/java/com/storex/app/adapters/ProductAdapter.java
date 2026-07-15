package com.storex.app.adapters;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.storex.app.R;
import com.storex.app.models.Product;
import com.storex.app.utils.CartManager;

import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    public interface OnAddToCartListener {
        void onAdded(Product product);
    }

    private List<Product> products;
    private OnAddToCartListener listener;

    public ProductAdapter(List<Product> products, OnAddToCartListener listener) {
        this.products = products;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);

        holder.name.setText(product.getName());
        holder.desc.setText(product.getDescription());
        holder.price.setText(String.format(Locale.getDefault(), "💰 %.0f LYD", product.getPrice()));

        if (product.getImageRes() != 0) {
            holder.image.setVisibility(View.VISIBLE);
            holder.image.setImageResource(product.getImageRes());
            holder.emoji.setVisibility(View.GONE);
        } else {
            // ما فماش صورة حقيقية بعد -> نرجع للإيموجي كحل احتياطي
            holder.image.setVisibility(View.GONE);
            holder.emoji.setVisibility(View.VISIBLE);
            holder.emoji.setText(product.getEmoji());
        }

        holder.addButton.setOnClickListener(v -> {
            CartManager.getInstance().addToCart(product);
            if (listener != null) listener.onAdded(product);

            // نبضة صغيرة على الزر عند الإضافة
            v.animate().scaleX(0.9f).scaleY(0.9f).setDuration(80)
                    .withEndAction(() -> v.animate().scaleX(1f).scaleY(1f).setDuration(120).start())
                    .start();
        });

        // ===== حركة البطاقة عند اللمس (شعور "حي" فقط بصرياً) =====
        holder.cardRoot.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    v.animate()
                            .scaleX(0.96f).scaleY(0.96f)
                            .translationZ(18f)
                            .setDuration(120)
                            .start();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    v.animate()
                            .scaleX(1f).scaleY(1f)
                            .translationZ(0f)
                            .setDuration(150)
                            .start();
                    break;
            }
            return false; // نسمح بمرور اللمسة للأزرار الداخلية
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        View cardRoot;
        TextView name, desc, price, emoji;
        ImageView image;
        View addButton;

        ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            cardRoot = itemView.findViewById(R.id.cardRoot);
            name = itemView.findViewById(R.id.productName);
            desc = itemView.findViewById(R.id.productDesc);
            price = itemView.findViewById(R.id.productPrice);
            emoji = itemView.findViewById(R.id.productEmoji);
            image = itemView.findViewById(R.id.productImage);
            addButton = itemView.findViewById(R.id.btnAddToCart);
        }
    }
}
