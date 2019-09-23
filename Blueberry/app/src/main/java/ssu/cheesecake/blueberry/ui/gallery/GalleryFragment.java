package ssu.cheesecake.blueberry.ui.gallery;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import ssu.cheesecake.blueberry.GalleryActivity;
import ssu.cheesecake.blueberry.MainActivity;
import ssu.cheesecake.blueberry.OptionActivity;
import ssu.cheesecake.blueberry.R;

public class GalleryFragment extends Fragment {


    private GalleryViewModel galleryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        Intent intent = new Intent(this.getActivity(), GalleryActivity.class);
        startActivity(intent);
        return root;
    }


    }