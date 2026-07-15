package com.storex.app.utils;

import com.storex.app.models.CartItem;
import com.storex.app.models.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * إدارة السلة (Singleton) - في الذاكرة فقط
 * ملاحظة: لا يوجد حالياً زر شراء فعلي، السلة للعرض والتجميع فقط
 * إلى حين إضافة طريقة الدفع لاحقاً
 */
public class CartManager {

    /** رسوم التوصيل الثابتة (LYD) - غيّرها من هنا إذا لزم */
    public static final double DELIVERY_FEE = 5;

    private static CartManager instance;
    private List<CartItem> items = new ArrayList<>();

    private CartManager() {}

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addToCart(Product product) {
        for (CartItem item : items) {
            if (item.getProduct().getId() == product.getId()) {
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }
        items.add(new CartItem(product, 1));
    }

    public void removeItem(Product product) {
        CartItem toRemove = null;
        for (CartItem item : items) {
            if (item.getProduct().getId() == product.getId()) {
                toRemove = item;
                break;
            }
        }
        if (toRemove != null) items.remove(toRemove);
    }

    /** زيادة الكمية بواحد */
    public void increaseQuantity(Product product) {
        for (CartItem item : items) {
            if (item.getProduct().getId() == product.getId()) {
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }
    }

    /** إنقاص الكمية بواحد، وإذا وصلت لصفر تتشال من السلة */
    public void decreaseQuantity(Product product) {
        CartItem toRemove = null;
        for (CartItem item : items) {
            if (item.getProduct().getId() == product.getId()) {
                item.setQuantity(item.getQuantity() - 1);
                if (item.getQuantity() <= 0) toRemove = item;
                break;
            }
        }
        if (toRemove != null) items.remove(toRemove);
    }

    public void clearCart() {
        items.clear();
    }

    public List<CartItem> getItems() {
        return items;
    }

    public int getItemCount() {
        int count = 0;
        for (CartItem item : items) count += item.getQuantity();
        return count;
    }

    /** المجموع الفرعي (بدون رسوم التوصيل) */
    public double getSubtotal() {
        double total = 0;
        for (CartItem item : items) total += item.getTotalPrice();
        return total;
    }

    public double getTotalPrice() {
        return getSubtotal();
    }

    /** المجموع الكلي شامل رسوم التوصيل (يظهر فقط إذا السلة ماشيش فارغة) */
    public double getGrandTotal() {
        if (items.isEmpty()) return 0;
        return getSubtotal() + DELIVERY_FEE;
    }
}
