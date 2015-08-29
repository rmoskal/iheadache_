Ext.define('IH.view.dashboard.pain.PainTypeGraph', {
			constructor : function( start, end, chunk) {
				
				var o = Ext.create(
						'IH.view.dashboard.UnwoundChart', '','/app/service/dashboard/pain-type',start,
						end);
	
				
				Ext.apply(o, {
					widgetName : 'IH.view.dashboard.pain.PainTypeGraph'
					});
				return o;

			}
});