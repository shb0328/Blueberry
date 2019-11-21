package ssu.cheesecake.blueberry;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class GroupSub extends LinearLayout {


        public GroupSub(Context context, AttributeSet attrs) {
            super(context, attrs);

            init(context);
        }

        public GroupSub(Context context) {
            super(context);

            init(context);
        }
        private void init(Context context){
            LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.subgroup,this,true);
        }

}
