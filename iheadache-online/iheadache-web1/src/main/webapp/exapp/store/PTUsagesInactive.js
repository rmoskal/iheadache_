Ext.define('IH.store.PTUsagesInactive', {
	extend : 'Ext.data.Store',
	model : 'IH.model.PTUsage',
	requires : ['IH.model.PTUsage'],
    autoSync: true,
	autoLoad : true,
	proxy : {
		type : 'rest',
		url : '/app/service/ptusage/inactive',
		reader : {
			type : 'json'
		},
        writer: {
            type: 'json'
        }
	}
});
