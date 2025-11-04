package com.example.guitarshop.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.guitarshop.R;

import java.text.DecimalFormat;

public class OrderDetailActivity extends AppCompatActivity {

    TextView tvName, tvPrice, tvTotal;
    ImageView ivImage;
    Button btnPay, btnIncrease, btnDecrease;
    EditText etQuantity;

    double price = 0.0;
    int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        tvName = findViewById(R.id.tvProductName);
        tvPrice = findViewById(R.id.tvProductPrice);
        tvTotal = findViewById(R.id.tvTotalPrice);
        ivImage = findViewById(R.id.ivProductImage);
        etQuantity = findViewById(R.id.etQuantity);
        btnPay = findViewById(R.id.btnPay);
        btnIncrease = findViewById(R.id.btnIncrease);
        btnDecrease = findViewById(R.id.btnDecrease);

        // Lấy dữ liệu từ Intent
        String name = getIntent().getStringExtra("productName");
        price = getIntent().getDoubleExtra("productPrice", 0.0);
        String imageUrl = getIntent().getStringExtra("productImage");

        tvName.setText(name);
        tvPrice.setText(formatCurrency(price));
        etQuantity.setText(String.valueOf(quantity));
        updateTotal();

        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.sample_guitar_banner)
                .error(R.drawable.sample_guitar_banner)
                .into(ivImage);

        // Nút tăng số lượng
        btnIncrease.setOnClickListener(v -> {
            quantity++;
            etQuantity.setText(String.valueOf(quantity));
            updateTotal();
        });

        // Nút giảm số lượng
        btnDecrease.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                etQuantity.setText(String.valueOf(quantity));
                updateTotal();
            }
        });

        // Cập nhật số lượng khi nhập trực tiếp
        etQuantity.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                try {
                    int val = Integer.parseInt(etQuantity.getText().toString());
                    quantity = val > 0 ? val : 1;
                } catch (NumberFormatException e) {
                    quantity = 1;
                }
                etQuantity.setText(String.valueOf(quantity));
                updateTotal();
            }
        });

        btnPay.setOnClickListener(v -> {
            double totalPrice = price * quantity;
            Toast.makeText(OrderDetailActivity.this,
                    "Thanh toán thành công cho " + name +
                            "\nSố lượng: " + quantity +
                            "\nTổng tiền: " + formatCurrency(totalPrice),
                    Toast.LENGTH_LONG).show();
            finish();
        });
    }

    private void updateTotal() {
        double totalPrice = price * quantity;
        tvTotal.setText("Tổng tiền: " + formatCurrency(totalPrice));
    }

    private String formatCurrency(double amount) {
        DecimalFormat formatter = new DecimalFormat("#,### đ");
        return formatter.format(amount);
    }
}
