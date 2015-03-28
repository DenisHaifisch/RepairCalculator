package com.example.repaircalculator;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    // переменные для хранения вычисленной площади пола/потолка, стен, комнаты, проемов
    private Double areaFloor = 0.0;
    private Double areaWall = 0.0;
    private Double areaRoom = 0.0;
    private Double areaWindows = 0.0;

    // счетчик вызовов метода windows
    private Integer windowsCounter = 0;

    // переменная для отображения проемов, начиная с первого
    private Integer windowsCounterPlusOne = 0;

    // массивы для хранения проемов и их размеров
    // массив для хранения проемов имеет вид:
    //     #         0               1               2
    //     1   ДлинаПроема1   ШиринаПроема1   ПлощадьПроема1
    //     2   ДлинаПроема2   ШиринаПроема2   ПлощадьПроема2
    //     3   ДлинаПроема3   ШиринаПроема3   ПлощадьПроема3
    //     ...
    private ArrayList<Double> windowSizes = new ArrayList<>();
    private ArrayList<ArrayList<Double>> windows = new ArrayList<>();

    // строковый массив для хранения размеров окон и вывода их на экран
    private ArrayList<String> windowsText = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // объявление ссылкок на объект кнопки и связывание ее с соответствующими элементами на экране устройства
        Button calculateButton = (Button) findViewById(R.id.button_calculate);
        Button addWindowButton = (Button) findViewById(R.id.button_addWindow);

        // создание ссылки на объект типа Интент для перехода с главного экрана на экран вывода
        final Intent intent = new Intent(this, OutActivity.class);

        // обработчик события - нажатие кнопки "Рассчитать"
        View.OnClickListener calculateButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // вызов метода вычисления
                calculateArea();

                // преобразование вычисленных значений в строки для передачи экрану вывода
                String areaFloorString = areaFloor.toString();
                String areaWallString = areaWall.toString();
                String areaRoomString = areaRoom.toString();

                // передача с помощью Интента данных экрану вывода
                intent.putExtra("areaFloor", areaFloorString);
                intent.putExtra("areaWall", areaWallString);
                intent.putExtra("areaRoom", areaRoomString);

                // переход на экран вывода
                startActivity(intent);

            }
        };

        // обработчик события - нажатие кнопки "Добавить проем"
        View.OnClickListener addWindowButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // вызов метода вычисления площади проема и вывода его на экран
                calculateWindow();

            }
        };

        // связывание объекта и обработчика события
        calculateButton.setOnClickListener(calculateButtonListener);
        addWindowButton.setOnClickListener(addWindowButtonListener);

    }

    // метод для вычисления площади комнаты
    private void calculateArea () {
        // объявление ссылок на текстовые поля для ввода размеров комнаты, и связывание их с соответствующими элементами на экране устройства
        EditText roomLengthText = (EditText) findViewById(R.id.textField_roomLength);
        EditText roomWidthText = (EditText) findViewById(R.id.textField_roomWidth);
        EditText roomHeightText = (EditText) findViewById(R.id.textField_roomHeight);

        // последовательность символов, полученная из текстовых полей
        CharSequence roomLengthSequence = roomLengthText.getText();
        CharSequence roomWidthSequence = roomWidthText.getText();
        CharSequence roomHeightSequence = roomHeightText.getText();

        // строки, преобразованные из последовательности символов
        String roomLengthString = roomLengthSequence.toString();
        String roomWidthString = roomWidthSequence.toString();
        String roomHeightString = roomHeightSequence.toString();

        // проверка, что поля для ввода не пустые
        if (roomLengthString.equals("") || roomWidthString.equals("") || roomHeightString.equals("")) {

            // добавить действия при отсутсвии данных
        } else {

            // числовые переменные, преобразованные из строк
            Double roomLength = Double.parseDouble(roomLengthString);
            Double roomWidth = Double.parseDouble(roomWidthString);
            Double roomHeight = Double.parseDouble(roomHeightString);

            // считаем общую площадь стен, площадь пола/потолка и площадь всей комнаты
            areaWall = 2 * (roomLength * roomHeight + roomWidth * roomHeight);
            areaFloor = roomLength * roomWidth;
            areaRoom = areaWall + 2 * areaFloor;
        }

    }

    // метод для вычисления площади проемов и вывода их на экран
    private void calculateWindow () {

        // объявление ссылок на текстовые поля для ввода размеров проемов, и связывание их с соответствующими элементами на экране устройства
        EditText windowLengthText = (EditText) findViewById(R.id.textField_windowLenfth);
        EditText windowWidthText = (EditText) findViewById(R.id.textField_windowWidth);

        // объявление ссылки на объект текстового поля с проемами и связывание ее с соответствующим элементом на экране устройства
        TextView textViewWindows = (TextView) findViewById(R.id.label_windows);

        // последовательность символов, полученная из текстовых полей
        CharSequence windowLengthSequence = windowLengthText.getText();
        CharSequence windowWidthSequence = windowWidthText.getText();

        // строки, преобразованные из последовательности символов
        String windowLengthString = windowLengthSequence.toString();
        String windowWidthString = windowWidthSequence.toString();

        // проверка, что поля для ввода не пустые
        if (windowLengthString.equals("") || windowWidthString.equals("")) {

            // добавить действия при отсутсвии данных
        } else {

            // числовые переменные, преобразованные из строк
            Double windowLength = Double.parseDouble(windowLengthString);
            Double windowWidth = Double.parseDouble(windowWidthString);

            // считаем площадь проема
            Double windowArea = windowLength * windowWidth;

            // прибавление площади вышерасчитанного проема к общей площади проемов
            areaWindows = areaWindows + windowArea;

            // добавление длины, ширины и площади проема в массив размеров проемов
            windowSizes.add(0, windowLength);
            windowSizes.add(1, windowWidth);
            windowSizes.add(2, windowArea);

            // добавление массива размеров проемов в массив проемов
            windows.add(windowsCounter, windowSizes);

            // чтобы список проемов начинался с первого
            windowsCounterPlusOne = windowsCounter + 1;

            // установка количества линий и размера текстового поля для вывода списка проемов
            textViewWindows.setLines(windowsCounterPlusOne);

            // добавление в текстовый массив размеров проемов строки вида "Проем # ДлинаПроема х ШиринаПроема"
            windowsText.add(windowsCounter, "Проем " + windowsCounterPlusOne + " " + windowLength + "x" + windowWidth + "\n");

            // Строковая переменная для вывода проемов на экран
            String windows = "";

            // Формирование строки со всеми проемами. Каждый проем на новой строке
            for (int i = 0; i < windowsText.size(); i++)
            {
                windows += windowsText.get(i) + "\n";
            }

            // вывод проемов на экран
            textViewWindows.setText(windows);

            // Очистка полей для ввода размеров проемов
            windowLengthText.setText("");
            windowWidthText.setText("");

            // наращивание счетчика вызовов метода
            windowsCounter++;

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
