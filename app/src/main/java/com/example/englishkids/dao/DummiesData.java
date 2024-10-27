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
        // Lesson 1: Simple Present
        grammars.add(new Grammar(1, "I eat breakfast every day.", 1, false));
        grammars.add(new Grammar(1, "She reads a book.", 1, false));
        grammars.add(new Grammar(1, "They go to school on foot.", 1, false));
        grammars.add(new Grammar(1, "He works in a bank.", 1, false));

        // Lesson 2: Present Continuous
        grammars.add(new Grammar(2, "They are playing soccer.", 2, false));
        grammars.add(new Grammar(2, "He is studying now.", 2, false));
        grammars.add(new Grammar(2, "She is cooking dinner.", 2, false));
        grammars.add(new Grammar(2, "I am watching TV.", 2, false));

        // Lesson 3: Past Simple
        grammars.add(new Grammar(3, "I visited my grandmother last week.", 3, false));
        grammars.add(new Grammar(3, "She watched a movie yesterday.", 3, false));
        grammars.add(new Grammar(3, "They traveled to Paris last summer.", 3, false));
        grammars.add(new Grammar(3, "He finished his homework last night.", 3, false));

        // Lesson 4: Future Simple
        grammars.add(new Grammar(4, "I will travel to Japan next year.", 4, false));
        grammars.add(new Grammar(4, "We will have a meeting tomorrow.", 4, false));
        grammars.add(new Grammar(4, "She will start her new job next month.", 4, false));
        grammars.add(new Grammar(4, "They will visit us next weekend.", 4, false));

        // Lesson 5: Present Perfect
        grammars.add(new Grammar(5, "I have eaten breakfast.", 5, false));
        grammars.add(new Grammar(5, "She has read the book.", 5, false));
        grammars.add(new Grammar(5, "They have traveled to many countries.", 5, false));
        grammars.add(new Grammar(5, "He has completed his project.", 5, false));

        // Lesson 6: Past Perfect
        grammars.add(new Grammar(6, "I had visited my grandmother before she moved.", 6, false));
        grammars.add(new Grammar(6, "She had watched the movie before it was released.", 6, false));
        grammars.add(new Grammar(6, "They had finished their work before the deadline.", 6, false));
        grammars.add(new Grammar(6, "He had learned Spanish before going to Spain.", 6, false));

        // Lesson 7: Future Perfect
        grammars.add(new Grammar(7, "I will have traveled to Japan by next year.", 7, false));
        grammars.add(new Grammar(7, "We will have finished the project by Friday.", 7, false));
        grammars.add(new Grammar(7, "She will have completed her degree by next semester.", 7, false));
        grammars.add(new Grammar(7, "They will have moved into their new house by the end of the month.", 7, false));

        // Lesson 8: Present Perfect Continuous
        grammars.add(new Grammar(8, "I have been eating breakfast for an hour.", 8, false));
        grammars.add(new Grammar(8, "She has been reading that book all day.", 8, false));
        grammars.add(new Grammar(8, "They have been working on this project for weeks.", 8, false));
        grammars.add(new Grammar(8, "He has been studying English for five years.", 8, false));

        // Lesson 9: Past Perfect Continuous
        grammars.add(new Grammar(9, "I had been visiting my grandmother before she passed away.", 9, false));
        grammars.add(new Grammar(9, "She had been watching movies for three hours.", 9, false));
        grammars.add(new Grammar(9, "They had been living in that city for ten years before they moved.", 9, false));
        grammars.add(new Grammar(9, "He had been working at that company for two years.", 9, false));

        // Lesson 10: Conditional Sentences
        grammars.add(new Grammar(10, "If it rains, I will stay at home.", 10, false));
        grammars.add(new Grammar(10, "If I had known, I would have gone.", 10, false));
        grammars.add(new Grammar(10, "If you study hard, you will pass the exam.", 10, false));
        grammars.add(new Grammar(10, "If I win the lottery, I will travel the world.", 10, false));

        db.grammarDao().insertAll(grammars);
    }

    private static void insertVocabulary(AppDatabase db) {
        List<Vocabulary> vocabularies = new ArrayList<>();
        vocabularies.add(new Vocabulary("Apple", "Quả táo", "https://unsplash.com/photos/fresh-red-apples-in-the-wooden-box-on-black-background-top-view-9OrF6J9AcVA", 1, false));
        vocabularies.add(new Vocabulary("Run", "Chạy", "https://unsplash.com/photos/pretty-young-woman-in-blue-track-suit-running-by-the-river-at-autumn-morning-IgYEb8fh8VM", 2, false));
        vocabularies.add(new Vocabulary("Study", "Học", "https://unsplash.com/photos/young-mother-helping-her-daughter-with-homework-at-home-KrG_EIpAS-M", 2, false));
        vocabularies.add(new Vocabulary("Eat", "Ăn", "https://static.vecteezy.com/system/resources/thumbnails/005/162/056/small_2x/cartoon-little-boy-eating-spaghetti-free-vector.jpg", 1, false));
        vocabularies.add(new Vocabulary("Dog", "Con chó", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTW7zpG7Q2QGK9YGchMgfbXycRtDimjSCjk8w&s", 1, false));
        vocabularies.add(new Vocabulary("Play", "Chơi", "https://thegeniusofplay.org/App_Themes/tgop/images/expertadvice/articles/play-in-park810x456.jpg", 2, false));
        vocabularies.add(new Vocabulary("Book", "Cuốn sách", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRPF7GVCOhi1rHsD2zwSidze85loxSp-9lLog&s", 1, false));
        vocabularies.add(new Vocabulary("Car", "Xe hơi", "https://imgd.aeplcdn.com/370x208/n/cw/ec/139651/curvv-exterior-right-front-three-quarter.jpeg?isig=0&q=80", 2, false));
        vocabularies.add(new Vocabulary("Teacher", "Giáo viên", "https://img.freepik.com/free-vector/hand-drawn-black-teacher-clipart-illustration_23-2150923180.jpg", 2, false));
        vocabularies.add(new Vocabulary("Friend", "Bạn bè", "https://thumbs.dreamstime.com/b/cute-happy-kid-hand-shake-friend-agreement-background-boy-cartoon-character-children-clipart-design-friends-friendship-160885724.jpg", 1, false));

        vocabularies.add(new Vocabulary("House", "Ngôi nhà", "https://media.istockphoto.com/id/155666671/vector/vector-illustration-of-red-house-icon.jpg?s=612x612&w=0&k=20&c=tBqaabvmjFOBVUibZxbd8oWJqrFR5dy-l2bEDJMtZ40=", 3, false));
        vocabularies.add(new Vocabulary("Water", "Nước", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQBsZm6v-x7WX2T3bSZKfD-gNhkgtkN3Gijlg&s", 3, false));
        vocabularies.add(new Vocabulary("Chair", "Cái ghế", "https://rusticreddoor.com/cdn/shop/products/hawthorne-rustic-ladder-back-chair.jpg?v=1643843712&width=1946", 3, false));
        vocabularies.add(new Vocabulary("Table", "Cái bàn", "https://images.pickawood.com/gfx/conf/tables/new/berlin-et-kernbuche-natur-geoelt-swiss-legn.jpg", 3, false));
        vocabularies.add(new Vocabulary("Window", "Cửa sổ", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS8vk9rjmtn-oViNGbptjYlHCN41TOhXwD2pA&s", 3, false));
        vocabularies.add(new Vocabulary("Door", "Cửa", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTqOJO39YPHl92ZSktG7mIFAbh97MaS7MD6IA&s", 3, false));
        vocabularies.add(new Vocabulary("Phone", "Điện thoại", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT_gjiZtDAmBZbI1lRov01KwnE9P8zsJ_XR-A&s", 3, false));
        vocabularies.add(new Vocabulary("Computer", "Máy tính", "https://upload.wikimedia.org/wikipedia/commons/thumb/1/1a/Crystal_Project_computer.png/200px-Crystal_Project_computer.png", 3, false));
        vocabularies.add(new Vocabulary("City", "Thành phố", "https://img.freepik.com/free-photo/shiny-night-city_1127-8.jpg", 3, false));
        vocabularies.add(new Vocabulary("Country", "Quốc gia", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS7Hoz2EV6tvYlmfNnmmVLXDtE8qYhFcCkSCA&s", 3, false));

        vocabularies.add(new Vocabulary("Food", "Thức ăn", "https://img.freepik.com/premium-vector/variety-food-including-variety-different-foods_1166763-16220.jpg?semt=ais_hybrid", 4, false));
        vocabularies.add(new Vocabulary("Drink", "Đồ uống", "https://www.beanilla.com/wp/wp-content/uploads/2022/06/RefreshingDrinks-1024x683.jpg", 4, false));
        vocabularies.add(new Vocabulary("School", "Trường học", "https://media.istockphoto.com/id/1480246301/vector/vector-illustration-of-high-school-building-vector-school-building.jpg?s=612x612&w=0&k=20&c=vR6dixHuh8Ypw1c3pjR-7ahN2V1vhCKxxTzDd7HlVbY=", 4, false));
        vocabularies.add(new Vocabulary("Garden", "Khu vườn", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSKx2FD1WzOeQhIvMk2Bqo3ttWIUutQuUD1Gg&s", 4, false));
        vocabularies.add(new Vocabulary("Sun", "Mặt trời", "https://easydrawingguides.com/wp-content/uploads/2018/09/Sun-10.png", 4, false));
        vocabularies.add(new Vocabulary("Moon", "Mặt trăng", "https://www.rmg.co.uk/sites/default/files/styles/full_width_1440/public/Color-Full%20Moon%20%C2%A9%20Nicolas%20Lefaudeux.jpg?itok=LCd5IvmA", 4, false));
        vocabularies.add(new Vocabulary("Star", "Ngôi sao", "https://img.freepik.com/free-vector/3d-metal-star-isolated_1308-117760.jpg", 4, false));
        vocabularies.add(new Vocabulary("Sky", "Bầu trời", "https://img.freepik.com/free-photo/white-cloud-blue-sky-sea_74190-4488.jpg", 4, false));
        vocabularies.add(new Vocabulary("Cloud", "Đám mây", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT8RjJzHiQBXmg-H4p4awTe7y1ZNmJ6YWoA9w&s", 4, false));
        vocabularies.add(new Vocabulary("Rain", "Mưa", "https://static.vecteezy.com/system/resources/thumbnails/042/146/565/small_2x/ai-generated-beautiful-rain-day-view-photo.jpg", 4, false));

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
