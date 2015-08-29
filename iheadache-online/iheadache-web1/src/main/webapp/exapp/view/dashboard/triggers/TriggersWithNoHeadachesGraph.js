Ext.define('IH.view.dashboard.triggers.TriggersWithNoHeadachesGraph', {
			constructor : function( start, end, chunk) {
				
				var o = Ext.create(
						'IH.view.dashboard.UnwoundChart','', '/app/service/dashboard/triggers-no',start,
						end);
	
				
				Ext.apply(o, {
					widgetName : 'IH.view.dashboard.triggers.TriggersWithNoHeadachesGraph'
					});
				return o;

			}
});