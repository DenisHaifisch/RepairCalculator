package com.example.repaircalculator;

import java.lang.Math;

// Класс для округления переменных типа Double и Float
public class RoundDecimal {

        // Метод для округления переменных типа Double.
        // Входные параметры - переменная типа Double для округления и переменная типа int с необходимым количеством знаков после запятой
        public static double roundDouble (double value, int digit) {

            digit = 10 ^ digit;
            value = value * digit;
            int i = (int) Math.round(value);
            return ((double)i / (double)digit);

        }

        // Метод для округления переменных типа Float.
        // Входные параметры - переменная типа Float для округления и переменная типа int с необходимым количеством знаков после запятой
        public static double roundFloat (float value, int digit) {

            digit = 10 ^ digit;
            value = value * digit;
            int i = Math.round(value);
            return (float) i / digit;

        }

    }

