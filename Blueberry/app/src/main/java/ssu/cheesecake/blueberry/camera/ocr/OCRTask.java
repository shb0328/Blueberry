package ssu.cheesecake.blueberry.camera.ocr;

import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import androidx.appcompat.app.AppCompatActivity;

public class OCRTask {

    private AppCompatActivity activity;
    private TessBaseAPI tessBaseAPI;
    private MessageHandler messageHandler;
    private String[] languageList = {
            "eng",
            "kor",
            "chi_tra",
            "ara",
            "hin",
            "osd"
    };

    public OCRTask(AppCompatActivity activity)
    {
        this.activity = activity;
        init();
    }

    public TessBaseAPI getTessBaseAPI() { return tessBaseAPI; }

    private void init()
    {

        String dir = activity.getFilesDir() + "/tesseract";
        Log.d("OCR Task init",dir);
        String languages = "";
        for(String lang : languageList){
            if(checkLanguageFile(dir+"/tessdata/",lang)) {
                languages += lang + "+";
            }
        }
        languages = languages.substring(0,languages.length()-1);
        Log.d("OCR Task init","languages : "+languages);

        tessBaseAPI = new TessBaseAPI();
        tessBaseAPI.init(dir, languages);
    }


    private boolean checkLanguageFile(String dir,String language)
    {
        File file = new File(dir);
        if(!file.exists() && file.mkdirs()){
            createFiles(dir,language);
        }
        else if(file.exists()){
            String filePath = dir + language +".traineddata";
            File langDataFile = new File(filePath);
            if(!langDataFile.exists())
                createFiles(dir,language);
        }
        return true;
    }

    private void createFiles(String dir,String language)
    {
        AssetManager assetMgr = activity.getAssets();

        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            inputStream = assetMgr.open(language + ".traineddata");
            String destFile = dir + language + ".traineddata";
            outputStream = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            inputStream.close();
            outputStream.flush();
            outputStream.close();

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }


}
