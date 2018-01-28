package com.ToeTactics.toetoe;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ToggleButton;

public class Settings extends Activity{
	ToggleButton button;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
	}
}
