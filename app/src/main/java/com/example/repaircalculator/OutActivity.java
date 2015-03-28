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
        TextView outAreaText = (TextView) findViewById(R.id.label_outArea);

        // формирование текста с информацией о площадях комнаты с данными, полученными от главного экрана
        outAreaText.setText("Общая площадь комнаты - " + intent.getStringExtra("areaRoom") + " кв. метров \n" +
                            "Площадь пола/потолка - " + intent.getStringExtra("areaFloor") + " кв. метров \n" +
                            "Площадь стен - " + intent.getStringExtra("areaWall") + " кв. метров");

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