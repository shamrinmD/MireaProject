package ru.mirea.shamrin.mireaproject;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HardwareFragment extends Fragment implements SensorEventListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView temperatureValue;
    private TextView accelerometerSensorX;
    private TextView accelerometerSensorY;
    private TextView accelerometerSensorZ;
    private TextView gyroscopeSensorX;
    private TextView gyroscopeSensorY;
    private TextView gyroscopeSensorZ;
    private SensorManager sensorManager;
    private Sensor temperatureSensor;
    private Sensor accelerometerSensor;
    private Sensor gyroscopeSensor;

    private static final int REQUEST_CODE_PERMISSION_CAMERA = 100;
    final String TAG = MainActivity.class.getSimpleName();
    private ImageView imageView;
    private static final int CAMERA_REQUEST = 0;
    private boolean isWorkCamera = false;
    private Uri imageUri;

    private OutputStream outputStream;

    private static final int REQUEST_CODE_PERMISSION = 100;
    private ImageButton startRecordButton;
    private ImageButton stopRecordButton;
    private MediaRecorder mediaRecorder;
    private File audioFile;
    private String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
    };
    private boolean isWorkAudio;

    ImageButton play, pause, stop;
    MediaPlayer mediaPlayer;
    boolean playing = false;

    public HardwareFragment() {
        // Required empty public constructor
    }

    public static HardwareFragment newInstance(String param1, String param2) {
        HardwareFragment fragment = new HardwareFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hardware, container, false);
        temperatureValue = view.findViewById(R.id.temperature);
        accelerometerSensorX = view.findViewById(R.id.accX);
        accelerometerSensorY = view.findViewById(R.id.accY);
        accelerometerSensorZ = view.findViewById(R.id.accZ);
        gyroscopeSensorX = view.findViewById(R.id.gyroX);
        gyroscopeSensorY = view.findViewById(R.id.gyroY);
        gyroscopeSensorZ = view.findViewById(R.id.gyroZ);

        imageView = view.findViewById(R.id.imageView);

        startRecordButton = view.findViewById(R.id.btnStart);
        stopRecordButton = view.findViewById(R.id.btnStop);
        mediaRecorder = new MediaRecorder();
        play = view.findViewById(R.id.btnPlay);
        pause = view.findViewById(R.id.btnPause);
        stop = view.findViewById(R.id.btnStopPlayer);
        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.music);

        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        view.findViewById(R.id.btnCamera).setOnClickListener(this::takePhoto);
        view.findViewById(R.id.btnSave).setOnClickListener(this::saveImage);
        startRecordButton.setOnClickListener(this::onClickStartRecord);
        stopRecordButton.setOnClickListener(this::onClickStopRecord);
        play.setOnClickListener(this::onClickPlay);
        pause.setOnClickListener(this::onClickPause);
        stop.setOnClickListener(this::onClickStopPlayer);

        int cameraPermissionStatus = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
        int storagePermissionStatus = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        // Проверка наличия разрешений на использование камеры и запись в память
        if (cameraPermissionStatus == PackageManager.PERMISSION_GRANTED && storagePermissionStatus == PackageManager.PERMISSION_GRANTED) {
            isWorkCamera = true;
        } else {
            // Запрос к пользователь на получение необходимых разрешений
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSION_CAMERA);
        }

        // Проверка наличия разрешений на выполнение аудиозаписи и сохранения на карту памяти
        isWorkAudio = hasPermissions(getActivity(), PERMISSIONS);
        if (!isWorkAudio) {
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, REQUEST_CODE_PERMISSION);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometerSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, gyroscopeSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, temperatureSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener( this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE){
            float tempValue = sensorEvent.values[0];
            temperatureValue.setText(tempValue + " C");
        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            float accValueX = sensorEvent.values[0];
            float accValueY = sensorEvent.values[1];
            float accValueZ = sensorEvent.values[2];
            accelerometerSensorX.setText("OX " + accValueX +" м/с2");
            accelerometerSensorY.setText("OY " + accValueY +" м/с2");
            accelerometerSensorZ.setText("OZ " + accValueZ +" м/с2");

        } else if(sensorEvent.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            float gyroValueX = sensorEvent.values[0];
            float gyroValueY = sensorEvent.values[1];
            float gyroValueZ = sensorEvent.values[2];
            gyroscopeSensorX.setText("OX " + gyroValueX + " рад/с");
            gyroscopeSensorY.setText("OY " + gyroValueY + " рад/с");
            gyroscopeSensorZ.setText("OZ " + gyroValueZ + " рад/с");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == getActivity().RESULT_OK) {
            imageView.setImageURI(imageUri);
        }
    }

    public void takePhoto(View view) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null && isWorkCamera)
        {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String authorities = getActivity().getApplicationContext().getPackageName() + ".fileprovider";
            imageUri = FileProvider.getUriForFile(getActivity(), authorities, photoFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + "_";
        File storageDirectory =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDirectory);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission granted
                isWorkCamera = true;
            } else {
                isWorkCamera = false;
            }
        }
        if (requestCode == REQUEST_CODE_PERMISSION) {
            // permission granted
            isWorkAudio = grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
    }

    // Сохранение изображения на карту памяти
    public void saveImage(View view) {
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        File filepath = Environment.getExternalStorageDirectory();
        File directory = new File(filepath.getAbsoluteFile() + "/Demo/");
        directory.mkdir();
        File file = new File(directory, System.currentTimeMillis() + ".jpg");
        try {
            outputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        Toast.makeText(getActivity().getApplicationContext(), "Картинка успешно сохранена", Toast.LENGTH_LONG).show();
        try {
            outputStream.flush();
        }catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    // Кнопка начала записи
    public void onClickStartRecord(View view) {
        try {
            startRecordButton.setEnabled(false);
            stopRecordButton.setEnabled(true);
            stopRecordButton.requestFocus();
            startRecording();
        } catch (Exception e) {
            Log.e(TAG, "Caught io exception " + e.getMessage());
        }
    }
    // Кнопка остановки записи
    public void onClickStopRecord(View view) {
        startRecordButton.setEnabled(true);
        stopRecordButton.setEnabled(false);
        startRecordButton.requestFocus();
        stopRecording();
        processAudioFile();
    }

    private void startRecording() throws IOException {
        // Проверка доступности sd - карты
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            Log.d(TAG, "sd-card success");
            // Выбор источника звука
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            // Выбор формата данных
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            // Выбор кодека
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            if (audioFile == null) {
                // Создание файла
                audioFile = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_MUSIC), "mirea.3gp");
            }
            mediaRecorder.setOutputFile(audioFile.getAbsolutePath());
            mediaRecorder.prepare();
            mediaRecorder.start();
            Toast.makeText(getActivity(), "Запись включена", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopRecording() {
        if (mediaRecorder != null) {
            Log.d(TAG, "stopRecording");
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
            Toast.makeText(getActivity(), "Запись остановилась", Toast.LENGTH_SHORT).show();
        }
    }
    private void processAudioFile() {
        Log.d(TAG, "processAudioFile");
        ContentValues values = new ContentValues(4);
        long current = System.currentTimeMillis();
        // установка meta данных созданному файлу
        values.put(MediaStore.Audio.Media.TITLE, "audio" + audioFile.getName());
        values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (current / 1000));
        values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/3gpp");
        values.put(MediaStore.Audio.Media.DATA, audioFile.getAbsolutePath());
        ContentResolver contentResolver = getActivity().getContentResolver();
        Log.d(TAG, "audioFile: " + audioFile.canRead());
        Uri baseUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri newUri = contentResolver.insert(baseUri, values);
        // оповещение системы о новом файле
        getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
    }
    // Кнопка воспроизведения записи
    private void onClickPlay(View view) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(String.valueOf(audioFile));
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        playing = true;
        Toast.makeText(getActivity(), "Воспроизведение", Toast.LENGTH_SHORT).show();
    }
    // Кнопка паузы воспросизведения записи
    private void onClickPause(View view) {
        if (playing == true) {
            mediaPlayer.pause();
            playing = false;
        }
        Toast.makeText(getActivity(), "Пауза", Toast.LENGTH_SHORT).show();
    }
    // Кнопка остановки воспроизведения записи
    private void onClickStopPlayer(View view) {
        if (playing == true) {
            mediaPlayer.stop();
            playing = false;
        }
        Toast.makeText(getActivity(), "Стоп", Toast.LENGTH_SHORT).show();
    }

}