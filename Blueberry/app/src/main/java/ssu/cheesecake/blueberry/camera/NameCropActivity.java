package ssu.cheesecake.blueberry.camera;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.googlecode.tesseract.android.TessBaseAPI;

import net.steamcrafted.loadtoast.LoadToast;

import java.util.Arrays;
import java.util.HashMap;

import ssu.cheesecake.blueberry.R;

enum Language {
    KR, EN
}

public class NameCropActivity extends AppCompatActivity implements View.OnClickListener{

    private Context app = this;

    private String imagePath = "";
    private Bitmap bitmap;

    private NameCropImageView nameCropImageView;
    private RadioGroup languageRadioGroup;
    private Button okButton, cancelButton;

    private Point leftTop, rightBottom;

    /**
     * OCR
     */
    private boolean isOCRFailed = false;
    private LoadToast loadToast;
    private OCRResult ocrResult;
    private HashMap<String, String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_crop);
        nameCropImageView = findViewById(R.id.nameCropImageView);
        languageRadioGroup = findViewById(R.id.languageRadioGroup);
        okButton = findViewById(R.id.btn_ok_name);
        cancelButton = findViewById(R.id.btn_cancel_name);

        okButton.setOnClickListener(this);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        imagePath = intent.getStringExtra("imagePath");
        bitmap = BitmapFactory.decodeFile(imagePath);
        nameCropImageView.init(bitmap);

        map = new HashMap<>();

        Log.d("DEBUG!", "Start!");
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance().getCloudTextRecognizer();
        FirebaseVisionCloudTextRecognizerOptions options = new FirebaseVisionCloudTextRecognizerOptions.Builder()
                .setLanguageHints(Arrays.asList("en", "kr"))
                .build();
        Log.d("DEBUG!", "Start!");
        Task<FirebaseVisionText> result =
                detector.processImage(image)
                        .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                            @Override
                            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                Log.d("DEBUG!", "Finish!");
                                String resultString = firebaseVisionText.getText();
                                Log.d("DEBUG!", resultString);
                            }
                        })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("DEBUG!", "Failed!");

                                    }
                                }
                        );

    }

    @Override
    protected void onDestroy() {
        if (isOCRFailed == true) {
            Toast.makeText(this, "cancel...", Toast.LENGTH_SHORT).show();
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        if (languageRadioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "언어를 선택해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
//        //leftTop, rightBottom Point 얻어옴
//        leftTop = nameCropImageView.getLeftTop();
//        rightBottom = nameCropImageView.getRightBottom();
//        Log.d("DEBUG!", "onClick: " + nameCropImageView.getWidth() + "," + nameCropImageView.getHeight());
//        //image의 크기가 view보다 크다면
//        if(bitmap.getWidth() > nameCropImageView.getWidth()) {
//            //view에서의 좌표를 image에서의 좌표로 재설정
//            leftTop.x = (int) (leftTop.x * ((float) bitmap.getWidth() / nameCropImageView.getWidth()));
//            leftTop.y = (int) (leftTop.y * ((float) bitmap.getWidth() / nameCropImageView.getWidth()));
//            rightBottom.x = (int) (rightBottom.x * ((float) bitmap.getWidth() / nameCropImageView.getWidth()));
//            rightBottom.y = (int) (rightBottom.y * ((float) bitmap.getWidth() / nameCropImageView.getWidth()));
//        }
//        //좌표값이 image의 범위를 벗어날 경우 image 내의 좌표로 재설정
//        if(leftTop.x < 0)
//            leftTop.x = 0;
//        if(leftTop.y < 0)
//            leftTop.y = 0;
//        if(rightBottom.x > bitmap.getWidth())
//            rightBottom.x = bitmap.getWidth();
//        if(rightBottom.y > bitmap.getHeight())
//            rightBottom.y = bitmap.getHeight();
//
//        Bitmap nameBitmap = cropNameImage(leftTop.x,leftTop.y,rightBottom.x,rightBottom.y);
//        AsyncTesseract asyncTesseractForName;
//        if(languageRadioGroup.getCheckedRadioButtonId() == R.id.korRadioBtn){
//            asyncTesseractForName = new AsyncTesseract(Language.KR);
//            asyncTesseractForName.setOCRInitializer();
//            asyncTesseractForName.execute(nameBitmap);
//
//        }else if(languageRadioGroup.getCheckedRadioButtonId() == R.id.engRadioBtn){
//            asyncTesseractForName = new AsyncTesseract(Language.EN);
//            asyncTesseractForName.setOCRInitializer();
//            asyncTesseractForName.execute(nameBitmap);
//
//        }
//         Bitmap phoneAndEmailBitmap = cropPhoneAndEmailImage(leftTop.x,leftTop.y,rightBottom.x,rightBottom.y);
//         AsyncTesseract asyncTesseractForPhoneAndEmail;
//         asyncTesseractForPhoneAndEmail = new AsyncTesseract();
//         asyncTesseractForPhoneAndEmail.setOCRInitializer();
//         asyncTesseractForPhoneAndEmail.execute(phoneAndEmailBitmap);

    }


    private Bitmap cropNameImage(int left, int top, int right, int bottom) {
        Rect rect = new Rect(left, top, right, bottom);
        return Bitmap.createBitmap(bitmap, rect.left, rect.top, rect.width(), rect.height());
    }

    private Bitmap cropPhoneAndEmailImage(int left, int top, int right, int bottom) {
        Bitmap restBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Rect rect = new Rect(left, top, right, bottom);
        //Crop 영역 제외한 image 저장할 Canvas
        Canvas canvas = new Canvas(restBitmap);
        Paint paint = new Paint();
        //Rect 제외한 영역만 활성화
        canvas.drawRect(rect, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        //Bitmap 위에 실제 image 입력
        canvas.drawBitmap(bitmap, 0, 0, paint);
        //전체 영역 Bitmap에 저장 후 return
        return Bitmap.createBitmap(restBitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight());
    }

    class AsyncTesseract extends AsyncTask<Bitmap, Integer, String> {

        private TessBaseAPI tessBaseAPI;
        private Language nameLanguage;

        public AsyncTesseract() {
            this(null);
        }

        public AsyncTesseract(Language nameLanguage) {
            super();
            this.nameLanguage = nameLanguage;
        }

        public void setOCRInitializer() {
            if (isName()) {
                OCRInitializer ocrInitializer = new OCRInitializer(app, nameLanguage);
                tessBaseAPI = ocrInitializer.getTessBaseAPI();
                return;
            }
            OCRInitializer ocrInitializer = new OCRInitializer(app);
            tessBaseAPI = ocrInitializer.getTessBaseAPI();
        }

        private boolean isName() {
            return nameLanguage != null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (isName()) {
                loadToast = new LoadToast(app);
                loadToast.setText("OCR Converting...");
                //TODO: center,, not 1000
                loadToast.setTranslationY(1000);
                loadToast.show();
            }
        }

        @Override
        protected String doInBackground(Bitmap... bitmaps) {
            tessBaseAPI.setImage(bitmaps[0]);
            return tessBaseAPI.getUTF8Text();
        }

        @Override
        protected void onCancelled() {
            if (isName()) {
                loadToast.error();
            }
            isOCRFailed = true;
            onDestroy();
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(String resultText) {
            Log.i("OCR result", resultText);

            if (isName()) {
                map.put("name", resultText);
                loadToast.success();
                Log.i("OCR result",
                        "\nname: " + map.get("name"));
            } else {
                ocrResult = new OCRResult(resultText);
                map.put("email", ocrResult.parseEmail());
                map.put("phone", ocrResult.parsePhone());
                Log.i("OCR result",
                        "\nemail: " + map.get("email")
                                + "\nphone: " + map.get("phone"));
            }

            super.onPostExecute(resultText);
        }
    }
}
