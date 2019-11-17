package ssu.cheesecake.blueberry.camera;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class OCRTask {

    private Context context;
    private TessBaseAPI tessBaseAPI;
    private String[] languageList = {
            "eng",
            "kor",
    };

    public OCRTask(Context context)
    {
        this.context = context;
        init();
    }

    public TessBaseAPI getTessBaseAPI() { return tessBaseAPI; }

    private void init()
    {

        String dir = context.getFilesDir() + "/tesseract";
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
        AssetManager assetMgr = context.getAssets();

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
