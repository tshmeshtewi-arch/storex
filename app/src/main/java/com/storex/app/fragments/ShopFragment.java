package com.storex.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.storex.app.R;
import com.storex.app.adapters.ProductAdapter;
import com.storex.app.utils.ProductRepository;

public class ShopFragment extends Fragment {

    private RecyclerView recyclerView;
    private TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shop, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.productsRecycler);
        tabLayout = view.findViewById(R.id.categoryTabs);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        showProducts(ProductRepository.getShopProducts());

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    showProducts(ProductRepository.getShopProducts());
                } else {
                    showProducts(ProductRepository.getGamesProducts());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void showProducts(java.util.List<com.storex.app.models.Product> products) {
        ProductAdapter adapter = new ProductAdapter(products, product ->
                Toast.makeText(getContext(), "✅ تمت إضافة " + product.getName() + " للسلة", Toast.LENGTH_SHORT).show()
        );
        recyclerView.setAdapter(adapter);
    }
}
