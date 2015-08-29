Ext.define('IH.store.SymptomsProfile', {
	extend : 'Ext.data.Store',
	model : 'IH.model.ProfileItem',
	autoLoad : true,
	sorters : ['description'],

	proxy : {
		type : 'ajax',
		api : {
			read : '/app/service/profile/custom_symptoms'
		},
		reader : {
			type : 'json'
		}
	}

});
