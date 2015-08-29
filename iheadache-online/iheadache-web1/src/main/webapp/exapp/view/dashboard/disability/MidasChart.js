Ext.define('IH.view.dashboard.disability.MidasChart', {
	constructor : function( start, end, chunk) {
		
		    var store = Ext.create('IH.store.MidasChart');
		    this.widgetName = 'IH.view.dashboard.disability.MidasChart';
			var o = Ext.create('Ext.chart.Chart',{
			store:store,
			width : 700,
			height : 200,
			animate : true,
			legend : {
				position : 'top'
			},
			axes : [{
						title : 'Disability Score',
						type : 'Numeric',
						position : 'left',
						fields : ['midas','Estimated','Actual'],
						minimum : 0
					}, {
						type : 'Category',
						position : 'bottom',
						fields : ['axis'],
						label : {
							renderer : Ext.util.Format
									.dateRenderer('M-y')
						}
					}],

			series : [/*{
						type : 'line',
						xField : 'axis',
						yField : 'midas',
				        smooth: true,
		                fill: true,
		                showInLegend : false,
		                markerConfig: {
	                        type: 'circle',
	                        radius: 0
	                        
	                    }

					}, */
					{
		                type: 'scatter',
		                axis: 'left',
		                xField: 'axis',
		                yField: 'Estimated',
		                displayName: 'Estimated',
		                markerConfig: {
		                    type: 'circle',
		                    size: 5
		                }
					},
					{
		                type: 'scatter',
		                axis: 'left',
		                xField: 'axis',
		                yField: 'Actual',
		                displayName: 'Actual',
		                markerConfig: {
		                    type: 'diamond',
		                    size: 5
		                }
					}
					]

		});
		o.widgetName = 'IH.view.dashboard.disability.MidasChart';
		store.load({ params: {'startDate': start, 'endDate': end, chunk:chunk},
		    callback : function(r, options, success) {
                var prevMax = o.axes.get("left").prevMax;
                if (prevMax <10) {
                	o.axes.get("left").maximum = 20;
                	o.redraw();
                }
            }});
		return o;
	}
});