Ext.define('IH.model.UserPrincipal', {
			extend : 'Ext.data.Model',
			fields : [
			        {
						name : 'firstName',
						type : 'string',
						mapping: 'firstName'
					}, 
			          {
						name : 'lastName',
						type : 'string',
						mapping: 'lastName'
					}, {
						name : 'name',
						type : 'string',
						mapping: 'name'
					},{
						name : 'address',
						type : 'string',
						mapping: 'address'
					},{
						name : 'city',
						type : 'string',
						mapping: 'city'
					},{
						name : 'state',
						type : 'string',
						mapping: 'state'
					},{
						name : 'country',
						type : 'string',
						mapping: 'country'
					},{
						name : 'birthdate',
						type : 'string',
						mapping: 'birthdate'
					},{
						name : 'gender',
						type : 'string',
						mapping: 'gender'
					},{
						name : 'zipcode',
						type : 'string',
						mapping: 'zipcode'
					},{
						name : 'id',
						type : 'string',
						mapping: 'id'
					}
					
					]
					
					
		});