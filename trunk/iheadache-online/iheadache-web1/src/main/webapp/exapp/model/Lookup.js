Ext.define('IH.model.Lookup', {
			extend : 'Ext.data.Model',
			fields : [{
						name : 'id',
						type : 'string',
						mapping : 'id'
					}, {
						name : 'lookupType',
						type : 'string',
						mapping : 'lookupType'
					}, {
						name : 'description',
						type : 'string',
						mapping : 'description'
					}]
		});
