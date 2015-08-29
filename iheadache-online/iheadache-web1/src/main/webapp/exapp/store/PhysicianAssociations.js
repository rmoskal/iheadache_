Ext.define('IH.store.PhysicianAssociations', {
	extend : 'Ext.data.Store',
	model : 'IH.model.UserPrincipal',
	autoLoad : true,

	proxy : {
		type : 'ajax',
		api : {
			read : '/app/service/hcp/associations'
		},
		reader : {
			type : 'json'
		}
	}

});
