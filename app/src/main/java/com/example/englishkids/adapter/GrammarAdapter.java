package com.example.englishkids.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.englishkids.R;
import com.example.englishkids.entity.Grammar;
import java.util.List;

public class GrammarAdapter extends RecyclerView.Adapter<GrammarAdapter.GrammarViewHolder> {

    private List<Grammar> grammarList;

    public GrammarAdapter(List<Grammar> grammarList) {
        this.grammarList = grammarList;
    }

    @NonNull
    @Override
    public GrammarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grammar, parent, false);
        return new GrammarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GrammarViewHolder holder, int position) {
        Grammar grammar = grammarList.get(position);
        holder.sentenceTextView.setText(grammar.getCorrectSentence());
    }

    @Override
    public int getItemCount() {
        return grammarList.size();
    }

    static class GrammarViewHolder extends RecyclerView.ViewHolder {
        TextView sentenceTextView;

        public GrammarViewHolder(@NonNull View itemView) {
            super(itemView);
            sentenceTextView = itemView.findViewById(R.id.sentenceTextView);
        }
    }
}
