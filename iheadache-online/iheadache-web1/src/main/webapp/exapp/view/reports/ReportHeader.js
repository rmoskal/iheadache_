Ext.define('IH.view.reports.ReportHeader', {
	extend : 'Ext.form.Panel',
	alias : 'widget.report_header',
	width : 700,
	height : 140,
	bodyPadding : 10,
	items : [  {
		xtype : 'fieldset',
		title : "Include",
		items : [ {
			xtype : 'checkboxgroup',
			columns : 3,
			vertical : true,
			items : [ {
				boxLabel : 'Headache Type',
				name : 'rb',
				inputValue : 'headache_types',
				checked : true
			}, {
				boxLabel : 'Time Disabled',
				name : 'rb',
				inputValue : 'disability',
				checked : true
			}, {
				boxLabel : 'Triggers',
				name : 'rb',
				inputValue : 'triggers',
				checked : true
			}, {
				boxLabel : 'Acute Treatments',
				name : 'rb',
				inputValue : 'treats',
				checked : true
			},{
				boxLabel : 'Disability Score',
				name : 'rb',
				inputValue : 'midas_scores',
				checked : true
			}, 
			{
				boxLabel : 'Notes',
				name : 'rb',
				inputValue : 'notes',
				checked : true
			},{
				boxLabel : 'Preventative Treatments',
				name : 'rb',
				inputValue : 'p_treats',
				checked : true
			} ]
		} ]
	} ]
});