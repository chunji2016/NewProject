package com.example.reportview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

	LinearLayout arc;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		arc = (LinearLayout) findViewById(R.id.arc);//圆弧计分
		ProgressArc progressArc = new ProgressArc(this, 50);
		progressArc.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		arc.addView(progressArc);
	}
}
