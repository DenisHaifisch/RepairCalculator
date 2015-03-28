package com.example.repaircalculator;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends ActionBarActivity {

    // переменные для хранения вычисленной площади
    Double areaFloor;
    Double areaWall;
    Double areaRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // связывание ссылок на кнопки с соответствующими элементами на экране устройства
        Button calculateButton = (Button) findViewById(R.id.button_calculate);

        // создание и инициализация ссылки на объект типа Интент для перехода с главного экрана на экран вывода
        final Intent intent = new Intent(this, OutActivity.class);

        // обработчик события - нажатие кнопки "Рассчитать"
        View.OnClickListener calculateButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // вызов метода вычисления
                calculate();

                // преобразование вычисленных значений в строки для передачи экрану вывода
                String areaFloorString = areaFloor.toString();
                String areaWallString = areaWall.toString();
                String areaRoomString = areaRoom.toString();

                // передача с помощью интента данных экрану вывода
                intent.putExtra("areaFloor", areaFloorString);
                intent.putExtra("areaWall", areaWallString);
                intent.putExtra("areaRoom", areaRoomString);

                // переход на экран вывода
                startActivity(intent);

            }
        };

        // связывание объекта и обработчика события
        calculateButton.setOnClickListener(calculateButtonListener);

    }

    // метод для вычисления
    private void calculate ()
    {
        // связывание ссылок на текстовые поля с соответствующими элементами на экране устройства
        EditText lengthText = (EditText) findViewById(R.id.textField_length);
        EditText widthText = (EditText) findViewById(R.id.textField_width);
        EditText heightText = (EditText) findViewById(R.id.textField_height);

        // последовательность символов, полученная из текстовых полей
        CharSequence lengthSequence = lengthText.getText();
        CharSequence widthSequence = widthText.getText();
        CharSequence heightSequence = heightText.getText();

        // строки, преобразованные из последовательности символов
        String lengthString = lengthSequence.toString();
        String widthString = widthSequence.toString();
        String heightString = heightSequence.toString();

        // числовые переменные, преобразованные из строк
        Double lengthNumber = Double.parseDouble(lengthString);
        Double widthNumber = Double.parseDouble(widthString);
        Double heightNumber = Double.parseDouble(heightString);

        // считаем общую площадь стен, площадь пола/потолка и площадь всей комнаты
        areaWall = 2 * (lengthNumber * heightNumber + widthNumber * heightNumber);
        areaFloor = lengthNumber * widthNumber;
        areaRoom = areaWall + 2 * areaFloor;

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
