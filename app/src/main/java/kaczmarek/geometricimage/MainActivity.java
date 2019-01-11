package kaczmarek.geometricimage;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    private DrawView mDrawView;
    private BottomNavigationView mBottomNavigationView;
    private float dx = 0,dy = 0, mScaleFactor = 1.0f;
    private ScaleGestureDetector mScaleGestureDetector;
    private BottomSheetBehavior mSettingsBottomSheetBehavior, mSaveBottomSheetBehavior,
            mColorsBottomSheetBehavior;
    private RelativeLayout rlColorsSheet, rlSettingsSheet, rlSaveSheet;
    private FrameLayout pallete;
    private CoordinatorLayout mainLayout;
    private SeekBar altPiSeekBar, countDetailsSeekBar, strokeWidthSeekBar,
            redColor, greenColor, blueColor;
    private Button btnColorLine,btnColorBackground,btnSelectColor, btnSaveToPng,
            btnSavetoJpg80, btnSavetoJpg90,btnSavetoJpg100;
    private int stateColorPallete = 0,stateFirstSelectLineColor = 0,
            stateFirstSelectBackgroundColor = 0, valueSaveRedBackgroundColor,
            valueSaveRedLineColor, valueSaveGreenBackgroundColor, valueSaveGreenLineColor,
            valueSaveBlueBackgroundColor, valueSaveBlueLineColor;
    private String btnsavemode=null;
    private File imageFile = null;
    private static final int NUMBER_OF_REQUEST = 23401;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawView = findViewById(R.id.drawview);
        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        altPiSeekBar = findViewById(R.id.altPi);
        countDetailsSeekBar = findViewById(R.id.countDetails);
        strokeWidthSeekBar = findViewById(R.id.strokeWidth);
        redColor = findViewById(R.id.colorR);
        greenColor = findViewById(R.id.colorG);
        blueColor = findViewById(R.id.colorB);

        btnColorLine = findViewById(R.id.colorLine);
        btnColorBackground = findViewById(R.id.colorBackground);
        btnSelectColor = findViewById(R.id.selectColor);
        btnSaveToPng = findViewById(R.id.savePNG);
        btnSavetoJpg80 = findViewById(R.id.saveJPG80);
        btnSavetoJpg90 = findViewById(R.id.saveJPG90);
        btnSavetoJpg100 = findViewById(R.id.saveJPG100);

        pallete = findViewById(R.id.pallete);
        mainLayout = findViewById(R.id.mainLayout);

        rlSettingsSheet = findViewById(R.id.settings_bottom_sheet);
        mSettingsBottomSheetBehavior = BottomSheetBehavior.from(rlSettingsSheet);
        rlColorsSheet = findViewById(R.id.colors_bottom_sheet);
        mColorsBottomSheetBehavior = BottomSheetBehavior.from(rlColorsSheet);
        rlSaveSheet = findViewById(R.id.save_bottom_sheet);
        mSaveBottomSheetBehavior = BottomSheetBehavior.from(rlSaveSheet);

        mBottomNavigationView = findViewById(R.id.bottom_navigation);

        //Поведение нижних вьюх
        mBottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_colors:
                                mBottomNavigationView.getMenu().getItem(0).setChecked(true);
                                if (mSettingsBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
                                    mSettingsBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                }
                                if (mSaveBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
                                    mSaveBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                }
                                if (mColorsBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                                    mColorsBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                } else {
                                    mColorsBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                }
                                break;
                            case R.id.action_settings:
                                mBottomNavigationView.getMenu().getItem(1).setChecked(true);
                                if (mColorsBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
                                    mColorsBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                }
                                if (mSaveBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
                                    mSaveBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                }
                                if (mSettingsBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                                    mSettingsBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                                } else {
                                    mSettingsBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                }
                                break;
                            case R.id.action_save:
                                mBottomNavigationView.getMenu().getItem(2).setChecked(true);
                                if (mSettingsBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
                                    mSettingsBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                }
                                if (mColorsBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
                                    mColorsBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                }
                                if (mSaveBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                                    mSaveBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                                } else {
                                    mSaveBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                }
                                break;
                        }
                        return false;
                    }
                });
        // Обработчик для всех кнопок
        View.OnClickListener mOnClickListner = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.colorLine:
                        stateColorPallete=0;
                        btnColorBackground.setTextColor(getResources().getColor(R.color.white));
                        btnColorLine.setTextColor(getResources().getColor(R.color.colorAccent));
                        if(stateFirstSelectLineColor == 0)
                            firstOpenPanelLineColor();
                        else
                            setLineColorPallete();
                        break;
                    case R.id.colorBackground:
                        stateColorPallete = 1;
                        btnColorLine.setTextColor(getResources().getColor(R.color.white));
                        btnColorBackground.setTextColor(getResources().getColor(R.color.colorAccent));
                        if (stateFirstSelectBackgroundColor == 0)
                            firstOpenPanelBackgroundColor();
                        else
                            setBackgroundColorPallete();
                        break;
                    case R.id.selectColor:
                        if(stateColorPallete == 0) {
                            mDrawView.updateLineColor(redColor.getProgress(), greenColor.getProgress(), blueColor.getProgress());
                            getLineColorPallete();
                            stateFirstSelectLineColor = 1;

                        } else{
                            mDrawView.updateBackgroundColor(redColor.getProgress(),greenColor.getProgress(),blueColor.getProgress());
                            mainLayout.setBackgroundColor(Color.rgb(redColor.getProgress(), greenColor.getProgress(), blueColor.getProgress()));
                            getBackgroundColorPallete();
                            stateFirstSelectBackgroundColor =1;
                        }
                        break;
                    case R.id.savePNG:
                        permissionCheck("png");
                        break;
                    case R.id.saveJPG80:
                        permissionCheck("jpg80");
                        break;
                    case R.id.saveJPG90:
                        permissionCheck("jpg90");
                        break;
                    case R.id.saveJPG100:
                        permissionCheck("jpg100");
                        break;
                }
            }
        };
        btnColorLine.setOnClickListener(mOnClickListner);
        btnColorBackground.setOnClickListener(mOnClickListner);
        btnSelectColor.setOnClickListener(mOnClickListner);
        btnSaveToPng.setOnClickListener(mOnClickListner);
        btnSavetoJpg80.setOnClickListener(mOnClickListner);
        btnSavetoJpg90.setOnClickListener(mOnClickListner);
        btnSavetoJpg100.setOnClickListener(mOnClickListner);

        //Обработчик для всех ползунков
        SeekBar.OnSeekBarChangeListener mSettingsChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(seekBar.getId() == R.id.altPi || seekBar.getId() == R.id.strokeWidth
                        || seekBar.getId() == R.id.countDetails)
                    updateProgress();
                if(seekBar.getId() == R.id.colorR || seekBar.getId() == R.id.colorG
                        || seekBar.getId() == R.id.colorB)
                    refreshPallete();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };
        altPiSeekBar.setOnSeekBarChangeListener(mSettingsChangeListener);
        strokeWidthSeekBar.setOnSeekBarChangeListener(mSettingsChangeListener);
        countDetailsSeekBar.setOnSeekBarChangeListener(mSettingsChangeListener);
        redColor.setOnSeekBarChangeListener(mSettingsChangeListener);
        blueColor.setOnSeekBarChangeListener(mSettingsChangeListener);
        greenColor.setOnSeekBarChangeListener(mSettingsChangeListener);

        updateProgress();
    }
    //Проверка разрешения на запись в память смартфона
    private void permissionCheck(String mode){
        if (mSaveBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
        {
            mSaveBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            this.btnsavemode = mode;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int canRead = ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE);
                int canWrite = ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (canRead != PackageManager.PERMISSION_GRANTED ||
                        canWrite != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE}, NUMBER_OF_REQUEST);
                } else {
                    saveImage(btnsavemode);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case NUMBER_OF_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    saveImage(btnsavemode);
                else
                    Toast.makeText(getApplicationContext(),
                            R.string.permission_error, Toast.LENGTH_LONG).show();

                return;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        mScaleGestureDetector.onTouchEvent(motionEvent);
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dx = mDrawView.getX() - motionEvent.getRawX();
                dy = mDrawView.getY() - motionEvent.getRawY();

                break;

            case MotionEvent.ACTION_MOVE:
                mDrawView.animate()
                        .x(motionEvent.getRawX() + dx)
                        .y(motionEvent.getRawY() + dy)
                        .setDuration(0)
                        .start();
                break;
        }
        return true;
    }
    //Обновляем холст
    private void updateProgress(){
        int firstProgress = altPiSeekBar.getProgress()*2;
        int secondProgress = countDetailsSeekBar.getProgress()*10;
        int thirdProgress = strokeWidthSeekBar.getProgress();
        mDrawView.updateCanvas(firstProgress,secondProgress,thirdProgress);
    }
    //Устанавливаем первоначальный цвет палитры для фона
    private void firstOpenPanelBackgroundColor(){
        redColor.setProgress(24);
        greenColor.setProgress(25);
        blueColor.setProgress(30);
        refreshPallete();
    }
    //Устанавливаем первоначальный цвет палитры для линий
    private void firstOpenPanelLineColor(){
        redColor.setProgress(255);
        greenColor.setProgress(0);
        blueColor.setProgress(106);
        refreshPallete();
    }
    //Обновляем слой палитру
    private void refreshPallete() {
        int redValue, greenValue, blueValue;
        redValue = redColor.getProgress();
        greenValue = greenColor.getProgress();
        blueValue = blueColor.getProgress();
        pallete.setBackgroundColor(0xff000000 + redValue * 0x10000 + greenValue * 0x100
                + blueValue);
    }
    //Сохраняем цвет фона
    private void getBackgroundColorPallete() {
        this.valueSaveRedBackgroundColor = redColor.getProgress();
        this.valueSaveGreenBackgroundColor = greenColor.getProgress();
        this.valueSaveBlueBackgroundColor = blueColor.getProgress();
    }
    //Устанавливаем сохраненный цвет фона
    private void setBackgroundColorPallete() {
        redColor.setProgress(this.valueSaveRedBackgroundColor);
        greenColor.setProgress(this.valueSaveGreenBackgroundColor);
        blueColor.setProgress(this.valueSaveBlueBackgroundColor);
    }
    //Сохраняем цвет линий
    private void getLineColorPallete() {
        this.valueSaveRedLineColor = redColor.getProgress();
        this.valueSaveGreenLineColor = greenColor.getProgress();
        this.valueSaveBlueLineColor = blueColor.getProgress();
    }
    //Устанавливаем сохраненный цвет линий
    private void setLineColorPallete() {
        redColor.setProgress(this.valueSaveRedLineColor);
        greenColor.setProgress(this.valueSaveGreenLineColor);
        blueColor.setProgress(this.valueSaveBlueLineColor);
    }
    //Сохраняем полученное изображение в папке Pictures -> GeometryImages
    private void saveImage(String saveMode) {
        mBottomNavigationView.setVisibility(View.INVISIBLE);
        rlSaveSheet.setVisibility(View.INVISIBLE);

        View resultView = mainLayout.getRootView();
        try {
            resultView.setDrawingCacheEnabled(true);
            Bitmap bitmap = resultView.getDrawingCache();
            FileOutputStream ostream = null;
            File path = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
            File saveFolder = new File(path, "GeometryImages");
            if(!saveFolder.exists())
                saveFolder.mkdir();
            SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            Date myDate = new Date();
            switch (saveMode){
                case "png":
                    imageFile = new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES)+"/GeometryImages/",
                            "images"+timeStampFormat.format(myDate)+".png");
                    ostream = new FileOutputStream(imageFile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                    break;
                case "jpg80":
                    imageFile = new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES)+"/GeometryImages/",
                            "images"+timeStampFormat.format(myDate)+".jpg");
                    ostream = new FileOutputStream(imageFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                    break;
                case "jpg90":
                    imageFile = new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES)+"/GeometryImages/",
                            "images"+timeStampFormat.format(myDate)+".jpg");
                    ostream = new FileOutputStream(imageFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, ostream);
                    break;
                case "jpg100":
                    imageFile = new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES)+"/GeometryImages/",
                            "images"+timeStampFormat.format(myDate)+".jpg");
                    ostream = new FileOutputStream(imageFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
                    break;
            }
            if(ostream != null)
                ostream.close();
            String mCurrentPhotoPath = imageFile.getAbsolutePath();
            MediaScannerConnection.scanFile(this,
                    new String[] { mCurrentPhotoPath }, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {}
                    });

        } catch(Exception e){
            e.printStackTrace();
        }
        resultView.destroyDrawingCache();
        mBottomNavigationView.setVisibility(View.VISIBLE);
        rlSaveSheet.setVisibility(View.VISIBLE);
        Snackbar mSnackBar = Snackbar
                .make(mainLayout, R.string.saving_image, Snackbar.LENGTH_LONG)
                .setAction(R.string.open_mage, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        Uri uri = FileProvider.getUriForFile(MainActivity.this,
                                BuildConfig.APPLICATION_ID + ".provider",imageFile);
                        intent.setDataAndType(uri, "image/*");
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION |
                                Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        startActivity(intent);
                    }
                });
        mSnackBar.show();
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector){
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
            mDrawView.setScaleX(mScaleFactor);
            mDrawView.setScaleY(mScaleFactor);
            return true;
        }
    }
}
