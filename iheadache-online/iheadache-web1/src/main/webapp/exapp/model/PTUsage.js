Ext.define('IH.model.PTUsage', {
			extend : 'Ext.data.Model',
			idProperty: '_id',
			fields : [{
						name : '_id',
						type : 'string',
						mapping : '_id'
					}, 
					{
						name : '_rev',
						type : 'string',
						mapping : '_rev'
					}, 
					{
						name:'user',
						type : 'string',
						mapping : 'user'
					},
					{
						name : 'start',
						type : 'date',
						dateFormat: 'time',
						mapping : 'start'
					}, {
						name : 'end',
						type : 'date',
						dateFormat: 'time',
						mapping : 'end'
					}, {
						name : 'stopReason',
						type : 'string',
						mapping : 'stopReason'
					}, {
						name : 'dose',
						type : 'string',
						mapping : 'dose'
					}, {
						name : 'treatmentDescription',
						type : 'string',
						mapping : 'treatmentDescription'
					},
					{
						name : 'genericDescription',
						type : 'string',
						mapping : 'genericDescription'
					},
					{
						name : 'treatmentId',
						type : 'string',
						mapping : 'treatmentId'
					}
					],
					
					 validations: [
					               {type: 'presence', field: 'start'}

					           ]
		});