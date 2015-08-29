Ext.define('IH.view.dashboard.triggers.TriggersWithHeadachesGraph', {
			constructor : function( start, end, chunk) {
				
				var o = Ext.create(
						'IH.view.dashboard.UnwoundChart','', '/app/service/dashboard/triggers',start,
						end);
	
				
				Ext.apply(o, {
					widgetName : 'IH.view.dashboard.triggers.TriggersWithHeadachesGraph'
					});
				return o;

			}
});