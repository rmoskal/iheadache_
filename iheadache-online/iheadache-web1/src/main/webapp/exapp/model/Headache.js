Ext.define('IH.model.Headache', {
			extend : 'Ext.data.Model',
			fields : [{
						name : 'id',
						type : 'string',
						mapping : 'value._id'
					}, {
						name : 'start',
						type : 'date',
						mapping : 'value.start'
					}, {
						name : 'end',
						type : 'date',
						mapping : 'value.end'
					},
					{
						name: 'status',
						type:'string'
					}]
		});