Ext.define('IH.controller.DashCalendar', {
	extend : 'IH.controller.DashBase',
	views : [ 'dashboard.calendar.BackWindow', 'dashboard.InnerPanel','IH.view.dashboard.calendar.DashCalendar' ],
	stores: ['IH.store.DashCalendar'],
	models: ['IH.model.DashCalendar'],
	renderMe : function() {
		
		if(Ext.ComponentQuery.query('#d_DashCalendar').length==1)
			return;
		Ext.create('IH.view.dashboard.calendar.BackWindow', {
			id : 'd_DashCalendar',
			title : 'Monthly Calendars',
			renderTo : 'main-content',
			hideMode : 'offsets',
			items : this.initShow()

		});

		this.control({
			   '#d_DashCalendar [action=refresh-dash]' : {
				click : this.refreshDash
			}
		});

		
		
	},

	init : function() {

		this.callParent(arguments);

	},
	reload : function(start, end, chunk) {
		return [

				Ext.create(
							'IH.view.dashboard.calendar.DashCalendar',
							start, end, {html:'Nothing'})
			

		];

	}

});