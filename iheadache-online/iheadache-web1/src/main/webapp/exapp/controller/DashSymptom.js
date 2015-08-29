Ext.define('IH.controller.DashSymptom', {
	extend : 'IH.controller.DashBase',
	views : [ 'dashboard.BackWindow', 'dashboard.InnerPanel',
			'dashboard.StackedHeadacheTypeGraph','IH.view.dashboard.symptoms.StackedSymptomGraph',
			"IH.view.dashboard.symptoms.StackedCustomSymptomGraph",'IH.view.dashboard.symptoms.SymptomsWithHeadachesGraph',
			'IH.view.dashboard.symptoms.SymptomsWithHeadachesGraph'],
	requires : [ 'IH.view.dashboard.StackedChart', 'IH.view.dashboard.UnwoundChart' ],
	
	renderMe : function() {
		
		if(Ext.ComponentQuery.query('#d_DashSymptoms').length==1)
			return;
		var p =  Ext.create('IH.view.dashboard.BackWindow', {
			id : 'd_DashSymptoms',
			title : 'Symptoms',
			renderTo : 'main-content',
			hideMode : 'offsets',
			items : this.initShow()

		});

		this.control({
			   '#d_DashSymptoms [action=refresh-dash]' : {
				click : this.refreshDash
			}
		});		
		
	},

	init : function() {

		this.callParent(arguments);
	},
	reload : function(start, end, chunk) {
		return [

				Ext.create('IH.view.dashboard.InnerPanel', {
					title : 'Number of Headaches by Type',
					items : [ Ext.create(
							'IH.view.dashboard.StackedHeadacheTypeGraph',
							start, end, chunk) ]
				}),
				
				Ext.create('IH.view.dashboard.InnerPanel', {
					'title' : 'Headache Symptoms (Top 5 During Timeframe)',
					items : [ Ext.create(
							'IH.view.dashboard.symptoms.StackedSymptomGraph', start,end, chunk) ]
				}),
				Ext.create('IH.view.dashboard.InnerPanel', {
					'title' : 'Custom Headache Symptoms (Top 5 During Timeframe)',
					items : [ Ext.create(
							'IH.view.dashboard.symptoms.StackedCustomSymptomGraph', start,end, chunk) ]
				}),
				Ext.create('IH.view.dashboard.InnerPanel', {
					'title' : 'Headache Symptoms',
					height : 250,
					items : [ Ext.create(
							'IH.view.dashboard.symptoms.SymptomsWithHeadachesGraph',start,end) ]
				}),
				Ext.create('IH.view.dashboard.InnerPanel', {
					'title' : 'Symptoms Recorded Without Headache Pain',
					height : 250,
					items : [ Ext.create(
							'IH.view.dashboard.symptoms.SymptomsWithNoHeadachesGraph',start,end) ]
				})

		];

	}

});