Ext.define('IH.store.PTUsages', {
	extend : 'Ext.data.Store',
	model : 'IH.model.PTUsage',
	requires : ['IH.model.PTUsage'],
	//autoLoad : false,
	proxy : {
		type : 'ajax',
		api : {
			read : '/app/service/ptusage/all'
		},
		reader : {
			type : 'json',
			root: 'rows'
		}
	}
});
