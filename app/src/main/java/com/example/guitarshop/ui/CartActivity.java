package com.example.guitarshop.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.guitarshop.CartAdapter;
import com.example.guitarshop.R;
import com.example.guitarshop.api.ApiClient;
import com.example.guitarshop.api.ApiResponse;
import com.example.guitarshop.api.ApiService;
import com.example.guitarshop.models.CartItem;
import com.example.guitarshop.utils.PreferenceManager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerCart;
    private TextView tvCartTotal;
    private Button btnCheckout;
    private List<CartItem> cartList = new ArrayList<>();
    private CartAdapter cartAdapter;
    private ApiService apiService;
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Ánh xạ view
        recyclerCart = findViewById(R.id.recyclerCart);
        tvCartTotal = findViewById(R.id.tvCartTotal);
        btnCheckout = findViewById(R.id.btnCheckout);

        // Tạo instance API
        apiService = ApiClient.getClient().create(ApiService.class);

        // ✅ Lấy userId từ SharedPreferences sau khi có context
        currentUserId = PreferenceManager.getUserId(this);
        if (currentUserId == 0 || currentUserId == -1) {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng, vui lòng đăng nhập lại!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Thiết lập RecyclerView
        recyclerCart.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(this, cartList, currentUserId, this::updateTotal);
        recyclerCart.setAdapter(cartAdapter);

        // Gọi API lấy giỏ hàng
        loadCart(currentUserId);

        // Nút thanh toán
        btnCheckout.setOnClickListener(v -> {
            Toast.makeText(this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadCart(int userId) {
        apiService.getCart(userId).enqueue(new Callback<ApiResponse<List<CartItem>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<CartItem>>> call, Response<ApiResponse<List<CartItem>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    cartList.clear();
                    cartList.addAll(response.body().getData());
                    cartAdapter.notifyDataSetChanged();
                    updateTotal();
                } else {
                    Toast.makeText(CartActivity.this, "Không lấy được giỏ hàng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<CartItem>>> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateTotal() {
        double total = 0;
        for (CartItem item : cartList) {
            total += item.getTotalPrice() * item.getQuantity();
        }
        DecimalFormat formatter = new DecimalFormat("#,###");
        tvCartTotal.setText("Tổng tiền: " + formatter.format(total) + " đ");
    }
}
