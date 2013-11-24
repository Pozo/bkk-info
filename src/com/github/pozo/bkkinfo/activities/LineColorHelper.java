package com.github.pozo.bkkinfo.activities;

import java.util.Locale;

import android.content.Context;
import android.graphics.Color;

import com.github.pozo.bkkinfo.R;
import com.github.pozo.bkkinfo.model.Line;

public class LineColorHelper {
	public static int getColorByNameAndType(Context context, String lineName, String lineType) {
		if(lineType.equals(Line.Type.BUSZ.name().toLowerCase(Locale.getDefault()))) {
			return context.getResources().getColor(R.color.blue);	
			
		} else if(lineType.equals(Line.Type.VILLAMOS.name().toLowerCase(Locale.getDefault()))) {
			return context.getResources().getColor(R.color.yellow);
			
		} else if(lineType.equals(Line.Type.TROLIBUSZ.name().toLowerCase(Locale.getDefault()))) {
			return context.getResources().getColor(R.color.red);
			
		} else if(lineType.equals(Line.Type.HEV.name().toLowerCase(Locale.getDefault()))) {
			if(lineName.equals("H5")) {
				return context.getResources().getColor(R.color.purple);
			} else if(lineName.equals("H6")) {
				return context.getResources().getColor(R.color.h6);
			} else if(lineName.equals("H7")) {
				return context.getResources().getColor(R.color.h7);
			} else if(lineName.equals("H8") || lineName.equals("H9")) {
				return context.getResources().getColor(R.color.h8);
			} else {
				return context.getResources().getColor(R.color.purple);
			}
			
			
		} else if(lineType.equals(Line.Type.HAJO.name().toLowerCase(Locale.getDefault()))) {
			if(lineName.equals("D11")) {
				return context.getResources().getColor(R.color.d11);
			} else if(lineName.equals("D12")) {
				return context.getResources().getColor(R.color.d12);
			} else if(lineName.equals("D13")) {
				return context.getResources().getColor(R.color.d13);
			} else {
				return context.getResources().getColor(R.color.d11);
			}
		} else if(lineType.equals(Line.Type.EJSZAKAI.name().toLowerCase(Locale.getDefault()))) {
			return Color.BLACK;
			
		} else if(lineType.equals(Line.Type.SIKLO.name().toLowerCase(Locale.getDefault()))) {
			return context.getResources().getColor(R.color.siklo);
			
		} else if(lineType.equals(Line.Type.LIBEGO.name().toLowerCase(Locale.getDefault()))) {
			return context.getResources().getColor(R.color.libego);
			
		} else if(lineType.equals(Line.Type.METRO.name().toLowerCase(Locale.getDefault()))) {
			if(lineName.equals("M1")) {
				return context.getResources().getColor(R.color.yellow);
			} else if(lineName.equals("M2")) {
				return context.getResources().getColor(R.color.red);
			} else if(lineName.equals("M3")) {
				return context.getResources().getColor(R.color.blue);
			} else {
				return context.getResources().getColor(R.color.blue);
			}
			
		} else {
			return context.getResources().getColor(R.color.blue);
		}
		
	}
	public static int getTextColorByType(String lineType, String lineName) {
		if(lineType.equals(Line.Type.VILLAMOS.name().toLowerCase(Locale.getDefault()))) {
			return Color.BLACK;
		} else if(lineType.equals(Line.Type.METRO.name().toLowerCase(Locale.getDefault()))) {
			if(lineName.equals("M1")) {
				return Color.BLACK;
			} else {
				return Color.WHITE;
			}
		} else {
			return Color.WHITE;
		}
	}
}
