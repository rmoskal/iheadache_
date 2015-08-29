Ext.define('IH.view.dashboard.disability.DisabilityWithNoHeadachesGraph', {
			constructor : function( start, end, chunk) {
				
				var o = Ext.create(
						'IH.view.dashboard.UnwoundChart', '','/app/service/dashboard/disability-no',start,
						end);
	
				
				Ext.apply(o, {
					widgetName : 'IH.view.dashboard.disability.DisabilityWithNoHeadachesGraph'
					});
				return o;

			}
});