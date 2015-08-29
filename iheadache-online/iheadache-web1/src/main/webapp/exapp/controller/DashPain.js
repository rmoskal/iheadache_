Ext.define('IH.controller.DashPain', {
	extend : 'IH.controller.DashBase',
	views : [ 'dashboard.BackWindow', 'dashboard.InnerPanel',
			'dashboard.acute.StackedAcuteGraph',
			'dashboard.StackedHeadacheTypeGraph','IH.view.dashboard.pain.PainTypeGraph',
			'IH.view.dashboard.pain.PainLocationGraph','IH.view.dashboard.pain.PainTable'
			],
	requires : [ 'IH.view.dashboard.StackedChart' ],
	
	renderMe : function() {
		if(Ext.ComponentQuery.query('#d_DashPain').length==1)
			return;
		Ext.create('IH.view.dashboard.BackWindow', {
			id : 'd_DashPain',
			title : 'Pain',
			renderTo : 'main-content',
			hideMode : 'offsets',
			items : this.initShow()

		});
		
		this.control({
			'#d_DashPain [action=refresh-dash]' : {
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
					title : 'Number of Headaches by Type',
					items : [ Ext.create(
							'IH.view.dashboard.StackedHeadacheTypeGraph',
							start, end, chunk) ]
				}),
				
				Ext.create('IH.view.dashboard.InnerPanel', {
					title : 'Pain Location',
					height : 250,
					items : [ Ext.create(
							'IH.view.dashboard.pain.PainLocationGraph', start,end) ]
				}),
				Ext.create('IH.view.dashboard.InnerPanel', {
					title : 'Pain Type',
					height : 250,
					items : [ Ext.create(
							'IH.view.dashboard.pain.PainTypeGraph',start,end) ]
				}),
				Ext.create('IH.view.dashboard.InnerPanel', {
					'title' : 'Pain Details',
					height : 250,
					items :  [Ext.create(
							'IH.view.dashboard.pain.PainTable',
							start, end)] 
				})
				
				

		];

	}

});