Ext.define('IH.store.ValAvg', {
	extend : 'Ext.data.Store',
	model : 'IH.model.ValAvg',
	proxy : {
		type : 'ajax',
		api : {
		},
		reader : {
			type : 'json'
		}
	}
});