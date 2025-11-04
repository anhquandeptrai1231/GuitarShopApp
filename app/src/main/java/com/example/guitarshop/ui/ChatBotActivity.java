package com.example.guitarshop.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.guitarshop.ChatAdapter;
import com.example.guitarshop.R;
import com.example.guitarshop.models.ChatMessage;
import com.example.guitarshop.api.ApiClient;
import com.example.guitarshop.api.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class ChatBotActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText editTextMessage;
    private Button buttonSend;
    private ChatAdapter adapter;
    private List<ChatMessage> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        recyclerView = findViewById(R.id.recyclerViewChat);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);

        adapter = new ChatAdapter(messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // 🟢 Tin nhắn chào mừng tự động khi mở khung chat
        messages.add(new ChatMessage("model", "🎸 Chào mừng bạn đến với đội ngũ hỗ trợ GuitarShop! Mình có thể giúp gì cho bạn hôm nay?"));
        adapter.notifyItemInserted(messages.size() - 1);
        recyclerView.scrollToPosition(messages.size() - 1);

        buttonSend.setOnClickListener(v -> sendMessage());
    }

    private void sendMessage() {
        String text = editTextMessage.getText().toString().trim();
        if (text.isEmpty()) return;

        messages.add(new ChatMessage("user", text));
        adapter.notifyItemInserted(messages.size() - 1);
        recyclerView.scrollToPosition(messages.size() - 1);
        editTextMessage.setText("");

        ApiService api = ApiClient.getClient().create(ApiService.class);
        HashMap<String, Object> body = new HashMap<>();

        List<HashMap<String, String>> history = new ArrayList<>();

        HashMap<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", text);

        history.add(message);
        body.put("history", history);

        api.chatWithAI(body).enqueue(new Callback<HashMap<String, String>>() {
            @Override
            public void onResponse(Call<HashMap<String, String>> call, Response<HashMap<String, String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String reply = response.body().get("response");
                    messages.add(new ChatMessage("model", reply));
                    adapter.notifyItemInserted(messages.size() - 1);
                    recyclerView.scrollToPosition(messages.size() - 1);
                }
            }

            @Override
            public void onFailure(Call<HashMap<String, String>> call, Throwable t) {
                messages.add(new ChatMessage("model", "❌ Lỗi kết nối đến server. Vui lòng thử lại sau."));
                adapter.notifyItemInserted(messages.size() - 1);
                recyclerView.scrollToPosition(messages.size() - 1);
            }
        });
    }
}
