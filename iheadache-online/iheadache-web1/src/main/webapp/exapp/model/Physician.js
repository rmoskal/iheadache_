Ext.define('IH.model.Physician', {
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
						mapping : 'name'
					},
					{
						name : 'lastName',
					},
					{
						name : 'firstName',
					},
					{
						name : 'specialty',
						type : 'string',
						mapping : 'specialty',
						defaultValue : false
					}
					
					
					, {
						name : 'in',
						type : 'boolean',
						mapping : 'in'
					},
					{
						name : 'institution',
						type : 'string',
						mapping : 'institution',
						defaultValue : false
					},
					
					{
						name : 'state',
						type : 'string',
						mapping : 'state',
						defaultValue : false
					},
					{
						name : 'city',
						type : 'string',
						mapping : 'city',
						defaultValue : false
					},
					{
						name : 'zipcode',
						type : 'string',
						mapping : 'zipcode',
					}
					
					
					]
		});