Ext.define('IH.store.UserPrincipal', {
	extend : 'Ext.data.Store',
	model : 'IH.model.PTUsage',
	requires : ['IH.model.UserPrincipal'],
	//autoLoad : false,
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
