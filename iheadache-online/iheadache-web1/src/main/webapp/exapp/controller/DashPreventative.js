Ext.define('IH.controller.DashPreventative', {
	extend : 'IH.controller.DashBase',
	models:['PTUsage'],
	stores: ['PTUsages'],
	views : [ 'dashboard.BackWindow', 'dashboard.InnerPanel',
			'dashboard.StackedHeadacheTypeGraph','dashboard.preventative.PTUsages' ],
	requires : [ 'IH.view.dashboard.StackedChart' ],
	
	
	renderMe : function() {
		
		if(Ext.ComponentQuery.query('#d_DashPreventative').length==1)
			return;
		Ext.create('IH.store.PTUsages');
		Ext.create('IH.view.dashboard.BackWindow', {
			id : 'd_DashPreventative',
			title : 'Preventative Treatments',
			renderTo : 'main-content',
			hideMode : 'offsets',
			items : this.initShow()

		});
		
		this.control({
			'#d_DashPreventative [action=refresh-dash]' : {
				click : this.refreshDash
			}

		});
		
		
	},

	init : function() {
		
		this.callParent(arguments);

	},
	reload : function(start, end, chunk) {
		
	    //this.getPTUsagesStore().load(start,end);
		return [
		        
			Ext.create('IH.view.dashboard.InnerPanel', {
					'title' : 'Number of Headaches by Type',
					items : [ Ext.create(
							'IH.view.dashboard.StackedHeadacheTypeGraph',
							start, end, chunk) ]
				}),  
				
				Ext.create('IH.view.dashboard.InnerPanel', {
					'title' : 'Preventative Treatments',
					height : 350,
					width : 700,
					items : [Ext.create(
							'IH.view.dashboard.preventative.PTUsages',
							start, end, chunk)]
				})  
				
				

		];

	}

});