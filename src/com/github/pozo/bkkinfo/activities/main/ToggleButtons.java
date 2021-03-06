package com.github.pozo.bkkinfo.activities.main;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.StateListDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.github.pozo.bkkinfo.R;
import com.github.pozo.bkkinfo.model.Line;
import com.github.pozo.bkkinfo.model.Line.Type;
import com.github.pozo.bkkinfo.tasks.RetriveModelTask;

public class ToggleButtons {
	private final MainActivity mainActivity;
	private final Context context;
	
    private int currentFilterState = 0;
	
    public ToggleButtons(MainActivity mainActivity) {
    	this.mainActivity = mainActivity;
    	this.context = mainActivity.getBaseContext();
    	
    	createToggleButtons();
    }

    private void createToggleButtons() {
		LinearLayout tableRow = (LinearLayout) mainActivity.findViewById(R.id.toggle_buttons);
		
		for (Type type : Type.values()) {
			if(type.isShowable()) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				ToggleButton tb = (ToggleButton) inflater.inflate(R.layout.toggle_button, null);
				tb.setTag(type.name());
				tb.setBackgroundDrawable(getBackgroundDrawableByType(tb));
				
				tableRow.addView(tb);				
			}

		}
	}
	private StateListDrawable getBackgroundDrawableByType(ToggleButton toggleButton) {
		StateListDrawable states = new StateListDrawable();

		
    	states.addState(new int[] {
    		android.R.attr.state_checked},
    		mainActivity.getResources().getDrawable(getResourceId(toggleButton.getTag(),android.R.attr.state_checked)));
    	
    	states.addState(new int[] {},
    		mainActivity.getResources().getDrawable(getResourceId(toggleButton.getTag(),android.R.attr.state_empty)));
		return states;
	}
	private int getResourceId(Object object, int state) {
		if(object.equals(Type.VILLAMOS.name())) {
			return getResourceIdByState(state,R.drawable.image6158,R.drawable.image6157);

		} else if(object.equals(Type.BUSZ.name())) {
			return getResourceIdByState(state,R.drawable.image6162,R.drawable.image6161);

		} else if(object.equals(Type.HEV.name())) {
			return getResourceIdByState(state,R.drawable.image6154,R.drawable.image6153);
			
		} else if(object.equals(Type.METRO.name())) {
			return getResourceIdByState(state,R.drawable.image6119,R.drawable.image6118);

		} else if(object.equals(Type.TROLIBUSZ.name())) {
			return getResourceIdByState(state,R.drawable.image6159,R.drawable.image6160);
			
		} else if(object.equals(Type.HAJO.name())) {
			return getResourceIdByState(state,R.drawable.image6155,R.drawable.image6156);

		} else if(object.equals(Type.EJSZAKAI.name())) {
			return getResourceIdByState(state,R.drawable.image6120,R.drawable.image6121);
			
		} else {
			return getResourceIdByState(state,R.drawable.image6158,R.drawable.image6157);
		}
	}
	private int getResourceIdByState(int state, int resourceIfChecked, int resourceIfUNChecked) {
		if(state == android.R.attr.state_checked) {
			return resourceIfChecked;				
		} else {
			return resourceIfUNChecked;
		}	
	}
	public void changeFilterState(View view) {
		ToggleButton toggleButton = (ToggleButton) view;

		boolean on = toggleButton.isChecked();
		
		String tag = (String) toggleButton.getTag();
		
		if(tag.equals(Type.METRO.name())) {
			if(on){
				currentFilterState |= Line.Type.METRO.getFlagValue();
			} else {
				currentFilterState -= Line.Type.METRO.getFlagValue();
			}
		} else if(tag.equals(Type.HEV.name())) {
			if(on){
				currentFilterState |= Line.Type.HEV.getFlagValue();
			} else {
				currentFilterState -= Line.Type.HEV.getFlagValue();
			}
		} else if(tag.equals(Type.HAJO.name())) {
			if(on){
				currentFilterState |= Line.Type.HAJO.getFlagValue();
			} else {
				currentFilterState -= Line.Type.HAJO.getFlagValue();
			}
		} else if(tag.equals(Type.VILLAMOS.name())) {
			if(on){
				currentFilterState |= Line.Type.VILLAMOS.getFlagValue();
			} else {
				currentFilterState -= Line.Type.VILLAMOS.getFlagValue();
			}
		} else if(tag.equals(Type.TROLIBUSZ.name())) {
			if(on){
				currentFilterState |= Line.Type.TROLIBUSZ.getFlagValue();
			} else {
				currentFilterState -= Line.Type.TROLIBUSZ.getFlagValue();
			}
		} else if(tag.equals(Type.BUSZ.name())) {
			if(on){
				currentFilterState |= Line.Type.BUSZ.getFlagValue();
			} else {
				currentFilterState -= Line.Type.BUSZ.getFlagValue();
			}
		} else if(tag.equals(Type.EJSZAKAI.name())) {
			if(on){
				currentFilterState |= Line.Type.EJSZAKAI.getFlagValue();
			} else {
				currentFilterState -= Line.Type.EJSZAKAI.getFlagValue();
			}
		} 
		new RetriveModelTask(mainActivity, false).execute();
	}
	public boolean hasFlag(Line.Type lineType) {
		return (currentFilterState & lineType.getFlagValue()) != 0x0;
	}
	public ArrayList<String> getFilteredLines() {
		ArrayList<String> retval = new ArrayList<String>();
		
		for (Type type : Line.Type.values()) {
			if(hasFlag(type)) {
				retval.add(type.name());
			}
		}
	
		return retval;
	}
	public int getCurrentFilterState() {
		return currentFilterState;
	}

	public void setCurrentFilterState(int currentFilterState) {
		this.currentFilterState = currentFilterState;
	}
	public boolean isFilteredLinesEmpty() {
		return getFilteredLines().isEmpty();
	}
}
