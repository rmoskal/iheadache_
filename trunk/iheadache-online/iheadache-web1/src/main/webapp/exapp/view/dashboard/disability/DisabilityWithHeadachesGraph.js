Ext.define('IH.view.dashboard.disability.DisabilityWithHeadachesGraph', {
			constructor : function( start, end, chunk) {
				
				var o = Ext.create(
						'IH.view.dashboard.UnwoundChart', '','/app/service/dashboard/disability',start,
						end);
	
				
				Ext.apply(o, {
					widgetName : 'IH.view.dashboard.disability.DisabilityWithHeadachesGraph'
					});
				return o;

			}
});