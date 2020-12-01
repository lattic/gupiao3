package com.example.chart.entity;

import java.util.List;

import com.example.chart.base.entity.Entry;

/**
 * Created by Rex on 2018/11/14.
 */
public class EMAEntity implements ChartEntity {

    private List<Entry> emaList1;
    private List<Entry> emaList2;

    public EMAEntity(List<Entry> emaList1, List<Entry> emaList2) {
		super();
		this.emaList1 = emaList1;
		this.emaList2 = emaList2;
	}


	public List<Entry> getEmaList1() {
		return emaList1;
	}


	public void setEmaList1(List<Entry> emaList1) {
		this.emaList1 = emaList1;
	}


	public List<Entry> getEmaList2() {
		return emaList2;
	}


	public void setEmaList2(List<Entry> emaList2) {
		this.emaList2 = emaList2;
	}


	@Override
    public void clearValues() {
        if (emaList1 != null) {
        	emaList1.clear();
        }
        if (emaList2 != null) {
        	emaList2.clear();
        }
    }

}
