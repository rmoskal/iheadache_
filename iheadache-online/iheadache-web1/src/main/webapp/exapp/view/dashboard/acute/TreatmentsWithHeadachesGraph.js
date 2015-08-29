Ext.define('IH.view.dashboard.acute.TreatmentsWithHeadachesGraph', {
			constructor : function( start, end, chunk) {
				
				var o = Ext.create(
						'IH.view.dashboard.UnwoundChart', '','/app/service/dashboard/acutetreatments',start,
						end);
	
				
				Ext.apply(o, {
					widgetName : 'IH.view.dashboard.acute.TreatmentsWithHeadachesGraph'
					});
				return o;

			}
});