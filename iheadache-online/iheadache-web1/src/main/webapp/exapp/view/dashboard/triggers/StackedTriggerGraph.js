Ext.define('IH.view.dashboard.triggers.StackedTriggerGraph', {
			constructor : function( start, end, chunk) {
			
				var o =  Ext.create(
						'IH.view.dashboard.StackedChart',
						'/app/service/dashboard/stacked/triggers',
						start, end,chunk,
						'# of Triggers');
				
				Ext.apply(o, {
					widgetName : 'IH.view.dashboard.triggers.StackedTriggerGraph'
					});
				
				return o;


			}
});