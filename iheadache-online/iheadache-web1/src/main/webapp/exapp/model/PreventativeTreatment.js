Ext.define('IH.model.PreventativeTreatment', {
			extend : 'Ext.data.Model',
		    idgen: 'uuid',
			fields : [{
						name : 'id',
						type : 'string',
						mapping : 'id'
					}, {
						name : 'revision',
						type : 'string',
						mapping : '_rev'
					},
					{
						name : 'ptname',
						type : 'string',
						mapping : 'ptname'
					}, {
						name : 'genericName',
						type : 'string',
						mapping : 'genericName'
					}, {
						name : 'in',
						type : 'boolean',
						mapping : 'in'
					}, {
						name : 'custom',
						type : 'boolean',
						mapping : 'custom',
						defaultValue : false
					}]
		});
