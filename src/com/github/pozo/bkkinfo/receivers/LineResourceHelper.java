package com.github.pozo.bkkinfo.receivers;

import java.util.Locale;

import com.github.pozo.bkkinfo.R;
import com.github.pozo.bkkinfo.model.Line;

public class LineResourceHelper {
	public static int getResourceByType(String lineType) {
		if(lineType.equals(Line.Type.BUSZ.name().toLowerCase(Locale.getDefault()))) {
			return R.drawable.agazat_busz;
			
		} else if(lineType.equals(Line.Type.EJSZAKAI.name().toLowerCase(Locale.getDefault()))) {
			return R.drawable.agazat_ejszakai;
		} else if(lineType.equals(Line.Type.HAJO.name().toLowerCase(Locale.getDefault()))) {
			return R.drawable.agazat_hajo;
		} else if(lineType.equals(Line.Type.HEV.name().toLowerCase(Locale.getDefault()))) {
			return R.drawable.agazat_hev;
		} else if(lineType.equals(Line.Type.LIBEGO.name().toLowerCase(Locale.getDefault()))) {
			return R.drawable.agazat_libego;
		} else if(lineType.equals(Line.Type.METRO.name().toLowerCase(Locale.getDefault()))) {
			return R.drawable.agazat_metro;
		} else if(lineType.equals(Line.Type.SIKLO.name().toLowerCase(Locale.getDefault()))) {
			return R.drawable.agazat_siklo;
		} else if(lineType.equals(Line.Type.TROLIBUSZ.name().toLowerCase(Locale.getDefault()))) {
			return R.drawable.agazat_trolibusz;
		} else if(lineType.equals(Line.Type.VILLAMOS.name().toLowerCase(Locale.getDefault()))) {
			return R.drawable.agazat_villamos;
		} else {
			return R.drawable.agazat_busz;
		}
	}
}
