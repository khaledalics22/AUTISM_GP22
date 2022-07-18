package com.example.GUI;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ocvapp.R;

public class ItemActivity extends AppCompatActivity {
    String[] name;
    int[] ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemactivity);
        Intent intent = getIntent();
        final int position = intent.getIntExtra("position", 0);
        switch (position) {
            case 0:
                name = new String[]{"تفاحة", "مَوْزَة", "فَرْخَةٌ", "جبنة",
                        "لَحْمٌ", "بايض", "أُرْزٌ", "بطاطس", "كعكة", "بيدزا", "ساندوتش",
                        "طماطم", "شاي", "حليب", "سمكة", "شكولاتا", "خبز", "فول",
                        "آيِس كِرِيم", "ملح", "فلفل"
                };
                ids = new int[]{
                        R.drawable.cards_food_apple, R.drawable.cards_food_banana,
                        R.drawable.cards_food_chicken, R.drawable.cards_food_cheese, R.drawable.cards_food_steak, R.drawable.cards_food_egg,
                        R.drawable.cards_food_rice, R.drawable.cards_food_frenchfries,
                        R.drawable.cards_food_pie, R.drawable.cards_food_pizza, R.drawable.cards_food_sandwich,
                        R.drawable.cards_food_tomato, R.drawable.cards_food_tea,
                        R.drawable.cards_food_milk, R.drawable.cards_food_fish,
                        R.drawable.cards_food_chocolate,
                        R.drawable.cards_food_bread, R.drawable.cards_food_beans,
                        R.drawable.cards_food_icecream, R.drawable.cards_food_salt, R.drawable.cards_food_spices
                };
                break;
            case 1:
                name = new String[]{"نحلة", "فراشة", "قطة", "فَرْخَةٌ", "بَقَرَةٌ", "تمساح",
                        "كلب", "بطة", "فيل", "سَمَكَةٌ", "ضفدع", "زرافة", "حصان",
                        "أسد", "قرد", "فأر", "باندا", "أرنب",
                        "خروف", "ثعبان", "سلحفاة"

                };
                ids = new int[]{
                        R.drawable.cards_animals_bee, R.drawable.cards_animals_butterfly,
                        R.drawable.cards_animals_cat, R.drawable.cards_animals_chicken,
                        R.drawable.cards_animals_cow, R.drawable.cards_animals_crocodile,
                        R.drawable.cards_animals_dog, R.drawable.cards_animals_duck,
                        R.drawable.cards_animals_elephant, R.drawable.cards_animals_fish,
                        R.drawable.cards_animals_frog, R.drawable.cards_animals_giraffe,
                        R.drawable.cards_animals_horse, R.drawable.cards_animals_lion,
                        R.drawable.cards_animals_monkey, R.drawable.cards_animals_mouse,
                        R.drawable.cards_animals_panda, R.drawable.cards_animals_rabbit,
                        R.drawable.cards_animals_sheep, R.drawable.cards_animals_snake,
                        R.drawable.cards_animals_turtl
                };
                break;
            case 2:
                name = new String[]{"حقيبة", "بنطال", "حذاء", "ساعة", "ثوب", "نظارة", "قبعة", "معطف",
                        "قَمِيصٌ", "حزام"
                };
                ids = new int[]{
                        R.drawable.cards_school_backpack, R.drawable.cards_clothes_pants,
                        R.drawable.cards_clothes_shoes,
                        R.drawable.clock, R.drawable.cards_clothes_dress, R.drawable.glass,
                        R.drawable.cards_clothes_baseballcap, R.drawable.cards_clothes_jacket,
                        R.drawable.cards_clothes_tshirt, R.drawable.cards_clothes_belt
                };
                break;
            case 3:
                name = new String[]{"فرح", "بكاء", "غَضَبٌ", "حب", "رَفَضَ", "خوف", "اِنْدِهَاشٌ ", "مَرَضٌ", "ذكاء"

                };
                ids = new int[]{
                        R.drawable.cards_feelings_joyful,
                        R.drawable.cards_feelings_sad, R.drawable.cards_feelings_angry,
                        R.drawable.cards_feelings_inlove, R.drawable.cards_feelings_stressed,
                        R.drawable.cards_feelings_afraid, R.drawable.cards_feelings_shocked,
                        R.drawable.cards_feelings_sick, R.drawable.cards_feelings_smart
                };
                break;
            case 4:
                name = new String[]
                        {"أمي", "أبي", "جدي", "جدتي", "أخي", "أختي", "عَمَّتِي", "عَمِّي", "مُعَلِّمِي"
                                , "مُعَلِّمَتِي"
                        };
                ids = new int[]{
                        R.drawable.cards_people_mother,
                        R.drawable.cards_people_father, R.drawable.cards_people_grandfather,
                        R.drawable.cards_people_grandmother, R.drawable.cards_people_brother,
                        R.drawable.cards_people_sister, R.drawable.cards_people_aunt,
                        R.drawable.cards_people_uncle, R.drawable.cards_people_teacher,
                        R.drawable.cards_school_teacher
                };
                break;
            case 5:
                name = new String[]
                        {"أَحْمَرُ", "أَسْوَدُ", "أَزْرَقُ", "بُنِي", "رَمَادِيٌّ", "أَخْضَرُ", "بُرْتُقَالِيٌّ",
                                "زَهْرِيّ", "بَنَفْسَجِيٌّ", "أَبْيَضٌ"
                                , "أَصْفَرُ"
                        };
                ids = new int[]{
                        R.drawable.cards_colors_red,
                        R.drawable.cards_colors_black,
                        R.drawable.cards_colors_blue,
                        R.drawable.cards_colors_brown,
                        R.drawable.cards_colors_gray,
                        R.drawable.cards_colors_green, R.drawable.cards_colors_orange,
                        R.drawable.cards_colors_pink, R.drawable.cards_colors_purple,
                        R.drawable.cards_colors_white, R.drawable.cards_colors_yellow
                };
                break;
            case 6:
                name = new String[]
                        {"دَائِرَةٌ", "سَهْمٌ",
                                "هِلَالٌ", "مُكَعَّبٌ", "قَلْبٌ", "مُسْتَطِيلٌ", "سُدَاسِيٌّ",
                                "خُمَاسِيّ", "مُرَبَّعٌ", "نَجْمَةٌ"

                        };
                ids = new int[]{
                        R.drawable.cards_shapes_circle,
                        R.drawable.cards_shapes_arrow,
                        R.drawable.cards_shapes_crescent,
                        R.drawable.cards_shapes_cube,
                        R.drawable.cards_shapes_heart,
                        R.drawable.cards_shapes_rectangle,
                        R.drawable.cards_shapes_rhombus,
                        R.drawable.cards_shapes_pentagon,
                        R.drawable.cards_shapes_square,
                        R.drawable.cards_shapes_star
                };
                break;
            case 7:
                name = new String[]
                        {"كُرَةٌ سَلَّة", "شَطْرَنْجٌ",
                                "صَيْدٌ", "كُرَةٌ قَدَم", "حِيَاكَةٌ", "رَسْمٌ"
                                , "تَلْوِينٌ", "قِرَاءَةٌ",
                                "سِبَاقٌ", "سِبَاحَةٌ", "كِتَابَةٌ"

                        };
                ids = new int[]{
                        R.drawable.cards_activities_basketball,
                        R.drawable.cards_activities_chess,
                        R.drawable.cards_activities_fishing,
                        R.drawable.cards_activities_football,
                        R.drawable.cards_activities_knitting,
                        R.drawable.cards_activities_drawing,
                        R.drawable.cards_activities_painting,
                        R.drawable.cards_activities_reading,
                        R.drawable.cards_activities_running,
                        R.drawable.cards_activities_swimming,
                        R.drawable.cards_activities_texting
                };
                break;
            case 8:
                name = new String[]
                        {"طَائِرَة", "إِسْعَافٌ",
                                "عَجَلَةٌ", "قَارِبٌ", "أُتُوبِيسٌ ", "سَيَّارَةٌ"
                                , "مَطَافِئ", "هِلِيكُوبْتَر",
                                "مُنْطَادٌ", "تَاكْسِي", "سَفِينَةٌ", "صَارُوخٌ", "شُرْطَةٌ", "قِطَارٌ"

                        };
                ids = new int[]{
                        R.drawable.cards_transport_airplane,
                        R.drawable.cards_transport_ambulance,
                        R.drawable.cards_transport_bicycle,
                        R.drawable.cards_transport_boat,
                        R.drawable.cards_transport_bus,
                        R.drawable.cards_transport_car,
                        R.drawable.cards_transport_firetruck,
                        R.drawable.cards_transport_helicopter,
                        R.drawable.cards_transport_hotairballoon,
                        R.drawable.cards_transport_taxi,
                        R.drawable.cards_transport_ship,
                        R.drawable.cards_transport_spaceship,
                        R.drawable.cards_transport_policecar,
                        R.drawable.cards_transport_train,

                };
                break;
            case 9:
                name = new String[]
                        {"وَاحِد", "اِثْنَيْن",
                                "ثَلَاثَة", "أَرْبَعَة", "خَمْسَة", "سِتَّة"
                                , "سَبْعَة", "ثَمَانِيَة",
                                "تِسْعَة", "عَشَرَة", "آلَةٌ حَاسِبَة"

                        };
                ids = new int[]{
                        R.drawable.cards_numbers_one,
                        R.drawable.cards_numbers_two,
                        R.drawable.cards_numbers_three,
                        R.drawable.cards_numbers_four,
                        R.drawable.cards_numbers_five,
                        R.drawable.cards_numbers_six,
                        R.drawable.cards_numbers_seven,
                        R.drawable.cards_numbers_eight,
                        R.drawable.cards_numbers_nine,
                        R.drawable.cards_numbers_ten,
                        R.drawable.cards_numbers_calculator
                };
                break;
            default:
                name = new String[]{"سعيد", "حزين", "غضبان", "بِحُبٍّ", "أرفض", "نوم", "خوف", "تعجب"
                };
                ids = new int[]{
                        R.drawable.laugh, R.drawable.cry, R.drawable.angry,
                        R.drawable.love, R.drawable.notlike, R.drawable.sleep,
                        R.drawable.cards_people_uncle, R.drawable.cards_school_chair,
                };
                break;
        }
        int len = ids.length;
        Item[] myListData = new Item[len];
        for (int i = 0; i < len; i++) {
            myListData[i] = new Item(name[i], ids[i], position);
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewFood);
        Adapter adapter = new Adapter(myListData);
        recyclerView.setHasFixedSize(true);
        int col = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(this, col));
        recyclerView.setAdapter(adapter);
    }

}
