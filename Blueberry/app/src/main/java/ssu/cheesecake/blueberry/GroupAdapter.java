package ssu.cheesecake.blueberry;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Vector;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import ssu.cheesecake.blueberry.custom.CustomGroup;

public class GroupAdapter extends ArrayAdapter {

    Context context;
    int resID;
    Vector<CustomGroup> data;

    View.OnTouchListener groupDeleteListener;

    public GroupAdapter(@NonNull Context context, int resource, @NonNull Vector<CustomGroup> objects,View.OnTouchListener groupDeleteListener) {
        super(context, resource, objects);
        this.context = context;
        this.resID = resource;
        this.data = objects;
        this.groupDeleteListener = groupDeleteListener;
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
        TextView groupName = holder.groupName;
        groupName.setTextColor(Color.GRAY);

        CustomGroup customGroup = data.get(position);

        String groupname = customGroup.getGroupName();
        groupName.setText(groupname);

        groupName.setOnTouchListener(groupDeleteListener);
        return convertView;
    }

}
