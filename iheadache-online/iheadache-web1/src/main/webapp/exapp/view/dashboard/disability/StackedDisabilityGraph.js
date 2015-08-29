Ext.define('IH.view.dashboard.disability.StackedDisabilityGraph', {
			constructor : function( start, end, chunk) {
			
				var o =  Ext.create(
						'IH.view.dashboard.SimpleStackedChart',
						'/app/service/dashboard/stacked/disability',
						start, end,chunk,
						'Hours Disabled', 'Disability');
				
				Ext.apply(o, {
					widgetName : 'IH.view.dashboard.disability.StackedDisabilityGraph'
					});
				
				return o;

			}
});