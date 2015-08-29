Ext.define('IH.controller.DashAcute', {
	extend : 'IH.controller.DashBase',
	views : [ 'dashboard.BackWindow', 'dashboard.InnerPanel',
			'dashboard.acute.StackedAcuteGraph',
			'dashboard.StackedHeadacheTypeGraph' ,'dashboard.acute.TreatmentsWithHeadachesGraph',
			'dashboard.acute.TreatmentsWithNoHeadachesGraph'],
	requires : [ 'IH.view.dashboard.StackedChart','IH.view.dashboard.UnwoundChart' ],
	
	renderMe : function() {
		var id = 'd_DashAcute';
		if(Ext.ComponentQuery.query('#d_DashAcute').length==1)
			return;
		Ext.create('IH.view.dashboard.BackWindow', {
			id : 'd_DashAcute',
			title : 'Acute Treatments',
			renderTo : 'main-content',
			hideMode : 'offsets',
			items : this.initShow()

		});
		
		this.control({
			'#d_DashAcute [action=refresh-dash]' : {
				click :this.refreshDash
			} 

		});	
	},

	init : function() {
		this.callParent(arguments);
		
	},
	reload : function(start, end, chunk) {
		return [

				Ext.create('IH.view.dashboard.InnerPanel', {
					'title' : 'Number of Headaches by Type',
					items : [ Ext.create(
							'IH.view.dashboard.StackedHeadacheTypeGraph',
							start, end, chunk) ]
				}),

				Ext.create('IH.view.dashboard.InnerPanel', {
					'title' : 'Acute Treatments (Top 5 During Timeframe)',
					items : [ Ext.create(
							'IH.view.dashboard.acute.StackedAcuteGraph', start,
							end, chunk) ]
				}),
				Ext.create('IH.view.dashboard.InnerPanel', {
					'title' : 'Acute Headache Treatments',
					height : 250,
					items : [ Ext.create(
							'IH.view.dashboard.acute.TreatmentsWithHeadachesGraph',start,end) ]
				}),
				Ext.create('IH.view.dashboard.InnerPanel', {
					'title' : 'Treatments without Headache Pain',
					height : 250,
					items : [ Ext.create(
							'IH.view.dashboard.acute.TreatmentsWithNoHeadachesGraph',start,end) ]
				})

		];

	}

});