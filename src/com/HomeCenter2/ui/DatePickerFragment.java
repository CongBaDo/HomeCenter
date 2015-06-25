package com.HomeCenter2.ui;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.Time;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.HomeCenter2.data.MyDate;
import com.HomeCenter2.data.configManager;

public class DatePickerFragment extends DialogFragment implements
		DatePickerDialog.OnDateSetListener {
	
	public DatePickerFragment(TextView v) {
		mTextView = v;
	}
	
	private TextView mTextView = null;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		MyDate c = (MyDate)mTextView.getTag();
		int year = c.getYear();
		int month = c.getMonth();
		int day = c.getDay();

		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month - 1, day);
	}

	public void onDateSet(DatePicker view, int year, int month, int day) {
		configManager.setDate(mTextView, day, month + 1, year);			
	}
}