Ext.define('IH.model.ProfileItem', {
			extend : 'Ext.data.Model',
			requires: ['Ext.data.UuidGenerator'],
		    idgen: 'uuid',
			fields : [{
						name : 'id',
						type : 'string',
						mapping : 'id'
					}, {
						name : 'description',
						type : 'string',
						mapping : 'description'
					}, {
						name : 'in',
						type : 'boolean',
						mapping : 'in'
					},
					{
						name : 'custom',
						type : 'boolean',
						mapping : 'custom',
						defaultValue : false
					}]
		});