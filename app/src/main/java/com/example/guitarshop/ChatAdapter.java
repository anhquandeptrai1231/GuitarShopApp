package com.example.guitarshop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.guitarshop.models.ChatMessage;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_USER = 0;
    private static final int VIEW_TYPE_BOT = 1;

    private List<ChatMessage> messages;

    public ChatAdapter(List<ChatMessage> messages) {
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).getRole().equals("user") ? VIEW_TYPE_USER : VIEW_TYPE_BOT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_USER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_user, parent, false);
            return new UserViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_bot, parent, false);
            return new BotViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChatMessage msg = messages.get(position);
        if (holder instanceof UserViewHolder) {
            ((UserViewHolder) holder).textView.setText(msg.getContent());
        } else {
            ((BotViewHolder) holder).textView.setText(msg.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        UserViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textMessageUser);
        }
    }

    static class BotViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        BotViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textMessageBot);
        }
    }
}
