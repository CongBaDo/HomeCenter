package com.HomeCenter2.ui;

import java.util.Calendar;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.HomeCenter2.data.MyTime;
import com.HomeCenter2.data.configManager;

public class TimePickerFragment extends DialogFragment implements
		TimePickerDialog.OnTimeSetListener {
	public TimePickerFragment(TextView v) {
		mTextView = v;
	}
	
	private TextView mTextView = null;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		MyTime c = (MyTime)mTextView.getTag();
		int hour = c.getHour();
		int minute = c.getMinute();
		
		
		// Create a new instance of TimePickerDialog and return it
		return new TimePickerDialog(getActivity(), this, hour, minute,
				DateFormat.is24HourFormat(getActivity()));
	}	
	
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		Log.d("TimePickerFragment", "onTimeSet");
		configManager.setTime(mTextView, minute, hourOfDay);		
		
	}
}