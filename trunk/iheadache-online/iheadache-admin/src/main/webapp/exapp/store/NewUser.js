Ext.define('IH.store.NewUser', {
			extend : 'Ext.data.Store',
			model : 'IH.model.UserPrincipal',
			requires : ['IH.model.UserPrincipal'],
			autoLoad:true,

			proxy : {
				type : 'ajax',
				 headers: { 'Accept': 'application/json' },
				api : {
					read : '/app/service/admin/hcp-unapproved'
				},
				reader : {
					root: 'payload',
					type : 'json'
				}
			}

		});