Ext.define('IH.view.dashboard.acute.StackedAcuteGraph', {
			constructor : function( start, end, chunk) {
	
				var o = Ext.create(
						'IH.view.dashboard.StackedChart',
						'/app/service/dashboard/stacked/acutetreatments',
						start, end,chunk,
						'# of Treatments', "Blue");
				
				Ext.apply(o, {
					widgetName : 'IH.view.dashboard.acute.StackedAcuteGraph'
					});
				
				return o;

			}
});