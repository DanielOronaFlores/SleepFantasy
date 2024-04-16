package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import Database.DataAccess.MonstersDataAccess;
import Database.DatabaseConnection;
import Dates.DateManager;
import Styles.Themes;

public class MonsterVisualizer extends AppCompatActivity {
    private final DatabaseConnection connection = DatabaseConnection.getInstance(this);
    private final MonstersDataAccess monstersDataAccess = new MonstersDataAccess(connection);;
    private final DateManager dateManager = new DateManager();
    private final int[] monsters = { //TODO: Cambiar por los monstruos reales.
            R.drawable.menu_gamification,
            R.drawable.menu_challenges,
            R.drawable.menu_missions,
            R.drawable.fantasy,
            R.drawable.medals_gold
    };

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monsters);

        connection.openDatabase();

        ImageView monster = findViewById(R.id.monsterImageView);
        TextView name = findViewById(R.id.monsterNameTextView);
        TextView description = findViewById(R.id.daysRemainingTextView);

        int idMonster = monstersDataAccess.getActiveMonster();
        if (idMonster != -1) {
            monster.setImageResource(monsters[idMonster - 1]);
            name.setText(getMonsterName(idMonster));
            description.setText(getDaysRemaining() + " días restantes");
        } else {
            name.setText("SIN MONSTRUO");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setTheme();
    }

    private String getMonsterName(int id){
        switch (id) {
            case 1:
                return "INSOMNIA";
            case 2:
                return "RUIDOS ALTOS";
            case 3:
                return "INTRANQUILIDAD";
            case 4:
                return "PESADILLAS";
            case 5:
                return "SONAMBULISMO";
            default:
                return "SIN MONSTRUO";
        }
    }

    private int getDaysRemaining() {
        String currentDay = dateManager.getCurrentDate();
        System.out.println("Current day: " + currentDay);
        String appearedDay = monstersDataAccess.getDateAppearedActiveMonster();
        System.out.println("Appeared day: " + appearedDay);
        String lastDay = dateManager.getDateNextDays(appearedDay, 3);
        System.out.println("Last day: " + lastDay);
        if (lastDay == null) return 0;
        return (int) dateManager.getDaysDifference(currentDay, lastDay);
    }

    private void setTheme() {
        Themes.setBackgroundColor(this, findViewById(R.id.monsters));

    }
}