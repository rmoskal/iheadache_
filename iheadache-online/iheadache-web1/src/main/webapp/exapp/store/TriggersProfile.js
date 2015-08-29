Ext.define('IH.store.TriggersProfile', {
	extend : 'Ext.data.Store',
	model : 'IH.model.ProfileItem',
	autoLoad : true,
	sorters : ['description'],
	// autoSync: true,

	proxy : {
		type : 'ajax',
		api : {
			read : '/app/service/profile/triggers'
		},
		reader : {
			type : 'json'
		}
	}

});
