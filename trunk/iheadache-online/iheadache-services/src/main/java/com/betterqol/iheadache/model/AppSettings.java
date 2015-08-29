package com.betterqol.iheadache.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AppSettings {

	public static enum DurationRules {NOT_REQUIRED,IGNORED, REQUIRED}

	public static enum SortRules {PROBABLE_TENSION,TENSION_PROBABLE}
	
	public static enum DrugRules {TRADE,GENERIC,BOTH}
	
	private DurationRules durationRule;
	private SortRules sortRule;
	private DrugRules drugRule;
	
	public AppSettings() {
		
		durationRule = DurationRules.NOT_REQUIRED;
		sortRule = SortRules.PROBABLE_TENSION;
		drugRule = DrugRules.BOTH;
		
	}

	public DurationRules getDurationRule() {
		return durationRule;
	}

	public void setDurationRule(DurationRules durationRule) {
		this.durationRule = durationRule;
	}

	public SortRules getSortRule() {
		return sortRule;
	}

	public void setSortRule(SortRules sortRule) {
		this.sortRule = sortRule;
	}

	public DrugRules getDrugRule() {
		return drugRule;
	}

	public void setDrugRule(DrugRules drugRule) {
		this.drugRule = drugRule;
	}
	
	

}
