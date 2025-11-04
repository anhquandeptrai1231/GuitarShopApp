package com.example.guitarshop;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.guitarshop.api.ApiClient;
import com.example.guitarshop.api.ApiService;
import com.example.guitarshop.models.AddCartItemRequest;
import com.example.guitarshop.models.Product;
import com.example.guitarshop.ui.OrderDetailActivity;

import java.text.DecimalFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;
    private int userId;
    private OnProductClickListener listener;

    // Interface để callback sự kiện ra Activity
    public interface OnProductClickListener {
        void onBuyNow(Product product);
        void onAddToCart(Product product);
    }

    public ProductAdapter(Context context, List<Product> productList, int userId, OnProductClickListener listener) {
        this.context = context;
        this.productList = productList;
        this.userId = userId;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.tvName.setText(product.getName());

        DecimalFormat formatter = new DecimalFormat("#,###");
        holder.tvPrice.setText(formatter.format(product.getPrice()) + " đ");
        holder.tvDescription.setText(product.getDescription() != null ? product.getDescription() : "Chưa có mô tả");

        Glide.with(context)
                .load(product.getImageUrl())
                .placeholder(R.drawable.sample_guitar_banner)
                .error(R.drawable.sample_guitar_banner)
                .into(holder.ivImage);

        holder.btnBuyNow.setOnClickListener(v -> {
            if(listener != null) listener.onBuyNow(product);
        });

        holder.btnAddToCart.setOnClickListener(v -> {
            if(listener != null) listener.onAddToCart(product);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvDescription;
        ImageView ivImage;
        Button btnBuyNow, btnAddToCart;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
            tvDescription = itemView.findViewById(R.id.tvProductDescription);
            ivImage = itemView.findViewById(R.id.ivProductImage);
            btnBuyNow = itemView.findViewById(R.id.btnBuyNow);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        }
    }
}
