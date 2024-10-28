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
        vocabularies.add(new Vocabulary("Apple", "Quả táo", "https://plus.unsplash.com/premium_photo-1661322640130-f6a1e2c36653?q=80&w=2670&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D", 1, false));
        vocabularies.add(new Vocabulary("Run", "Chạy", "https://unsplash.com/photos/pretty-young-woman-in-blue-track-suit-running-by-the-river-at-autumn-morning-IgYEb8fh8VM", 2, false));
        vocabularies.add(new Vocabulary("Study", "Học", "https://unsplash.com/photos/young-mother-helping-her-daughter-with-homework-at-home-KrG_EIpAS-M", 2, false));
        vocabularies.add(new Vocabulary("Eat", "Ăn", "https://static.vecteezy.com/system/resources/thumbnails/005/162/056/small_2x/cartoon-little-boy-eating-spaghetti-free-vector.jpg", 1, false));
        vocabularies.add(new Vocabulary("Dog", "Con chó", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTW7zpG7Q2QGK9YGchMgfbXycRtDimjSCjk8w&s", 1, false));
        vocabularies.add(new Vocabulary("Play", "Chơi", "https://thegeniusofplay.org/App_Themes/tgop/images/expertadvice/articles/play-in-park810x456.jpg", 2, false));
        vocabularies.add(new Vocabulary("Book", "Cuốn sách", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRPF7GVCOhi1rHsD2zwSidze85loxSp-9lLog&s", 1, false));
        vocabularies.add(new Vocabulary("Car", "Xe hơi", "https://imgd.aeplcdn.com/370x208/n/cw/ec/139651/curvv-exterior-right-front-three-quarter.jpeg?isig=0&q=80", 2, false));
        vocabularies.add(new Vocabulary("Teacher", "Giáo viên", "https://img.freepik.com/free-vector/hand-drawn-black-teacher-clipart-illustration_23-2150923180.jpg", 2, false));
        vocabularies.add(new Vocabulary("Friend", "Bạn bè", "https://haenglish.edu.vn/wp-content/uploads/2023/08/Best-Friend-Story-1.jpg", 1, false));

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

        vocabularies.add(new Vocabulary("Snow", "Tuyết", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQgILv6V5CCG-GQr1lgP3bNbyW2ugdXSqCniQ&s", 5, false));
        vocabularies.add(new Vocabulary("Wind", "Gió", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQFQ2U1msCK5LoxGSnifrmremGuup1T-RMVvg&s", 5, false));
        vocabularies.add(new Vocabulary("Fire", "Lửa", "https://images.vexels.com/media/users/3/146887/isolated/preview/41faeb4b7129b75f4883d75c72627835-fire-flame-clipart.png?w=360", 5, false));
        vocabularies.add(new Vocabulary("Earth", "Đất", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQiDzBupA046HfsR0HCaBdE2o80MT7Z-363VQ&s", 5, false));
        vocabularies.add(new Vocabulary("Ocean", "Đại dương", "https://cdn.shopify.com/s/files/1/0603/5439/6408/files/fishcircle_2000x952_09cd941e-e1e9-4ed6-9b93-a94166e9c37f.webp?v=1653509090", 5, false));
        vocabularies.add(new Vocabulary("River", "Dòng sông", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTLLG85m6wq6bgyTZmv-UQ6zHOoYQs-lFf1sg&s", 5, false));
        vocabularies.add(new Vocabulary("Mountain", "Ngọn núi", "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e7/Everest_North_Face_toward_Base_Camp_Tibet_Luca_Galuzzi_2006.jpg/800px-Everest_North_Face_toward_Base_Camp_Tibet_Luca_Galuzzi_2006.jpg", 5, false));
        vocabularies.add(new Vocabulary("Forest", "Khu rừng", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQkFDAaf0Rb3CCkYGLTS4ef194Geigc-FT2w&s", 5, false));
        vocabularies.add(new Vocabulary("Desert", "Sa mạc", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ3R-XHd0jCGVDhplQ_unClg5YsJdBF2e6htQ&s", 5, false));
        vocabularies.add(new Vocabulary("Beach", "Bãi biển", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSXo3xlrCq-EDrU-GDPowfLiKDeAUrGHSgCew&s", 5, false));

        vocabularies.add(new Vocabulary("Train", "Tàu hỏa", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTjTfZJ3HEP8QXAesOlRGRZfrw9oCYfT0UJpw&s", 6, false));
        vocabularies.add(new Vocabulary("Bus", "Xe buýt", "https://img.freepik.com/premium-vector/cartoon-school-bus-white-background_1072300-79.jpg", 6, false));
        vocabularies.add(new Vocabulary("Bicycle", "Xe đạp", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQP_Mj7kgeXBDNwFqlQ0Ug9WgkV1kd96cSTYg&s", 6, false));
        vocabularies.add(new Vocabulary("Airplane", "Máy bay", "https://i.abcnewsfe.com/a/29ad17e0-4dec-488a-9c27-bdc2424ba5a5/electric-plane-ht-ml-240110_1704902584341_hpMain_16x9.jpg?w=992", 6, false));
        vocabularies.add(new Vocabulary("Ship", "Tàu thủy", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ2Yom0yWhdAeO6veOjWePZSdCNHYBaznlVfA&s", 6, false));
        vocabularies.add(new Vocabulary("Motorcycle", "Xe máy", "https://content2.kawasaki.com/ContentStorage/KMV/ProductTrimGroup/32/aa6b43cf-7721-429c-8f55-2a8527f49752.jpg?w=750", 6, false));
        vocabularies.add(new Vocabulary("Train Station", "Ga tàu", "https://upload.wikimedia.org/wikipedia/commons/1/1e/Hanoi_train_station_from_trackside.jpg", 6, false));
        vocabularies.add(new Vocabulary("Airport", "Sân bay", "https://image.cnbcfm.com/api/v1/image/107177246-1673454132712-gettyimages-1246154739-AFP_336V8DZ.jpeg?v=1682946269&w=1480&h=833&ffmt=webp&vtcrop=y", 6, false));
        vocabularies.add(new Vocabulary("Market", "Chợ", "https://cdn.vietnamisawesome.com/wp-content/uploads/2023/02/668651407.jpg", 6, false));
        vocabularies.add(new Vocabulary("Hospital", "Bệnh viện", "https://thumbs.dreamstime.com/z/hospital-building-white-background-city-flat-style-47934085.jpg", 6, false));

        vocabularies.add(new Vocabulary("Pizza", "Bánh pizza", "https://daylambanh.edu.vn/wp-content/uploads/2022/10/cach-lam-pizza-bang-noi-chien-khong-dau.jpg", 7, false));
        vocabularies.add(new Vocabulary("Burger", "Bánh mì kẹp", "https://burgerking.vn/media/catalog/product/cache/1/small_image/316x/9df78eab33525d08d6e5fb8d27136e95/1/9/19-burger-b_-n_ng-whopper-ph_-mai-th_t-heo-x_ng-kh_i_-bbq_1.jpg", 7, false));
        vocabularies.add(new Vocabulary("Ice Cream", "Kem", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTjZ5QpVr0Yq0taYNdA7vg1AfD7KUfEbk_NXQ&s", 7, false));
        vocabularies.add(new Vocabulary("Pasta", "Mì Ý", "https://www.allrecipes.com/thmb/JPQcpUKRsPXhUZm0H-XZUpjrp8w=/0x512/filters:no_upscale():max_bytes(150000):strip_icc()/67700_RichPastaforthePoorKitchen_ddmfs_4x3_2284-220302ec8328442096df370dede357d7.jpg", 7, false));
        vocabularies.add(new Vocabulary("Salad", "Salad", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTcOKw8TpfrH31D_AfVEPGzPYnNTvVth75nFg&s", 7, false));

        // Vocabulary for lesson 8 (Animals)
        vocabularies.add(new Vocabulary("Elephant", "Con voi", "https://upload.wikimedia.org/wikipedia/commons/thumb/3/37/African_Bush_Elephant.jpg/1200px-African_Bush_Elephant.jpg", 8, false));
        vocabularies.add(new Vocabulary("Lion", "Sư tử", "https://www.shutterstock.com/image-photo/lion-family-4-person-vertical-600w-2430115969.jpg", 8, false));
        vocabularies.add(new Vocabulary("Tiger", "Hổ", "https://cdn.britannica.com/83/195983-138-66807699/numbers-tiger-populations.jpg?w=800&h=450&c=crop", 8, false));
        vocabularies.add(new Vocabulary("Monkey", "Con khỉ", "https://media.newyorker.com/photos/59095bb86552fa0be682d9d0/master/pass/Monkey-Selfie.jpg", 8, false));
        vocabularies.add(new Vocabulary("Giraffe", "Hươu cao cổ", "https://d1jyxxz9imt9yb.cloudfront.net/medialib/4659/image/s768x1300/AdobeStock_331202050_538219_reduced.jpg", 8, false));

        // Vocabulary for lesson 9 (Jobs)
        vocabularies.add(new Vocabulary("Doctor", "Bác sĩ", "https://img.freepik.com/premium-vector/boy-with-stethoscope-around-his-neck_1013341-328801.jpg?semt=ais_hybrid", 9, false));
        vocabularies.add(new Vocabulary("Engineer", "Kỹ sư", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSJ08-IuzbiqS398HMmcMp5oicV00RDI_8i3g&s", 9, false));
        vocabularies.add(new Vocabulary("Teacher", "Giáo viên", "https://img.freepik.com/free-vector/hand-drawn-black-teacher-clipart-illustration_23-2150923180.jpg", 9, false));
        vocabularies.add(new Vocabulary("Chef", "Đầu bếp", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT1DxB8UnYh4fJRarIPFSHxa_TXp13hRBWsiw&s", 9, false));
        vocabularies.add(new Vocabulary("Police Officer", "Cảnh sát", "https://m.media-amazon.com/images/I/51ucFarzXAL._AC_UF1000,1000_QL80_.jpg", 9, false));

        // Vocabulary for lesson 10 (School Supplies)
        vocabularies.add(new Vocabulary("Pencil", "Bút chì", "https://img.freepik.com/premium-vector/pencil-personal-use-vector-illustration_1275990-6151.jpg", 10, false));
        vocabularies.add(new Vocabulary("Eraser", "Cục tẩy", "https://cdn.theschoollocker.com.au/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/5/2/526_c20.jpg", 10, false));
        vocabularies.add(new Vocabulary("Notebook", "Vở", "https://m.media-amazon.com/images/I/718vM+75UNL.jpg", 10, false));
        vocabularies.add(new Vocabulary("Ruler", "Thước kẻ", "https://images-na.ssl-images-amazon.com/images/I/71YTbLEY0NL.jpg", 10, false));
        vocabularies.add(new Vocabulary("Backpack", "Ba lô", "https://cdn.thewirecutter.com/wp-content/media/2022/09/backpacks-2048px-9904.jpg?auto=webp&quality=75&width=1024", 10, false));

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
