package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // поля
    private EditText input;
    private TextView output;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private int[] numbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // привязка разметки к полям
        input = findViewById(R.id.input);
        output = findViewById(R.id.output);

        // получение доступа к сенсорам
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // инициализация сенсора
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {


        @SuppressLint("SetTextI18n")
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

            Sensor multiSensor = sensorEvent.sensor;
            // действие при получении данных с акселерометра
            if (multiSensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                float xAccelerometer = sensorEvent.values[0]; // ускорение по оси X (поперечное направление)
                float yAccelerometer = sensorEvent.values[1]; // ускорение по оси Y (продольное направление)
                float zAccelerometer = sensorEvent.values[2]; // ускорение по оси Z (вертикальное направление)
                // определим среднее значение ускорения по всем осям
                float medianAccelerometer = (xAccelerometer + yAccelerometer + zAccelerometer) / 3;
                // условия определения порядка участников
                if (medianAccelerometer > 10) { // если телефон был в условиях ускорения, то
                    String inputText = input.getText().toString().trim(); // Получаем введенный текст и удаляем пробелы по краям

                    if (!inputText.isEmpty()) { // Проверяем, что введенный текст не пустой
                        try {
                            int number = Integer.parseInt(inputText); // Пробуем преобразовать введенный текст в число

                            if (number <= 0) { // Проверяем, что введенное число не равно или меньше 0
                                Toast.makeText(MainActivity.this, "Количество участников должно быть больше 0", Toast.LENGTH_SHORT).show();
                            } else {
                                output.setText("Участнику достаётся номер " + (new Random().nextInt(number) + 1)); // Выводим случайный номер участника
                            }
                        } catch (NumberFormatException n) {
                            Toast.makeText(MainActivity.this, "Вы ввели некорректное количество участников", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Вы не ввели количество участников", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }

        // метод задания точности сенсора
        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        // регистрация сенсоров (задание слушателя)
        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL); // (слушатель, сенсор - аксерометр, время обновления - среднее)
    }

    @Override
    protected void onPause() {
        super.onPause();
        // отзыв регистрации сенсоров (отключение слушателя)
        sensorManager.unregisterListener(sensorEventListener);
    }

    // метод генерации массива N случайных чисел
    private int[] valueArrayRandom(int number) {
        Random random = new Random();

        int[] arrayValue = new int[number]; // создание массива для заполнения
        for (int i = 0; i < arrayValue.length; i++) {
            boolean flag = true;

            while (flag) {
                arrayValue[i] = random.nextInt(number) + 1;
                flag = false; // сброс флага
                for (int k = 0; k < i; k++) {
                    if (arrayValue[i] == arrayValue[k]) {
                        flag = true;
                        break;
                    }
                }
            }
        }
        return arrayValue;
    }

    private int valueRandom(int number) {
        Random random = new Random();
        return random.nextInt(number) + 1;
    }
}