package com.HomeCenter2.ui.slidingmenu.framework;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActionBar extends Fragment implements INumberNotificationListener {

	private TextView mPrimaryTitle, mNumberNotification;
	private int mTitleId;
	private int mResourceId;
	private View mView;
	public IActionBarListener mListener;
	public String mResource;

	public ActionBar(int resourceId, int _title) {
		mResourceId = resourceId;
		mTitleId = _title;
		mResource = null;
		// Retain this instance is to restore the state of Fragment since it has been re-launched from background
		// and to avoid NullPointerException in Fragment.instantiate
		setRetainInstance(true); 
	}

	public ActionBar(String resource, int _title) {
		mResource = resource;
		mTitleId = _title;
		mResourceId = -1;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mResourceId == -1) {
			Resources res = getResources();
			mResourceId = res.getIdentifier(mResource + "_header", "layout",
					getActivity().getPackageName());
		}
		mView = inflater.inflate(mResourceId, container, false);
		return mView;
	}

	@Override
	public void onResume() {
		super.onResume();
		mListener.onCreated();
	}

	public View getRootView() {
		return mView;
	}

	public void setPrimaryTitle(int resourceId) {
		mTitleId = resourceId;
		if (mPrimaryTitle != null && resourceId != -1) {
			mPrimaryTitle.setText(getString(resourceId));
		}
	}

	public void setPrimaryTitle(String title) {
		if (mPrimaryTitle != null && title != null) {
			mPrimaryTitle.setText(title);
		}
	}

	public void setMenuButtonCallBack(int resId, final MenuCallBack callBack) {
		if (callBack != null) {
	        View view = (View) mView.findViewById(resId);

	        if (view != null) {
	            view.setOnClickListener(new OnClickListener() {
	                @Override
	                public void onClick(View v) {
	                    callBack.doAction();
	                }
	            });
	        }
		}
	}

	public void setMenuImageViewCallBack(int resId, int backgoundId, final MenuCallBack callBack) {
        if (callBack != null) {
            ImageView imgView = (ImageView) mView.findViewById(resId);

            if (imgView != null) {
                imgView.setImageResource(backgoundId);
                imgView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callBack.doAction();
                    }
                });
            }
		}
	}

	public void setTitle(int resourceId) {
		mPrimaryTitle = (TextView) mView.findViewById(resourceId);
		if (mTitleId != -1)
			mPrimaryTitle.setText(getString(mTitleId));
	}

	public void setTextViewText(int textViewId, int textResourceId) {
	    View v = mView.findViewById(textViewId);
	    if (v instanceof TextView) {
	        String text = getString(textResourceId);
	        ((TextView) v).setText(text);
	    }
	}

	public void setTextViewText(int textViewId, String text) {
        View v = mView.findViewById(textViewId);
        if (v instanceof TextView) {
            ((TextView) v).setText(text);
        }
	}

	public LinearLayout setMenuLinearLayout(int resId,
			final MenuCallBack callBack) {
		LinearLayout linear = (LinearLayout) mView.findViewById(resId);
		if (callBack != null) {
			linear.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					callBack.doAction();

				}
			});
		}
		return linear;
	}

	public void setNumberNotification(int resourceId) {
		if (resourceId != -1) {
			/*
			 * mNumberNotification = (TextView) mView.findViewById(resourceId);
			 * RADialerUIEngine engine = RADialerUIEngine.instant();
			 * engine.addNumberNotificationObserver(this);
			 */
		}
	}

	public interface MenuCallBack {
		void doAction();
	}

	public interface IActionBarListener {
		void onCreated();
	}

	@Override
	public void onNumberNotificationChange(int number) {

	}

	public void setVisibleChildView(int textViewId, int visible) {
		View v = mView.findViewById(textViewId);
		v.setVisibility(visible);
	}

	public View setViewTitle(int resId, View view,
			final MenuCallBack callBack) {
		LinearLayout linear = (LinearLayout) mView.findViewById(resId);
		ViewGroup parent = (ViewGroup)view.getParent();
		
		if (parent != null) {
			parent.removeView(view);			
		}
		
		linear.addView(view);
		if (callBack != null) {
			linear.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					callBack.doAction();

				}
			});
		}
		return linear;
	}
}
