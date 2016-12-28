package org.androidtown.ui.pager;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 한 페이지를 위한 레이아웃
 *
 * @author Mike
 */
public class PersonPage extends LinearLayout {
	Context mContext;



	ImageView iconImage;



	private int[] resIds = {R.drawable.dream01, R.drawable.dream02, R.drawable.dream03};

	int countIndexes = resIds.length;

	public static final int CALL_NUMBER = 1001;

	public PersonPage(Context context) {
		super(context);

		init(context);
	}

	public PersonPage(Context context, AttributeSet attrs) {
		super(context, attrs);

		init(context);
	}

	private void init(Context context) {
		mContext = context;


		// inflate XML layout
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.person_page, this, true);

		iconImage = (ImageView) findViewById(R.id.iconImage);


	}




	public void setImage(int resId) {
		iconImage.setImageResource(resId);
	}





}

