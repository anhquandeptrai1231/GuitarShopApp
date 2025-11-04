package com.example.guitarshop;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.guitarshop.models.Category;
import com.example.guitarshop.ui.LoginActivity;
import com.example.guitarshop.utils.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerCategory;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;
    private ImageView btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Banner
        ViewPager2 viewPager = findViewById(R.id.viewPagerBanner);
        List<Integer> imageList = new ArrayList<>();
        imageList.add(R.drawable.banner1);
        imageList.add(R.drawable.banner2);
        imageList.add(R.drawable.banner3);
        BannerAdapter adapter = new BannerAdapter(this, imageList);
        viewPager.setAdapter(adapter);

        // Recycler Category
        recyclerCategory = findViewById(R.id.recyclerCategory);
        recyclerCategory.setLayoutManager(new GridLayoutManager(this, 2));
        categoryList = new ArrayList<>();
        categoryList.add(new Category(1, "Guitar", R.drawable.sample_guitar_banner));
        categoryList.add(new Category(2, "Ukulele", R.drawable.sample_guitar_banner));
        categoryList.add(new Category(3, "Bass", R.drawable.sample_guitar_banner));
        categoryList.add(new Category(4, "Phụ kiện", R.drawable.sample_guitar_banner));
        categoryAdapter = new CategoryAdapter(this, categoryList);
        recyclerCategory.setAdapter(categoryAdapter);

        // Logout
        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> showLogoutDialog());
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Đăng xuất")
                .setMessage("Bạn có chắc muốn đăng xuất không?")
                .setPositiveButton("Có", (dialog, which) -> {
                    PreferenceManager.clearToken(MainActivity.this);
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Không", null)
                .show();
    }
}
