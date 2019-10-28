package ssu.cheesecake.blueberry;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayOutputStream;
import java.io.File;

import ssu.cheesecake.blueberry.R;

import static android.app.Activity.RESULT_CANCELED;

public class GalleryFragment extends Fragment implements OnBackPressedListener{
    public File tempFile;
    private static final int REQUEST_CODE = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static Fragment navHostFragment;
    private static BottomNavigationView navView;
    private static Menu menu;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        //Navigation Menu bar Icon 변경
        navHostFragment = this.getActivity().getSupportFragmentManager().getFragments().get(0);
        navView = navHostFragment.getActivity().findViewById(R.id.nav_view);
        menu = navView.getMenu();
        menu.getItem(0).setChecked(true);

        //Gallery 실행
        goToGallery();
        //Navigation Menu Stack에서 GalleryFragment를 Pop함
        //Gallery에서 Back Button으로 앱으로 되돌아올 시 ListFragment 출력하기 위함
        //navHostFragment.getChildFragmentManager().popBackStack();
        return root;
    }

    private void goToGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_CANCELED)
            return;

        if (requestCode == PICK_FROM_ALBUM) {

            Uri photoUri = data.getData();

            Cursor cursor = null;

            try {

                /*
                 *  Uri 스키마를
                 *  content:/// 에서 file:/// 로  변경한다.
                 */
                String[] proj = {MediaStore.Images.Media.DATA};

                cursor = this.getActivity().getContentResolver().query(photoUri, proj, null, null, null);

                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                cursor.moveToFirst();

                String imagePath = cursor.getString(column_index);

                tempFile = new File(imagePath);

                Intent intent = new Intent(this.getActivity(), ReCheck.class);
                intent.putExtra("imagePath", imagePath);
                startActivity(intent);

            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            setImage();

        }
    }
    private void setImage() {

        ImageView imageView = this.getActivity().findViewById(R.id.fragmentGalleryImageView);

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);

        imageView.setImageBitmap(originalBm);

    }

    @Override
    public void onBackPressed() {
        Fragment navHostFragment = this.getActivity().getSupportFragmentManager().getFragments().get(0);
        navHostFragment.getChildFragmentManager().popBackStack();
    }
}