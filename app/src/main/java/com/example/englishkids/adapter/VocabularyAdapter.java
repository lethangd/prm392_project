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

        // Thay đổi để tải ảnh từ một URL
        String imageUrl = vocab.getImagePath(); // Giả sử bạn đã thêm phương thức getImageUrl() trong lớp Vocabulary

        Log.d("VocabularyAdapter", "ImageUrl: " + imageUrl + " for word: " + vocab.word); // Debugging line

        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .error(R.drawable.apple) // Set a default error image
                .into(holder.imgWordImage);
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
