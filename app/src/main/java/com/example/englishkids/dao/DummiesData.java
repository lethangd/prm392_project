package com.example.englishkids.dao;

import android.util.Log;
import androidx.annotation.NonNull;

import com.example.englishkids.dao.AppDatabase;
import com.example.englishkids.entity.Grammar;
import com.example.englishkids.entity.GrammarType;
import com.example.englishkids.entity.Lesson;
import com.example.englishkids.entity.Vocabulary;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DummiesData {

    private static final String TAG = "DummiesData";
    private static final ExecutorService executorService= Executors.newSingleThreadExecutor();

    public static void insertDummyData(AppDatabase db) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Step 1: Insert initial dummy data if tables are empty
        Log.d(TAG, "Inserting dummy data...");

        if (db.grammarTypeDao().getAllGrammarTypes().isEmpty()) {
            insertGrammarTypes(db);
        }

        if (db.lessonDao().getAllLessons().isEmpty()) {
            insertLessons(db);
        }

        if (db.grammarDao().getAllGrammar().isEmpty()) {
            insertGrammars(db);
        }

        if (db.vocabularyDao().getAllVocabulary().isEmpty()) {
            insertVocabulary(db);
        }

        // Step 2: Sync learned data from Firestore
        syncLearnedDataFromFirestore(db, firestore, userId);
    }

    private static void insertGrammarTypes(AppDatabase db) {
        List<GrammarType> grammarTypes = new ArrayList<>();
        grammarTypes.add(new GrammarType(1, "Simple Present", "Thì hiện tại đơn"));
        grammarTypes.add(new GrammarType(2, "Present Continuous", "Thì hiện tại tiếp diễn"));
        grammarTypes.add(new GrammarType(3, "Past Simple", "Thì quá khứ đơn"));
        grammarTypes.add(new GrammarType(4, "Future Simple", "Thì tương lai đơn"));
        grammarTypes.add(new GrammarType(5, "Present Perfect", "Thì hiện tại hoàn thành"));
        grammarTypes.add(new GrammarType(6, "Past Perfect", "Thì quá khứ hoàn thành"));
        grammarTypes.add(new GrammarType(7, "Future Perfect", "Thì tương lai hoàn thành"));
        grammarTypes.add(new GrammarType(8, "Present Perfect Continuous", "Thì hiện tại hoàn thành tiếp diễn"));
        grammarTypes.add(new GrammarType(9, "Past Perfect Continuous", "Thì quá khứ hoàn thành tiếp diễn"));
        grammarTypes.add(new GrammarType(10, "Conditional Sentences", "Câu điều kiện"));
        db.grammarTypeDao().insertAll(grammarTypes);
    }

    private static void insertLessons(AppDatabase db) {
        List<Lesson> lessons = new ArrayList<>();
        lessons.add(new Lesson("Lesson 1", "Thì hiện tại đơn", "Grammar"));
        lessons.add(new Lesson("Lesson 2", "Thì hiện tại tiếp diễn", "Grammar"));
        lessons.add(new Lesson("Lesson 3", "Thì quá khứ đơn", "Grammar"));
        lessons.add(new Lesson("Lesson 4", "Thì tương lai đơn", "Grammar"));
        lessons.add(new Lesson("Lesson 5", "Thì hiện tại hoàn thành", "Grammar"));
        lessons.add(new Lesson("Lesson 6", "Thì quá khứ hoàn thành", "Grammar"));
        lessons.add(new Lesson("Lesson 7", "Thì tương lai hoàn thành", "Grammar"));
        lessons.add(new Lesson("Lesson 8", "Thì hiện tại hoàn thành tiếp diễn", "Grammar"));
        lessons.add(new Lesson("Lesson 9", "Thì quá khứ hoàn thành tiếp diễn", "Grammar"));
        lessons.add(new Lesson("Lesson 10", "Câu điều kiện", "Grammar"));
        db.lessonDao().insertAll(lessons);
    }

    private static void insertGrammars(AppDatabase db) {
        List<Grammar> grammars = new ArrayList<>();
        grammars.add(new Grammar(1, "I eat breakfast every day.", 1, false));
        grammars.add(new Grammar(1, "She reads a book.", 1, false));
        grammars.add(new Grammar(2, "They are playing soccer.", 2, false));
        grammars.add(new Grammar(2, "He is studying now.", 2, false));
        grammars.add(new Grammar(3, "I visited my grandmother last week.", 3, false));
        grammars.add(new Grammar(3, "She watched a movie yesterday.", 3, false));
        grammars.add(new Grammar(4, "I will travel to Japan next year.", 4, false));
        grammars.add(new Grammar(4, "We will have a meeting tomorrow.", 4, false));

        grammars.add(new Grammar(5, "I have eaten breakfast.", 5, false));
        grammars.add(new Grammar(5, "She has read the book.", 5, false));
        grammars.add(new Grammar(6, "I had visited my grandmother before she moved.", 6, false));
        grammars.add(new Grammar(6, "She had watched the movie before it was released.", 6, false));
        grammars.add(new Grammar(7, "I will have traveled to Japan by next year.", 7, false));
        grammars.add(new Grammar(7, "We will have finished the project by Friday.", 7, false));
        grammars.add(new Grammar(8, "I have been eating breakfast for an hour.", 8, false));
        grammars.add(new Grammar(8, "She has been reading that book all day.", 8, false));
        grammars.add(new Grammar(9, "I had been visiting my grandmother before she passed away.", 9, false));
        grammars.add(new Grammar(9, "She had been watching movies for three hours.", 9, false));

        // Conditional sentences
        grammars.add(new Grammar(10, "If it rains, I will stay at home.", 10, false));
        grammars.add(new Grammar(10, "If I had known, I would have gone.", 10, false));
        db.grammarDao().insertAll(grammars);
    }

    private static void insertVocabulary(AppDatabase db) {
        List<Vocabulary> vocabularies = new ArrayList<>();
        vocabularies.add(new Vocabulary("Apple", "Quả táo", "path/to/image1", 1, false));
        vocabularies.add(new Vocabulary("Run", "Chạy", "path/to/image2", 2, false));
        vocabularies.add(new Vocabulary("Study", "Học", "path/to/image3", 2, false));
        vocabularies.add(new Vocabulary("Eat", "Ăn", "path/to/image4", 1, false));
        vocabularies.add(new Vocabulary("Dog", "Con chó", "path/to/image5", 1, false));
        vocabularies.add(new Vocabulary("Play", "Chơi", "path/to/image6", 2, false));
        vocabularies.add(new Vocabulary("Book", "Cuốn sách", "path/to/image7", 1, false));
        vocabularies.add(new Vocabulary("Car", "Xe hơi", "path/to/image8", 2, false));
        vocabularies.add(new Vocabulary("Teacher", "Giáo viên", "path/to/image9", 2, false));
        vocabularies.add(new Vocabulary("Friend", "Bạn bè", "path/to/image10", 1, false));

        vocabularies.add(new Vocabulary("House", "Ngôi nhà", "path/to/image11", 3, false));
        vocabularies.add(new Vocabulary("Water", "Nước", "path/to/image12", 3, false));
        vocabularies.add(new Vocabulary("Chair", "Cái ghế", "path/to/image13", 3, false));
        vocabularies.add(new Vocabulary("Table", "Cái bàn", "path/to/image14", 3, false));
        vocabularies.add(new Vocabulary("Window", "Cửa sổ", "path/to/image15", 3, false));
        vocabularies.add(new Vocabulary("Door", "Cửa", "path/to/image16", 3, false));
        vocabularies.add(new Vocabulary("Phone", "Điện thoại", "path/to/image17", 3, false));
        vocabularies.add(new Vocabulary("Computer", "Máy tính", "path/to/image18", 3, false));
        vocabularies.add(new Vocabulary("City", "Thành phố", "path/to/image19", 3, false));
        vocabularies.add(new Vocabulary("Country", "Quốc gia", "path/to/image20", 3, false));

        vocabularies.add(new Vocabulary("Food", "Thức ăn", "path/to/image21", 4, false));
        vocabularies.add(new Vocabulary("Drink", "Đồ uống", "path/to/image22", 4, false));
        vocabularies.add(new Vocabulary("School", "Trường học", "path/to/image23", 4, false));
        vocabularies.add(new Vocabulary("Garden", "Khu vườn", "path/to/image24", 4, false));
        vocabularies.add(new Vocabulary("Sun", "Mặt trời", "path/to/image25", 4, false));
        vocabularies.add(new Vocabulary("Moon", "Mặt trăng", "path/to/image26", 4, false));
        vocabularies.add(new Vocabulary("Star", "Ngôi sao", "path/to/image27", 4, false));
        vocabularies.add(new Vocabulary("Sky", "Bầu trời", "path/to/image28", 4, false));
        vocabularies.add(new Vocabulary("Cloud", "Đám mây", "path/to/image29", 4, false));
        vocabularies.add(new Vocabulary("Rain", "Mưa", "path/to/image30", 4, false));

        vocabularies.add(new Vocabulary("Snow", "Tuyết", "path/to/image31", 5, false));
        vocabularies.add(new Vocabulary("Wind", "Gió", "path/to/image32", 5, false));
        vocabularies.add(new Vocabulary("Fire", "Lửa", "path/to/image33", 5, false));
        vocabularies.add(new Vocabulary("Earth", "Đất", "path/to/image34", 5, false));
        vocabularies.add(new Vocabulary("Ocean", "Đại dương", "path/to/image35", 5, false));
        vocabularies.add(new Vocabulary("River", "Dòng sông", "path/to/image36", 5, false));
        vocabularies.add(new Vocabulary("Mountain", "Ngọn núi", "path/to/image37", 5, false));
        vocabularies.add(new Vocabulary("Forest", "Khu rừng", "path/to/image38", 5, false));
        vocabularies.add(new Vocabulary("Desert", "Sa mạc", "path/to/image39", 5, false));
        vocabularies.add(new Vocabulary("Beach", "Bãi biển", "path/to/image40", 5, false));

        vocabularies.add(new Vocabulary("Train", "Tàu hỏa", "path/to/image41", 6, false));
        vocabularies.add(new Vocabulary("Bus", "Xe buýt", "path/to/image42", 6, false));
        vocabularies.add(new Vocabulary("Bicycle", "Xe đạp", "path/to/image43", 6, false));
        vocabularies.add(new Vocabulary("Airplane", "Máy bay", "path/to/image44", 6, false));
        vocabularies.add(new Vocabulary("Ship", "Tàu thủy", "path/to/image45", 6, false));
        vocabularies.add(new Vocabulary("Motorcycle", "Xe máy", "path/to/image46", 6, false));
        vocabularies.add(new Vocabulary("Train Station", "Ga tàu", "path/to/image47", 6, false));
        vocabularies.add(new Vocabulary("Airport", "Sân bay", "path/to/image48", 6, false));
        vocabularies.add(new Vocabulary("Market", "Chợ", "path/to/image49", 6, false));
        vocabularies.add(new Vocabulary("Hospital", "Bệnh viện", "path/to/image50", 6, false));
        db.vocabularyDao().insertAll(vocabularies);
    }

    private static void syncLearnedDataFromFirestore(AppDatabase db, FirebaseFirestore firestore, String userId) {
        // Sync learned Vocabulary
        firestore.collection("userProgress").document(userId).collection("learnedVocabulary")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        int vocabId = Integer.parseInt(document.getId());
                        executorService.execute(() -> {
                            db.vocabularyDao().markAsLearned(vocabId);
                        });

                    }
                    Log.d(TAG, "Vocabulary progress synced from Firestore.");
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error syncing vocabulary progress", e));

        // Sync learned Grammar
        firestore.collection("userProgress").document(userId).collection("learnedGrammar")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        int grammarId = Integer.parseInt(document.getId());
                        executorService.execute(() -> {
                            db.grammarDao().markAsLearned(grammarId);
                        });

                    }
                    Log.d(TAG, "Grammar progress synced from Firestore.");
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error syncing grammar progress", e));
    }
}
