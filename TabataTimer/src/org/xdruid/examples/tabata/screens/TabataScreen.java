package org.xdruid.examples.tabata.screens;

import org.xdruid.examples.tabata.R;
import org.xdruid.ui.Dispatcher;
import org.xdruid.ui.SimpleScreen;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

public class TabataScreen extends SimpleScreen{
	
	//private View backButton;
	private EditText txtRoundsNumber;
	private EditText txtWorkSeconds;
	private EditText txtRestSeconds;
	public TabataScreen(Activity parent, 
			Dispatcher dispatcher, String name) {
		super(parent, dispatcher, name);
	}

	
	@Override
	protected void bindViews(Activity parent) throws Exception {
		//backButton = parent.findViewById(R.id.buttonBack);
		txtRoundsNumber = (EditText) parent.findViewById(R.id.txtRoundsNumber);
		txtWorkSeconds = (EditText)parent.findViewById(R.id.txtWorkLength);
		txtRestSeconds = (EditText)parent.findViewById(R.id.txtRestLength);
	}
	
	@Override
	protected void initializeViews(Activity parent, Dispatcher dispatcher,
			Object domainObject) throws Exception {
		txtRoundsNumber.setText("12");
		txtWorkSeconds.setText("20");
		txtRestSeconds.setText("10");
	}
	
	@Override
	protected void bindEvents() throws Exception {
		on("click",R.id.buttonBack,"btnBackClick");
	}
	
	@Override
	protected void bindLayouts() {
		bindLayout("tabata.tabata", R.layout.tabata);
	}
	
	
	public void screenReloading(Object domainObject) throws Exception {
	}

	public Object getValue() {
		return null;
	}

	@Override
	protected String getInitialLayout() {
		return "tabata.tabata";
	}

	
	public void btnBackClick(View view) throws Exception{
		trigger("xdruid.ui.history.back", "history.back", new Object());
	}
	
	
}
