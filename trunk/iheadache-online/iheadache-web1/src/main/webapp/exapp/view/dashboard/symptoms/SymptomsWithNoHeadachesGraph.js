Ext.define('IH.view.dashboard.symptoms.SymptomsWithNoHeadachesGraph', {
			constructor : function( start, end, chunk) {
				
				var o = Ext.create(
						'IH.view.dashboard.UnwoundChart','', '/app/service/dashboard/symptoms-no',start,
						end);
	
				
				Ext.apply(o, {
					widgetName : 'IH.view.dashboard.symptoms.SymptomsWithNoHeadachesGraph'
					});
				return o;

			}
});