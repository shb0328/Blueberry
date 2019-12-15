package ssu.cheesecake.blueberry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import ssu.cheesecake.blueberry.custom.CustomGroup;

public class GroupAdapter extends ArrayAdapter {

    Context context;
    int resID;
    ArrayList<CustomGroup> data;

    public GroupAdapter(@NonNull Context context, int resource, @NonNull ArrayList<CustomGroup> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resID = resource;
        this.data = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resID,null);
            GroupHolder groupHolder = new GroupHolder(convertView);
            convertView.setTag(groupHolder);
        }

        GroupHolder holder = (GroupHolder)convertView.getTag();
//        ImageView thumbnail = holder.thumbnail;
        TextView groupName = holder.groupName;

        CustomGroup customGroup = data.get(position);

//        String url = businessCard.getImageUrl();
//        Bitmap img = BitmapFactory.decodeFile(url);
//        thumbnail.setImageBitmap(img);

//        thumbnail.setImageBitmap(img);

        String groupname = customGroup.getGroupName();
        groupName.setText(groupname);

        return convertView;
    }
}
