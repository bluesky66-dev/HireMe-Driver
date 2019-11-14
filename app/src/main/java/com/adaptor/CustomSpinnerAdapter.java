package com.adaptor;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.driver.hire_me.Controller;
import com.driver.hire_me.R;
import com.fonts.Fonts;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {
	Typeface font = Typeface.createFromAsset(getContext().getAssets(),	Fonts.ROBOTO_CONDENCE);
	Controller controller;
	LayoutInflater inflater;
	String[]items;
	String[] userId;
	Context context;

	public CustomSpinnerAdapter(Context context, int spinner_text, String[] items) {
		super(context,spinner_text,items);
		this.context=context;
		inflater = LayoutInflater.from(context);
		this.items = items;

	}
	public CustomSpinnerAdapter(Context context, int spinner_text, String[] items, String[] userId) {
  		super(context,spinner_text,items);
		this.context=context;
		inflater = LayoutInflater.from(context);
		this.items = items;
		this.userId=userId;

	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		controller= (Controller) getContext().getApplicationContext();
		MyViewHolder mViewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.spinner_text, null);
			mViewHolder = new MyViewHolder();
			mViewHolder.dropItems = (TextView) convertView.findViewById(R.id.spinner_textview);
			mViewHolder.dropItems.setTypeface(font);
		//	mViewHolder.dropItems.setTextColor(Color.parseColor("#ffffff"));
		//	mViewHolder.dropItems.setTypeface(font);
			convertView.setTag(mViewHolder);
		} else {
			mViewHolder = (MyViewHolder) convertView.getTag();
		}
		System.out.println("items size = "+ items.length );
		mViewHolder.dropItems.setText(items[position]);
		mViewHolder.dropItems.setTypeface(font);

	//	mViewHolder.dropItems.setTextColor(Color.parseColor("#919090"));
		return convertView;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		MyViewHolder mViewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.spinner_dropdown, null);
			mViewHolder = new MyViewHolder();
			mViewHolder.dropItems = (TextView) convertView.findViewById(R.id.spinner_textview);
			mViewHolder.dropItems.setTypeface(font);
		//	mViewHolder.dropItems.setTextColor(Color.parseColor("#FFFFFF"));
		//	mViewHolder.dropItems.setTypeface(font);
			convertView.setTag(mViewHolder);
		} else {
			mViewHolder = (MyViewHolder) convertView.getTag();
		}
		mViewHolder.dropItems.setText(items[position]);
		mViewHolder.dropItems.setTypeface(font);
//		controller.setUserId(userId[position]);
//		String s=controller.getUserId();
//		Toast.makeText(context, "user id ::" + s, Toast.LENGTH_LONG).show();
//		Log.d("User id is::", controller.getUserId());
		mViewHolder.dropItems.setTextColor(Color.parseColor("#FFFFFF"));
		return convertView;
	}

	private class MyViewHolder {
		public TextView dropItems;
	}
}
