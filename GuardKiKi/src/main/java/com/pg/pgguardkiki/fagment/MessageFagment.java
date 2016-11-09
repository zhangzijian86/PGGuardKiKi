package com.pg.pgguardkiki.fagment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pg.pgguardkiki.R;

/**
 * Created by zzj on 11/9/16.
 */
public class MessageFagment  extends Fragment {

    private TextView MessageFagmentTV;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_message, null);
        MessageFagmentTV = (TextView)view.findViewById(R.id.MessageFagmentTV);
        return view;
    }

    public void setTextColor(){
        MessageFagmentTV.setText("#7423956");
    }

    //重写setMenuVisibility方法，不然会出现叠层的现象
    @Override
    public void setMenuVisibility(boolean menuVisibile) {
        super.setMenuVisibility(menuVisibile);
        if (this.getView() != null) {
            this.getView().setVisibility(menuVisibile ? View.VISIBLE : View.GONE);
        }
    }

}