Ext.define('IH.controller.DashTrigger', {
	extend : 'IH.controller.DashBase',
	views : [ 'dashboard.BackWindow', 'dashboard.InnerPanel',
			'dashboard.StackedHeadacheTypeGraph','IH.view.dashboard.triggers.StackedTriggerGraph',
			'IH.view.dashboard.triggers.TriggersWithHeadachesGraph',
			'IH.view.dashboard.triggers.TriggersWithNoHeadachesGraph'],
	requires : [ 'IH.view.dashboard.StackedChart' ],
	
	renderMe : function() {
		
		if(Ext.ComponentQuery.query('#d_DashTrigger').length==1)
			return;
		Ext.create('IH.view.dashboard.BackWindow', {
			id : 'd_DashTrigger',
			title : 'Triggers',
			renderTo : 'main-content',
			hideMode : 'offsets',
			items : this.initShow()

		});

		this.control({
			   '#d_DashTrigger [action=refresh-dash]' : {
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
					'title' : 'Number of Headaches by Type',
					items : [ Ext.create(
							'IH.view.dashboard.StackedHeadacheTypeGraph',start, end, chunk) ]
				}),

				Ext.create('IH.view.dashboard.InnerPanel', {
					'title' : 'Headache Triggers (Top 5 During Timeframe)',
					items : [ Ext.create(
							'IH.view.dashboard.triggers.StackedTriggerGraph', start,end, chunk) ]
				}),
				Ext.create('IH.view.dashboard.InnerPanel', {
					'title' : 'Headache Triggers',
					height : 250,
					items : [ Ext.create(
							'IH.view.dashboard.triggers.TriggersWithHeadachesGraph',start,end) ]
				}),
				Ext.create('IH.view.dashboard.InnerPanel', {
					'title' : 'Triggers Reported Without Headache Pain',
					height : 250,
					items : [ Ext.create(
							'IH.view.dashboard.triggers.TriggersWithNoHeadachesGraph',start,end) ]
				})

		];

	}

});