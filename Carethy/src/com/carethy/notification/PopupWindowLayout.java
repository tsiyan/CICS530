package com.carethy.notification;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carethy.R;
import com.carethy.activity.MainActivity;

public class PopupWindowLayout extends LinearLayout {
	private TextView tv,tl;
	private Button btn_dismiss;
	private Button btn_open;
	private WindowManager wm; 

	public PopupWindowLayout(Context context, String str, WindowManager wmr) {
		super(context);
		this.wm = wmr;
		this.setOrientation(1);
		this.setBackgroundResource(R.color.actionbar_background);
		this.setMinimumWidth(400);
		
		LinearLayout llyt = new LinearLayout(context);
		llyt.setOrientation(0);
		ImageView vi = new ImageView(context);
		vi.setImageResource(R.drawable.ic_launcher);
		vi.setLayoutParams(new LinearLayout.LayoutParams(150, 150));
		vi.setPadding(25, 25, 25, 25);
		llyt.addView(vi);
		
		tl = new TextView(context);
		tl.setText("Carethy");
		tl.setTextSize(18);
		tl.setTextColor(getResources().getColor(R.color.actionbar_text));
		tl.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
		tl.setLayoutParams(new LinearLayout.LayoutParams(-1, -1, (float)1.0));
		tl.setPadding(0, 35, 0, 35);
		llyt.addView(tl);
		llyt.setGravity(Gravity.LEFT);
		
		this.addView(llyt);
		
		tv = new TextView(context);
		tv.setText(str);
		tv.setTextSize(18);
		tv.setTextColor(Color.BLACK);
		tv.setBackgroundColor(Color.WHITE);
		tv.setTypeface(Typeface.SANS_SERIF, Typeface.NORMAL);
		tv.setPadding(30, 20, 30, 20);
		this.addView(tv);

		LinearLayout lly = new LinearLayout(context);
		lly.setOrientation(0);
		
		btn_dismiss = new Button(context);
		btn_dismiss.setText("Dismiss");
		btn_dismiss.setTextColor(Color.parseColor("#2C3E50"));
		btn_dismiss.setOnClickListener(new OnClickListener(){
	        @Override 
	        public void onClick(View v)  {
	        	removePopup();
	        }
		});
		btn_dismiss.setLayoutParams(new LinearLayout.LayoutParams(-1, -1, (float) 1.0));
		btn_dismiss.setBackgroundColor(Color.parseColor("#ecf0f1"));
		//btn_dismiss.setBackgroundResource(R.color.button);
		btn_dismiss.setScaleX((float)0.995);
		btn_dismiss.setPadding(0, 0, 5, 0);
		lly.addView(btn_dismiss);
		
		btn_open = new Button(context);
		btn_open.setText("Open");
		btn_open.setTextColor(Color.parseColor("#2C3E50"));
		btn_open.setOnClickListener(new OnClickListener(){
	        @Override 
	        public void onClick(View v)  {
	        	removePopup();
	        	Intent intent = new Intent(v.getContext(), MainActivity.class);
	        	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        	v.getContext().startActivity(intent);
	        }
		});
		btn_open.setLayoutParams(new LinearLayout.LayoutParams(-1, -1, (float) 1.0));
		btn_open.setScaleX((float)0.995);
		//btn_open.setBackgroundColor(getResources().getColor(R.color.button));
		btn_open.setBackgroundColor(Color.parseColor("#ecf0f1"));
		
		lly.addView(btn_open);
		
		this.addView(lly);

	}

	public void removePopup() {
		this.wm.removeView(this);
	}
}