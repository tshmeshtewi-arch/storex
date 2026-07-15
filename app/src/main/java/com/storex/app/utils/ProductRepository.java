package com.storex.app.utils;

import com.storex.app.R;
import com.storex.app.models.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductRepository {

    /*
     * ملاحظة لإضافة منتج جديد لاحقاً:
     * 1) حط صورة المنتج (jpg/png/gif) داخل: app/src/main/res/drawable/
     *    باسم بأحرف صغيرة بدون مسافات (مثال: my_product.jpg)
     * 2) زيد سطر جديد هنا بصيغة:
     *    list.add(new Product(ID, "الاسم", "الوصف", السعر, "shop" أو "games", "إيموجي احتياطي", R.drawable.my_product));
     * 3) خلي كل ID رقم مختلف عن الباقي.
     */

    public static List<Product> getShopProducts() {
        List<Product> list = new ArrayList<>();
        list.add(new Product(1, "Rockstar", "حساب فايف ام مع تحميل لعبة GTA", 20, "shop", "🎮", R.drawable.fivem));
        list.add(new Product(2, "Discord Nitro", "اشتراك ديسكورد نيترو شهر بالتفعيل", 20, "shop", "💬", R.drawable.nitro));
        list.add(new Product(3, "NETFLIX", "حساب نتفلكس شهر", 35, "shop", "🎬", R.drawable.netflix));
        list.add(new Product(4, "Snapchat", "اشتراك سناب شات بلس", 30, "shop", "👻", R.drawable.snapchat));
        list.add(new Product(5, "Credit", "كريدت شحن", 10, "shop", "💳", R.drawable.pcredit));
        list.add(new Product(6, "Boosts Discord", "2 Boost 3 Months", 18, "shop", "🚀", R.drawable.bost));
        return list;
    }

    public static List<Product> getGamesProducts() {
        List<Product> list = new ArrayList<>();
        list.add(new Product(7, "GTA 5", "GTA 5 النسخة الأصلية", 75, "games", "🕹️", R.drawable.gta5));
        list.add(new Product(8, "FIFA 23", "لعبة كرة القدم الأقوى", 60, "games", "⚽", R.drawable.fc23));
        list.add(new Product(9, "Call of Duty", "أحدث إصدار من COD", 80, "games", "🔫", R.drawable.cod));
        list.add(new Product(10, "Red Dead Redemption 2", "لعبة الغرب الأمريكي الشهيرة", 90, "games", "🤠", R.drawable.rd2));
        return list;
    }
}
