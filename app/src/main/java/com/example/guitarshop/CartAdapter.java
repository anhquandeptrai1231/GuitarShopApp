package com.example.guitarshop;

import android.content.Context;
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
import com.example.guitarshop.models.CartItem;
import com.example.guitarshop.models.UpdateCartItemRequest;

import java.text.DecimalFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    public interface CartListener {
        void onCartChanged();
    }

    private Context context;
    private List<CartItem> cartList;
    private int userId;
    private CartListener listener;
    private ApiService apiService;

    public CartAdapter(Context context, List<CartItem> cartList, int userId, CartListener listener) {
        this.context = context;
        this.cartList = cartList;
        this.userId = userId;
        this.listener = listener;
        this.apiService = ApiClient.getClient().create(ApiService.class);
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartList.get(position);

        holder.tvName.setText(item.getProductName());

        DecimalFormat formatter = new DecimalFormat("#,###");
        holder.tvPrice.setText(formatter.format(item.getProductPrice()) + " đ");
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));

        Glide.with(context)
                .load(item.getProductImage())
                .placeholder(R.drawable.sample_guitar_banner)
                .into(holder.ivImage);

        holder.btnIncrease.setOnClickListener(v -> updateQuantity(item, item.getQuantity() + 1));
        holder.btnDecrease.setOnClickListener(v -> {
            if (item.getQuantity() > 1) updateQuantity(item, item.getQuantity() - 1);
        });
        holder.btnRemove.setOnClickListener(v -> removeItem(item));
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    private void updateQuantity(CartItem item, int newQuantity) {
        UpdateCartItemRequest request = new UpdateCartItemRequest();
        request.cartItemId = item.id;
        request.quantity = newQuantity;

        apiService.updateCartItem(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {

                    item.setQuantity(newQuantity);
                    notifyDataSetChanged();
                    listener.onCartChanged();

                    Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Lỗi cập nhật số lượng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void removeItem(CartItem item) {
        apiService.removeCartItem(item.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    cartList.remove(item);
                    notifyDataSetChanged();
                    listener.onCartChanged();
                } else {
                    Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvQuantity;
        ImageView ivImage;
        Button btnIncrease, btnDecrease, btnRemove;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCartItemName);
            tvPrice = itemView.findViewById(R.id.tvCartItemPrice);
            tvQuantity = itemView.findViewById(R.id.tvCartItemQuantity);
            ivImage = itemView.findViewById(R.id.ivCartItemImage);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }
}
