Ext.define('IH.view.dashboard.symptoms.StackedCustomSymptomGraph', {
			constructor : function( start, end, chunk) {
			
				var o =  Ext.create(
						'IH.view.dashboard.StackedChart',
						'/app/service/dashboard/stacked/customsymptoms',
						start, end,chunk,
						'Custom Symptoms');
				
				Ext.apply(o, {
					widgetName : 'IH.view.dashboard.symptoms.StackedCustomSymptomGraph',
					});
				
				return o;


			}
});