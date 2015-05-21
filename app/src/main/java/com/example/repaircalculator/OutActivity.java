package com.example.repaircalculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class OutActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out);

        // создание ссылки на объект типа Интент, принимающего данные от главного экрана
        Intent intent = getIntent();

        // ссылки на объекты, в которые будет производиться вывод рассчитанной информации
        TextView outWallMaterials = (TextView) findViewById(R.id.label_outWall);
        TextView outCeilingMaterials = (TextView) findViewById(R.id.label_outCeiling);
        TextView outFloorMaterials = (TextView) findViewById(R.id.label_outFloor);

        // Формируем и выводим результаты вычислений только если полученная площадь пришла положительной
        if (intent.getBooleanExtra("positiveResult", false)) {

            // формирование текста с информацией о количестве рулонов обоев различной толщины, необходимом для оклейки стен
            outWallMaterials.setText("Для оклейки стен Вам понадобится " + intent.getStringExtra("wallNarrowWallpaper") +
                    " рулонов обоев шириной 0.53 метра. Или " + intent.getStringExtra("wallWideWallpaper") +
                    " рулонов обоев шириной 1.06 метра.");

            // формирование текста с информацией о количестве рулонов обоев различной толщины, необходимом для оклейки потолка
            outCeilingMaterials.setText("Для оклейки потолка Вам понадобится " + intent.getStringExtra("ceilingNarrowWallpaper") +
                    " рулонов обоев шириной 0.53 метра. Или " + intent.getStringExtra("ceilingWideWallpaper") +
                    " рулонов обоев шириной 1.06 метра.");

            // формирование текста с информацией о количестве материала, необходимом для покрытия пола
            outFloorMaterials.setText(String.format("Для покрытия пола Вам понадобится %.2f кв. метров материала.",
                                                     intent.getDoubleExtra("areaFloor", 0.0)));
        } else {

            // формирование сообщения о некорректности введенных данных
            outWallMaterials.setText("В комнате с введенными Вами размерами невозможно сделать ремонт. " +
                                     "Пожалуйста, проверьте корректность введенных данных");

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}