Ext.define('IH.view.dashboard.StackedHeadacheTypeGraph', {
			constructor : function( start, end, chunk) {
			
				var o =  Ext.create(
						'IH.view.dashboard.StackedChart',
						'/app/service/dashboard/stacked/headachetype',
						start, end,chunk,
						'# of Headaches','Headache');
				
				Ext.apply(o, {
					widgetName : 'IH.view.dashboard.StackedHeadacheTypeGraph'
					});
					
				return o;

			}
});