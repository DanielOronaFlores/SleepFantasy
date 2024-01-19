package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;

import DataAccess.MonstersDataAccess;
import Database.DatabaseConnection;
import Dates.DateManager;

public class monsters extends AppCompatActivity {
    DatabaseConnection connection;
    MonstersDataAccess monstersDataAccess;
    private final DateManager dateManager = new DateManager();
    private final int[] monsters = { //TODO: Cambiar por los monstruos reales.
            R.drawable.avatar_default_1,
            R.drawable.avatar_default_2,
            R.drawable.avatar_default_3,
            R.drawable.fantasy,
            R.drawable.avatar_default_5
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monsters);

        connection = DatabaseConnection.getInstance(this);
        connection.openDatabase();
        monstersDataAccess = new MonstersDataAccess(connection);

        ImageView monster = findViewById(R.id.monsterImageView);
        TextView name = findViewById(R.id.monsterNameTextView);
        TextView description = findViewById(R.id.daysRemainingTextView);

        int idMonster = getMonsterID();
        if (idMonster != -1) {
            monster.setImageResource(monsters[idMonster - 1]);
            name.setText(getMonsterName(idMonster));
            description.setText(getDaysRemaining() + " días restantes");
        } else {
            name.setText("SIN MONSTRUO");
        }
    }

    private int getMonsterID(){
        return monstersDataAccess.getActiveMonster();
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
        int DEFAULT_DAYS_REMAINING = 0;
        try {
            String currentDay = dateManager.getCurrentDate();
            String lastDay = monstersDataAccess.getDateDisappearedActiveMonster();
            return (int) dateManager.getDaysDifference(currentDay, lastDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return DEFAULT_DAYS_REMAINING;
    }
}