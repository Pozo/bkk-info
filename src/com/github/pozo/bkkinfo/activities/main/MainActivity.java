package com.github.pozo.bkkinfo.activities.main;

import java.util.ArrayList;
import java.util.HashMap;

import com.github.pozo.bkkinfo.R;
import com.github.pozo.bkkinfo.activities.notificationsettings.NotificationSettingsActivity;
import com.github.pozo.bkkinfo.model.Entry;
import com.github.pozo.bkkinfo.model.Model.Type;
import com.github.pozo.bkkinfo.tasks.RetriveModelTask;
import com.github.pozo.bkkinfo.utils.Constants;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;

public class MainActivity extends Activity {
	private ToggleButtons toggleButtons;
	
	private final HashMap<Type, TableLayout> tables = new HashMap<Type, TableLayout>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        toggleButtons = new ToggleButtons(this);

        tables.put(Type.ACTIVE, (TableLayout) findViewById(R.id.active_table));
        tables.put(Type.SOON, (TableLayout) findViewById(R.id.soon_table));
        tables.put(Type.FUTURE, (TableLayout) findViewById(R.id.future_table));
    }
    
	@Override
	protected void onResume() {
		super.onResume();
		Log.i(Constants.LOG_TAG, "MainActivity:onResume");
		new RetriveModelTask(this, false).execute();
	}
	private void addEntryToTable(Type type, Entry entry) {
		new TableEntryRow(this, entry).add(tables.get(type));
	}
	public void changeFilterState(View view) {
		toggleButtons.changeFilterState(view);
	}
	private void truncateTable(Type type) {
		new TableEntryRow(this).addEmpty(tables.get(type));
	}

	public void resetTables() {
		for (java.util.Map.Entry<Type, TableLayout> tableLayout : tables.entrySet()) {
			TableLayout table = tableLayout.getValue();
			table.removeViews(1, table.getChildCount()-1);
		}
	}
	public void fillTable(Type type, ArrayList<Entry> filteredEntries) {
		if (filteredEntries.isEmpty()) {
			truncateTable(type);
		} else {
			for (Entry entry : filteredEntries) {
				addEntryToTable(type, entry);
			}	
		}
	}
	public ToggleButtons getToggleButtons() {
		return toggleButtons;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.refresh:
	        	new RetriveModelTask(this, true).execute();
	            return true;
	        case R.id.alarms:
	    		String packageName = getPackageName();
	    		
	    		Intent notificationSettingsActivity = new Intent(this, NotificationSettingsActivity.class);
	    		notificationSettingsActivity.setPackage(packageName);
	    		notificationSettingsActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    		
	    		startActivity(notificationSettingsActivity);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
