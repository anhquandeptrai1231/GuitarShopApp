package com.example.guitarshop.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.guitarshop.ProductAdapter;
import com.example.guitarshop.R;
import com.example.guitarshop.api.ApiClient;
import com.example.guitarshop.api.ApiService;
import com.example.guitarshop.models.AddCartItemRequest;
import com.example.guitarshop.models.Product;
import com.example.guitarshop.utils.PreferenceManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListActivity extends AppCompatActivity {

    RecyclerView recyclerProducts;
    ProductAdapter productAdapter;
    ApiService apiService;
    int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        currentUserId = PreferenceManager.getUserId(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerProducts = findViewById(R.id.recyclerProducts);
        recyclerProducts.setLayoutManager(new LinearLayoutManager(this));

        apiService = ApiClient.getClient().create(ApiService.class);
        int categoryId = getIntent().getIntExtra("categoryId", 0);

        apiService.getProductsByCategory(categoryId).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> products = response.body();

                    productAdapter = new ProductAdapter(ProductListActivity.this, products, currentUserId, new ProductAdapter.OnProductClickListener() {
                        @Override
                        public void onBuyNow(Product product) {
                            Intent intent = new Intent(ProductListActivity.this, OrderDetailActivity.class);
                            intent.putExtra("productName", product.getName());
                            intent.putExtra("productPrice", product.getPrice());
                            intent.putExtra("productImage", product.getImageUrl());
                            startActivity(intent);
                        }

                        @Override
                        public void onAddToCart(Product product) {
                            AddCartItemRequest request = new AddCartItemRequest();
                            request.userId = currentUserId;
                            request.productId = product.getId();
                            request.quantity = 1;

                            apiService.addToCart(request).enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(ProductListActivity.this, product.getName() + " đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                                    } else {
                                        try {
                                            String errorBody = response.errorBody() != null ? response.errorBody().string() : "null";
                                            Toast.makeText(ProductListActivity.this, "Thêm giỏ hàng thất bại: " + response.code() + " - " + errorBody, Toast.LENGTH_LONG).show();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Toast.makeText(ProductListActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });

                    recyclerProducts.setAdapter(productAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(ProductListActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem menuItem = menu.findItem(R.id.action_cart);
        View actionView = menuItem.getActionView();
        ImageView ivCart = actionView.findViewById(R.id.ivCart);
        TextView tvCartBadge = actionView.findViewById(R.id.tvCartBadge);

        // TODO: update badge số lượng giỏ hàng thực tế nếu cần
        tvCartBadge.setText("0");

        ivCart.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));
        return true;
    }
}
