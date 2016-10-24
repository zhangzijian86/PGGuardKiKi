package com.pg.pgguardkiki.tools;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.pg.pgguardkiki.R;

public class LoadingProgressDialog extends Dialog {

	private Context context;
    private ImageView img;
    private TextView txt;
	
	public LoadingProgressDialog(Context context,String msg) {
		// TODO Auto-generated constructor stub
		super(context,R.style.DialogStyle);
		this.context=context;
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View  view=inflater.inflate(R.layout.dialog_loading, null); 
        img=(ImageView) view.findViewById(R.id.loadingImageView);
        txt=(TextView) view.findViewById(R.id.loadingDialogTxt);
        Animation anim=AnimationUtils.loadAnimation(context, R.anim.loading_animation);
        img.setAnimation(anim);
        txt.setText(msg);
        setContentView(view);
	}
	
	 public void setMsg(String msg){
         txt.setText(msg);
	 }
	 public void setMsg(int msgId){
		 txt.setText(msgId);
	 }
	
}
