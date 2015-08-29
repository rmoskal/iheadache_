Ext.define('IH.store.UnwoundChart', {
	extend : 'Ext.data.Store',
	model : 'IH.model.NVPair',
	proxy : {
		type : 'ajax',
		api : {
		},
		reader : {
			type : 'json'
		}
	}
});