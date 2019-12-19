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

import java.util.ArrayList;
import java.util.Arrays;

import ssu.cheesecake.blueberry.EditActivity;
import ssu.cheesecake.blueberry.R;
import ssu.cheesecake.blueberry.util.StringParser;

enum Language {
    KR, EN
}

public class NameCropActivity extends AppCompatActivity implements View.OnClickListener {

    private Context app = this;

    private String imagePath = "";
    private String fileName;
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
    private StringParser stringParser;

    //results
    private ArrayList<String> name;
    private ArrayList<String> phone;
    private ArrayList<String> email;
    private ArrayList<String> webSite;
    private ArrayList<String> company;
    private ArrayList<String> address;


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
        fileName = intent.getStringExtra("fileName");
        bitmap = BitmapFactory.decodeFile(imagePath);
        nameCropImageView.init(bitmap);


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

        setRegionOfName();

        Bitmap nameBitmap = cropNameImage(leftTop.x, leftTop.y, rightBottom.x, rightBottom.y);
        AsyncTesseract asyncTesseract;
        if (languageRadioGroup.getCheckedRadioButtonId() == R.id.korRadioBtn) {
            asyncTesseract = new AsyncTesseract(Language.KR);
            asyncTesseract.setOCRInitializer();
            asyncTesseract.execute(nameBitmap);

        } else if (languageRadioGroup.getCheckedRadioButtonId() == R.id.engRadioBtn) {
            asyncTesseract = new AsyncTesseract(Language.EN);
            asyncTesseract.setOCRInitializer();
            asyncTesseract.execute(nameBitmap);
        }

    }

    private void setRegionOfName() {
        //leftTop, rightBottom Point 얻어옴
        leftTop = nameCropImageView.getLeftTop();
        rightBottom = nameCropImageView.getRightBottom();
        Log.d("DEBUG!", "onClick: " + nameCropImageView.getWidth() + "," + nameCropImageView.getHeight());
        //image의 크기가 view보다 크다면
        if (bitmap.getWidth() > nameCropImageView.getWidth()) {
            //view에서의 좌표를 image에서의 좌표로 재설정
            leftTop.x = (int) (leftTop.x * ((float) bitmap.getWidth() / nameCropImageView.getWidth()));
            leftTop.y = (int) (leftTop.y * ((float) bitmap.getWidth() / nameCropImageView.getWidth()));
            rightBottom.x = (int) (rightBottom.x * ((float) bitmap.getWidth() / nameCropImageView.getWidth()));
            rightBottom.y = (int) (rightBottom.y * ((float) bitmap.getWidth() / nameCropImageView.getWidth()));
        }
        //좌표값이 image의 범위를 벗어날 경우 image 내의 좌표로 재설정
        if (leftTop.x < 0)
            leftTop.x = 0;
        if (leftTop.y < 0)
            leftTop.y = 0;
        if (rightBottom.x > bitmap.getWidth())
            rightBottom.x = bitmap.getWidth();
        if (rightBottom.y > bitmap.getHeight())
            rightBottom.y = bitmap.getHeight();
    }


    private Bitmap cropNameImage(int left, int top, int right, int bottom) {
        Rect rect = new Rect(left, top, right, bottom);
        return Bitmap.createBitmap(bitmap, rect.left, rect.top, rect.width(), rect.height());
    }

    private Bitmap cropTheOtherImage(int left, int top, int right, int bottom) {
        Bitmap restBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Rect rect = new Rect(left, top, right, bottom);
        Canvas canvas = new Canvas(restBitmap);
        Paint paint = new Paint();
        canvas.drawRect(rect, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return Bitmap.createBitmap(restBitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight());
    }

    class AsyncTesseract extends AsyncTask<Bitmap, Integer, String> {

        private TessBaseAPI tessBaseAPI;
        private Language nameLanguage;

        public AsyncTesseract(Language nameLanguage) {
            super();
            this.nameLanguage = nameLanguage;
        }

        public void setOCRInitializer() {
            OCRInitializer ocrInitializer = new OCRInitializer(app, nameLanguage);
            tessBaseAPI = ocrInitializer.getTessBaseAPI();
        }

        @Override
        protected void onPreExecute() {
            loadToast = new LoadToast(app);
            loadToast.setText("명함에서 글자를 인식하고 있습니다...");
            loadToast.setTranslationY(1000);
            loadToast.show();

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Bitmap... bitmaps) {
            tessBaseAPI.setImage(bitmaps[0]);
            return tessBaseAPI.getUTF8Text();
        }

        @Override
        protected void onCancelled() {
            loadToast.error();
            isOCRFailed = true;
            onDestroy();
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(String resultText) {
            name = new ArrayList<String>();
            name.add(resultText);
            Log.i("OCR result", "\n******\nname: " + resultText + "\n******\n");
            recogrizeFirebaseVisionText();
            super.onPostExecute(resultText);
        }

        private void recogrizeFirebaseVisionText() {

            FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(cropTheOtherImage(leftTop.x, leftTop.y, rightBottom.x, rightBottom.y));
            FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance().getCloudTextRecognizer();
            new FirebaseVisionCloudTextRecognizerOptions.Builder().setLanguageHints(Arrays.asList("en", "kr")).build();
            Task<FirebaseVisionText> result = detector.processImage(image).addOnSuccessListener(
                    new OnSuccessListener<FirebaseVisionText>() {
                        @Override
                        public void onSuccess(FirebaseVisionText firebaseVisionText) {
                            loadToast.success();

                            stringParser = new StringParser(firebaseVisionText);
                            String resultString = firebaseVisionText.getText();
                            Log.i("OCR result", "\n******\n" + resultString + "\n******\n");

                            phone = stringParser.GetNumberArray();
                            email = stringParser.GetEmailArray();
                            company = stringParser.GetCompanyArray();
                            address = stringParser.GetAddressArray();
                            webSite = stringParser.GetWebSiteArray();

                            Intent intent = new Intent(app, EditActivity.class);
                            intent.putExtra("path", imagePath);
                            intent.putExtra("fileName", fileName);
                            intent.putExtra("name", name);
                            intent.putExtra("phone", phone);
                            intent.putExtra("email", email);
                            intent.putExtra("company", company);
                            intent.putExtra("address", address);
                            intent.putExtra("webSite",webSite);
                            intent.putExtra("mode", "new");

                            startActivity(intent);
                            finish();
                        }
                    })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("DEBUG!", "Failed!");
                                    loadToast.error();
                                    isOCRFailed = true;
                                    onDestroy();
                                }
                            }
                    );
        }
    }
}