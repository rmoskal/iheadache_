Ext.define('IH.view.dashboard.StackedHeadacheDaysGraph', {
			constructor : function( start, end, chunk) {
			
				var o =  Ext.create(
						'IH.view.dashboard.SimpleStackedChart',
						'/app/service/dashboard/stacked/headachedays',
						start, end,chunk,
						'Days',"Headache");
				
				Ext.apply(o, {
					widgetName : 'IH.view.dashboard.StackedHeadacheDaysGraph'
					});
					
				return o;

			}
});