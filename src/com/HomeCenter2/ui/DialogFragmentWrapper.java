package com.HomeCenter2.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class DialogFragmentWrapper extends DialogFragment {
	public interface OnCreateDialogFragmentListener {
		public Dialog onCreateDialog(int id);
	}

	public static void showDialog(FragmentManager fragmentManager,
			OnCreateDialogFragmentListener listener, int id) {
		if(fragmentManager == null) {
			return;
		}
		removeDialog(fragmentManager);
		FragmentTransaction ft = fragmentManager.beginTransaction();
		// Create and show the mDialog.
		DialogFragmentWrapper newFragment = DialogFragmentWrapper.newInstance(
				listener, id);
		newFragment.show(ft, "mDialog");
	}

	public static void removeDialog(FragmentManager fragmentManager, int id) {
		removeDialog(fragmentManager);
	}

	private static void removeDialog(FragmentManager fragmentManager) {	
		if(fragmentManager == null) {
			return;
		}
		try {
			FragmentTransaction ft = fragmentManager.beginTransaction();
			DialogFragment prev = (DialogFragment) fragmentManager
					.findFragmentByTag("mDialog");
			if (prev != null) {
				ft.remove(prev);
				prev.dismiss();
			}
			ft.addToBackStack(null);
		} catch( IllegalStateException ex ) {   
			// Catch illegal state exception if we are in the background
			// No good work around for V4 Library
		}
	}

	private static DialogFragmentWrapper newInstance(
			OnCreateDialogFragmentListener listener, int id) {
		DialogFragmentWrapper frag = new DialogFragmentWrapper(listener);
		Bundle args = new Bundle();
		args.putInt("id", id);
		frag.setArguments(args);
		return frag;
	}

	private OnCreateDialogFragmentListener m_listener = null;

	private DialogFragmentWrapper(OnCreateDialogFragmentListener listener) {
		m_listener = listener;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		int id = getArguments().getInt("id");
		if (m_listener != null) {
			return m_listener.onCreateDialog(id);
		}
		return null;
	}
}
