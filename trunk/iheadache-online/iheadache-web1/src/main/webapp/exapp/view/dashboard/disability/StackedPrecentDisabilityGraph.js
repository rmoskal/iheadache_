Ext.define('IH.view.dashboard.disability.StackedPrecentDisabilityGraph', {
			constructor : function( start, end, chunk) {
			
				var o =  Ext.create(
						'IH.view.dashboard.StackedChart',
						'/app/service/dashboard/stacked/percent-disability',
						start, end,chunk,
						'Hours',  'Disability');
				
				Ext.apply(o, {
					widgetName : 'IH.view.dashboard.disability.StackedPrecentDisabilityGraph'
					});
				
				return o;

			}
});