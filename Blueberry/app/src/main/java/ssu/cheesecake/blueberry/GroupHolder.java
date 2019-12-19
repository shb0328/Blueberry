package ssu.cheesecake.blueberry;

import android.view.View;
import android.widget.TextView;

public class GroupHolder {
    TextView groupName;

    public GroupHolder(View view) {
        super();
        groupName = view.findViewById(R.id.groupname);
    }
}
