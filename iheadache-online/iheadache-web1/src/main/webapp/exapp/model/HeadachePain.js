Ext.define('IH.model.HeadachePain', {
			extend : 'Ext.data.Model',
			fields : ['painType', 'painLocation', 'painTypeId', 'painLocationId',{
						name : 'level',
						type : 'double'
					}, {
						name : 'time',
						type : 'date',
						dateFormat: 'time'
					}]
		});