Ext.define('IH.view.dashboard.symptoms.StackedSymptomGraph', {
			constructor : function( start, end, chunk) {
			
				var o =  Ext.create(
						'IH.view.dashboard.StackedChart',
						'/app/service/dashboard/stacked/symptoms',
						start, end,chunk,
						'Standard Symptom');
				
				Ext.apply(o, {
					widgetName : 'IH.view.dashboard.symptoms.StackedSymptomGraph'
					});
				
				return o;


			}
});