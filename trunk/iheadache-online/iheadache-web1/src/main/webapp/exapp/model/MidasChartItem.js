Ext.define('IH.model.MidasChartItem', {
			extend : 'Ext.data.Model',
			fields : ['midas', {
						name : 'axis',
						type : 'date'
					}, "Actual", "Estimated"]
		});