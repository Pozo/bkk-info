package com.github.pozo.bkkinfo;

import com.github.pozo.bkkinfo.model.Entry;
import com.github.pozo.bkkinfo.services.NotificationService;
import com.github.pozo.bkkinfo.tasks.RetriveModelTask;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;

public class MainActivity extends Activity {
	private ModelReceiver modelReceiver = new ModelReceiver();
	
	private ToggleButtons toggleButtons;
	
	private TableLayout activeTable;
    private TableLayout soonTable;
    private TableLayout futureTable;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        toggleButtons = new ToggleButtons(this);
        
        activeTable = (TableLayout) findViewById(R.id.active_table);
        soonTable = (TableLayout) findViewById(R.id.soon_table);
        futureTable = (TableLayout) findViewById(R.id.future_table);
        
        IntentFilter intentFilter = new IntentFilter(ModelReceiver.BROADCAST_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(modelReceiver, intentFilter);

        startService(new Intent(this, NotificationService.class));
        new RetriveModelTask(this).execute();
    }
	public void addEntryToActiveTable(Entry entry) {
		new TableEntryRow(this, entry).add(activeTable);
    }
    public void addEntryToSoonTable(Entry entry) {
    	new TableEntryRow(this, entry).add(soonTable);
	
    }
    public void addEntryToFutureTable(Entry entry) {
    	new TableEntryRow(this, entry).add(futureTable);
    }
	public void changeFilterState(View view) {
		toggleButtons.changeFilterState(view);
	}
	public void setActiveTableLayoutEmpty() {
		new TableEntryRow(this).addEmpty(activeTable);
	}
	public void setSoonTableLayoutEmpty() {
		new TableEntryRow(this).addEmpty(soonTable);
	}
	public void setFutureTableLayoutEmpty() {
		new TableEntryRow(this).addEmpty(futureTable);
	}
	public void resetTables() {		
		activeTable.removeViews(1, activeTable.getChildCount()-1);
		soonTable.removeViews(1, soonTable.getChildCount()-1);
		futureTable.removeViews(1, futureTable.getChildCount()-1);
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
