package ssu.cheesecake.blueberry;

import android.view.View;
import android.widget.TextView;

public class GroupHolder {
//    ImageView thumbnail;
    TextView groupName;

    public GroupHolder(View view) {
        super();
//        thumbnail = view.findViewById(R.id.thumbnail);
        groupName = view.findViewById(R.id.groupname);
    }
}
