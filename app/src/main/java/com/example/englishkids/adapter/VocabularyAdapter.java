package com.example.englishkids.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide; // Thư viện Glide để tải hình ảnh
import com.example.englishkids.R;
import com.example.englishkids.entity.Vocabulary;
import java.util.List;

public class VocabularyAdapter extends RecyclerView.Adapter<VocabularyAdapter.VocabViewHolder> {

    private List<Vocabulary> vocabList;

    public VocabularyAdapter(List<Vocabulary> vocabList) {
        this.vocabList = vocabList;
    }

    @NonNull
    @Override
    public VocabViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vocabulary, parent, false);
        return new VocabViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VocabViewHolder holder, int position) {
        Vocabulary vocab = vocabList.get(position);
        holder.wordTextView.setText(vocab.word);
        holder.meaningTextView.setText(vocab.meaning);

        int imageResId = holder.itemView.getContext().getResources().getIdentifier(vocab.getImagePath(), "drawable", holder.itemView.getContext().getPackageName());

        Log.d("VocabularyAdapter", "ImageResId: " + imageResId + " for word: " + vocab.word); // Debugging line

        if (imageResId != 0) {
            Glide.with(holder.itemView.getContext())
                    .load(imageResId)
                    .error(R.drawable.apple) // Set a default error image
                    .into(holder.imgWordImage);
        } else {
            holder.imgWordImage.setImageResource(R.drawable.apple);
        }
    }


    @Override
    public int getItemCount() {
        return vocabList.size();
    }

    static class VocabViewHolder extends RecyclerView.ViewHolder {
        TextView wordTextView, meaningTextView;
        ImageView imgWordImage; // Thêm ImageView

        public VocabViewHolder(@NonNull View itemView) {
            super(itemView);
            wordTextView = itemView.findViewById(R.id.wordTextView);
            meaningTextView = itemView.findViewById(R.id.meaningTextView);
            imgWordImage = itemView.findViewById(R.id.img_word_image); // Khởi tạo ImageView
        }
    }
}
