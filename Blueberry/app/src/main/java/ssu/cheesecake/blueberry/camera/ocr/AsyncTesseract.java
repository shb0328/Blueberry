package ssu.cheesecake.blueberry.camera.ocr;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

import net.steamcrafted.loadtoast.LoadToast;

import androidx.appcompat.app.AppCompatActivity;


public class AsyncTesseract extends AsyncTask<Bitmap, Integer, String> {

    private AppCompatActivity activity;

    private TessBaseAPI tessBaseAPI;
    private LoadToast loadToast;

    public AsyncTesseract(AppCompatActivity activity, LoadToast loadToast) {
        super();
        this.activity = activity;
        this.loadToast = loadToast;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loadToast = new LoadToast(activity);
        loadToast.setText("OCR Converting...");
        loadToast.show();
    }

    @Override
    protected String doInBackground(Bitmap... bitmaps) {
        tessBaseAPI.setImage(bitmaps[0]);

        return tessBaseAPI.getUTF8Text();
    }

    @Override
    protected void onCancelled() {
        loadToast.error();
        super.onCancelled();
    }

    @Override
    protected void onPostExecute(String resultText) {
        loadToast.success();
        Log.i("AsyncTesseract",resultText);

        //TODO:send resultText

        super.onPostExecute(resultText);
    }


}
