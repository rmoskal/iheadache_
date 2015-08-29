Ext.define('IH.model.Treatment', {
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
						name : 'form',
						type : 'string',
						mapping : 'form'
					}, {
						name : 'description',
						type : 'string',
						mapping : 'description'
					}, {
						name : 'genericName',
						type : 'string',
						mapping : 'genericName'
					}, {
						name : 'uom',
						type : 'string',
						mapping : 'uom'
					}, {
						name : 'migraineTreatment',
						type : 'boolean',
						mapping : 'migraineTreatment'
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
