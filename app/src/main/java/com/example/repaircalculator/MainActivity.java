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

    // переменные для хранения вычисленной площади пола/потолка, площади проемов, площади стен без проемов
    private Double areaFloor = 0.0;
    private Double areaWindows = 0.0;
    private Double areaWallWithoutWindows = 0.0;

    // площадь 1 рулона обоев двух стандартных размеров - узкие 10.05х0.53 метров и широкие 10.05х1.06 метров
    private final static Double NARROW_WALLPAPER = 5.3265;
    private final static Double WIDE_WALLPAPER = 10.653;

    // переменные для хранения количества рулонов обоев различной ширины для стен и потолка
    private Double wallNarrowWallpaper = 0.0;
    private Double wallWideWallpaper = 0.0;
    private Double ceilingNarrowWallpaper = 0.0;
    private Double ceilingWideWallpaper = 0.0;

    // счетчик вызовов метода windows
    private Integer windowsCounter = -1;

    // массивы для хранения проемов и их размеров
    // массив для хранения проемов имеет вид:
    //     #         0               1               2
    //     1   ДлинаПроема1   ШиринаПроема1   ПлощадьПроема1
    //     2   ДлинаПроема2   ШиринаПроема2   ПлощадьПроема2
    //     3   ДлинаПроема3   ШиринаПроема3   ПлощадьПроема3
    //     ...
    private ArrayList<Double> windowSizes = new ArrayList<>();
    private ArrayList<ArrayList<Double>> windows = new ArrayList<>();

    // строковый массив для хранения списка проемов и вывода его на экран
    private ArrayList<String> windowsText = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // объявление ссылок на объект кнопки и связывание их с соответствующими элементами на экране устройства
        Button calculateButton = (Button) findViewById(R.id.button_calculate);
        Button addWindowButton = (Button) findViewById(R.id.button_addWindow);
        Button clearWindowsListButton = (Button) findViewById(R.id.button_clearWindowsList);

        // объявление ссылки на объект текстового поля со списком проемов и связывание ее с соответствующим элементом на экране устройства
        final TextView textViewWindows = (TextView) findViewById(R.id.label_windows);

        // создание ссылки на объект типа Интент для перехода с главного экрана на экран вывода
        final Intent intent = new Intent(this, OutActivity.class);

        // обработчик события - нажатие кнопки "Рассчитать"
        View.OnClickListener calculateButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // вызов метода вычисления, если вычисления прошли удачно
                if (calculate()) {

                    // проверка, что не получились нулевые или отрицательные площади стен или пола/потолка
                    if ((areaFloor <= 0) || (areaWallWithoutWindows <= 0)) {

                        intent.putExtra("positiveResult", false);

                        // переход на экран вывода
                        startActivity(intent);

                    } else {

                        // преобразование вычисленных значений в строки для передачи экрану вывода
                        String areaFloorString = areaFloor.toString();
                        String wallNarrowWallpaperString = wallNarrowWallpaper.toString();
                        String wallWideWallpaperString = wallWideWallpaper.toString();
                        String ceilingNarrowWallpaperString = ceilingNarrowWallpaper.toString();
                        String ceilingWideWallpaperString = ceilingWideWallpaper.toString();

                        // передача с помощью Интента данных экрану вывода
                        intent.putExtra("positiveResult", true);
                        intent.putExtra("areaFloor", areaFloorString);
                        intent.putExtra("wallNarrowWallpaper", wallNarrowWallpaperString);
                        intent.putExtra("wallWideWallpaper", wallWideWallpaperString);
                        intent.putExtra("ceilingNarrowWallpaper", ceilingNarrowWallpaperString);
                        intent.putExtra("ceilingWideWallpaper", ceilingWideWallpaperString);

                        // переход на экран вывода
                        startActivity(intent);
                    }
                }

            }
        };

        // обработчик события - нажатие кнопки "Добавить проем"
        View.OnClickListener addWindowButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // вызов метода вычисления площади проема и вывода списка проемов на экран
                calculateWindow();

            }
        };

        // обработчик события - нажатие кнопки "Очистить список проемов"
        View.OnClickListener clearWindowsListButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // устанавливаем соответствующее сообщение и минимальный размер текстового поля для вывода списка проемов
                textViewWindows.setHeight(50);
                textViewWindows.setText(R.string.list_is_empty);

                // очищаем массив для хранения списка проемов, сбрасываем счетчик вызовов функции calculateWindow
                // и обнуляем переменную с суммарной площадью проемов
                windowsText.clear();
                windowsCounter = -1;
                areaWindows = 0.0;

            }
        };

        // связывание объекта и обработчика события
        calculateButton.setOnClickListener(calculateButtonListener);
        addWindowButton.setOnClickListener(addWindowButtonListener);
        clearWindowsListButton.setOnClickListener(clearWindowsListButtonListener);

    }

    // основной метод для вычисления площади пола/потолка комнаты, общей площади стен, площади стен без проемов,
    // необходимого количества рулонов обоев для стен и потолка
    private boolean calculate () {

        // объявление ссылок на текстовые поля для ввода размеров комнаты, и связывание их с соответствующими элементами на экране устройства
        EditText roomLengthText = (EditText) findViewById(R.id.textField_roomLength);
        EditText roomWidthText = (EditText) findViewById(R.id.textField_roomWidth);
        EditText roomHeightText = (EditText) findViewById(R.id.textField_roomHeight);

        // текстовое поле для сообщений об ошибках
        TextView errorText = (TextView) findViewById(R.id.label_error);

        // последовательность символов, полученная из текстовых полей
        CharSequence roomLengthSequence = roomLengthText.getText();
        CharSequence roomWidthSequence = roomWidthText.getText();
        CharSequence roomHeightSequence = roomHeightText.getText();

        // строки, преобразованные из последовательности символов
        String roomLengthString = roomLengthSequence.toString();
        String roomWidthString = roomWidthSequence.toString();
        String roomHeightString = roomHeightSequence.toString();

        // проверка, что поля для ввода не пустые
        // если поле для ввода какого-либо размера комнаты пустое, выводим соответствующее сообщение в поле для ошибок
        if (roomLengthString.equals("")) {

            errorText.setText(R.string.enter_length);
            errorText.setHeight(50);
            return false;

        } else if (roomWidthString.equals("")) {

            errorText.setText(R.string.enter_width);
            errorText.setHeight(50);
            return false;

        } else if (roomHeightString.equals("")) {

            errorText.setText(R.string.enter_height);
            errorText.setHeight(50);
            return false;

        } else {

            // очищаем поле для вывода сообщения об ошибке
            errorText.setText("");
            errorText.setHeight(0);

            // числовые переменные, преобразованные из строк
            Double roomLength = Double.parseDouble(roomLengthString);
            Double roomWidth = Double.parseDouble(roomWidthString);
            Double roomHeight = Double.parseDouble(roomHeightString);

            // считаем площадь пола/потолка, общую площадь стен и площадь стен без проемов
            areaFloor = roomLength * roomWidth;
            Double areaWall = 2 * (roomLength * roomHeight + roomWidth * roomHeight);
            areaWallWithoutWindows = areaWall - areaWindows;

            // считаем необходимое количество рулонов обоев различной ширины для стен и потолка
            wallNarrowWallpaper = areaWallWithoutWindows / NARROW_WALLPAPER;
            wallWideWallpaper = areaWallWithoutWindows / WIDE_WALLPAPER;
            ceilingNarrowWallpaper = areaFloor / NARROW_WALLPAPER;
            ceilingWideWallpaper = areaFloor / WIDE_WALLPAPER;

            // округляем необходимое количество рулонов обоев до одного знака после запятой
            wallNarrowWallpaper = RoundDecimal.roundDouble (wallNarrowWallpaper, 2);
            wallWideWallpaper = RoundDecimal.roundDouble (wallWideWallpaper, 2);
            ceilingNarrowWallpaper = RoundDecimal.roundDouble (ceilingNarrowWallpaper, 2);
            ceilingWideWallpaper = RoundDecimal.roundDouble (ceilingWideWallpaper, 2);

            return true;

        }

    }

    // метод для вычисления площади проемов и вывода списка проемов на экран
    private void calculateWindow () {

        // объявление ссылок на текстовые поля для ввода размеров проемов, и связывание их с соответствующими элементами на экране устройства
        EditText windowLengthText = (EditText) findViewById(R.id.textField_windowLenfth);
        EditText windowWidthText = (EditText) findViewById(R.id.textField_windowWidth);

        // объявление ссылки на объект текстового поля со списком проемов и связывание ее с соответствующим элементом на экране устройства
        TextView textViewWindows = (TextView) findViewById(R.id.label_windows);

        // последовательность символов, полученная из текстовых полей
        CharSequence windowLengthSequence = windowLengthText.getText();
        CharSequence windowWidthSequence = windowWidthText.getText();

        // строки, преобразованные из последовательности символов
        String windowLengthString = windowLengthSequence.toString();
        String windowWidthString = windowWidthSequence.toString();

        // проверка, что поля для ввода не пустые
        if ( !(windowLengthString.isEmpty()) && !(windowWidthString.isEmpty())) {

            // наращивание счетчика вызовов метода
            windowsCounter++;

            // увеличение счетчика вызовов метода на единицу, чтобы список проемов начинался с первого элемента, а не с нулевого
            Integer windowsCounterPlusOne = windowsCounter + 1;

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

            // добавление в текстовый массив размеров проемов строки вида "Проем # - ДлинаПроема х ШиринаПроема"
            windowsText.add("Проем " + windowsCounterPlusOne + " - " + windowLengthString + "x" + windowWidthString + "\n");

            // установка количества линий и размера текстового поля для вывода списка проемов
            // для вывода каждого проема требуется по 2 линии и по 50 пикселей
            textViewWindows.setLines(windowsCounterPlusOne * 2);
            textViewWindows.setHeight(windowsCounterPlusOne * 50);

            // Строковая переменная для вывода списка проемов на экран
            String windows = "";

            // Формирование строки со всеми проемами. Каждый проем на новой строке
            for (int i = 0; i < windowsText.size(); i++) {
                windows += windowsText.get(i) + "\n";
            }

            // вывод списка проемов на экран
            textViewWindows.setText(windows);

            // Очистка полей для ввода размеров проемов
            windowLengthText.setText("");
            windowWidthText.setText("");
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
