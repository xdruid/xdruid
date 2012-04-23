package org.xdruid.examples.tabata.screens;

import org.xdruid.examples.tabata.R;
import org.xdruid.ui.Dispatcher;
import org.xdruid.ui.SimpleScreen;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainScreen extends SimpleScreen{

	
	private Button buttonTabataTimer;
	private Button buttonTimer;
	private Button buttonStopwatch;
	
	
	public MainScreen(Activity parent, Dispatcher dispatcher, String name) {
		super(parent, dispatcher, name);
	}

	@Override
	protected void bindLayouts() {
		bindLayout("tabata.main", R.layout.main);
	}
	
	
	@Override
	protected void bindViews(Activity parent) throws Exception {
		buttonStopwatch = (Button)parent.findViewById(R.id.stopwatch);
		buttonTabataTimer = (Button)parent.findViewById(R.id.tabataTimer);
		buttonTimer = (Button)parent.findViewById(R.id.timer);
	}
	
	
	public void screenReloading(Object domainObject) throws Exception {
	}

	public Object getValue() {
		return null;
	}

	@Override
	protected String getInitialLayout() {
		return "tabata.main";
	}

	@Override
	protected void bindEvents() throws Exception {
		on("click", buttonTimer, "btnTimerClick");
		on("click", buttonTabataTimer, "btnTabataTimerClick");
		on("click", buttonStopwatch, "btnStopwatchClick");
	}
	
	public void btnTimerClick(View v){
		Toast.makeText(parent, "Timer Button clicked!", 
				Toast.LENGTH_SHORT).show();
	}
	
	public void btnTabataTimerClick(View v) throws Exception{
		dispatcher.showScreen("tabata.screens.tabata", new Object());
	}
	
	public void btnStopwatchClick(View v){
		Toast.makeText(parent, "Stopwatch clicked!", 
				Toast.LENGTH_SHORT).show();
	}
	
}
