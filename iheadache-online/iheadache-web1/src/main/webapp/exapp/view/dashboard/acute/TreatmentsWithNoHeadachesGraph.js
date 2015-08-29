Ext.define('IH.view.dashboard.acute.TreatmentsWithNoHeadachesGraph', {
			constructor : function( start, end, chunk) {
				
				var o = Ext.create(
						'IH.view.dashboard.UnwoundChart', '','/app/service/dashboard/acutetreatments-no',start,
						end);
	
				
				Ext.apply(o, {
					widgetName : 'IH.view.dashboard.acute.TreatmentsWithNoHeadachesGraph'
					});
				return o;

			}
});