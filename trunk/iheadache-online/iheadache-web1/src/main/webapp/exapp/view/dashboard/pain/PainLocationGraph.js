Ext.define('IH.view.dashboard.pain.PainLocationGraph', {
			constructor : function( start, end, chunk) {
				
				var o = Ext.create(
						'IH.view.dashboard.UnwoundChart', '','/app/service/dashboard/pain-location',start,
						end);
	
				
				Ext.apply(o, {
					widgetName : 'IH.view.dashboard.pain.PainLocationGraph'
					});
				return o;

			}
});