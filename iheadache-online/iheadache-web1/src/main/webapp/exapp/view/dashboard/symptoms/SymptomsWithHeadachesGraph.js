Ext.define('IH.view.dashboard.symptoms.SymptomsWithHeadachesGraph', {
			constructor : function( start, end, chunk) {
				
				var o = Ext.create(
						'IH.view.dashboard.UnwoundChart','', '/app/service/dashboard/symptoms',start,
						end);
	
				
				Ext.apply(o, {
					widgetName : 'IH.view.dashboard.symptoms.SymptomsWithHeadachesGraph'
					});
				return o;

			}
});