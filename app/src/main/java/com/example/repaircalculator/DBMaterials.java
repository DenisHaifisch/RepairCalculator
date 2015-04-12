package com.example.repaircalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// класс для работы с базой данных материалов
public class DBMaterials extends SQLiteOpenHelper{

    public long result = 0;

    DBMaterials (Context context) {

        super(context, "MaterialsBase", null, 1);

    }

    // создание и заполнение базы данных материалов
    // |---------------|----------------------------|-----------------------------|----------------------|
    // | идентификатор |          название          | еденица измерения материала | цена за еденицу, руб |
    // |---------------|----------------------------|-----------------------------|----------------------|
    // |             1 |    обои шириной 1.06 метра |                       рулон |                  700 |
    // |             2 |    обои шириной 0.53 метра |                       рулон |                 1500 |
    // |             3 |                   линолеум |             квадратный метр |                  600 |
    // |             4 |                    ламинат |             квадратный метр |                  800 |
    // |---------------|----------------------------|-----------------------------|----------------------|
    @Override
    public void onCreate(SQLiteDatabase db) {

        // создание базы данных
        db.execSQL("CREATE TABLE Materials ( " +
                   "id integer primary key autoincrement, " +
                   "name text, " +
                   "measure text, " +
                   "cost integer );");

        // данные для базы
        ContentValues content1 = new ContentValues();
        content1.put("name", "обои шириной 1.06 метра");
        content1.put("measure", "рулон");
        content1.put("cost", 700);

        ContentValues content2 = new ContentValues();
        content2.put("name", "обои шириной 0.53 метра");
        content2.put("measure", "рулон");
        content2.put("cost", 1500);

        ContentValues content3 = new ContentValues();
        content3.put("name", "линолеум");
        content3.put("measure", "квадратный метр");
        content3.put("cost", 500);

        ContentValues content4 = new ContentValues();
        content4.put("name", "ламинат");
        content4.put("measure", "квадратный метр");
        content4.put("cost", 800);

        // заполнение базы данных
        SQLiteDatabase database = this.getWritableDatabase();
        result = database.insert("Materials", null, content1);
        result = database.insert("Materials", null, content2);
        result = database.insert("Materials", null, content3);
        result = database.insert("Materials", null, content4);

        database.close();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
