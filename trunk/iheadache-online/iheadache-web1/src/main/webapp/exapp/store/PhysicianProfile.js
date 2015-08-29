Ext.define('IH.store.PhysicianProfile', {
	extend : 'Ext.data.Store',
	model : 'IH.model.Physician',
	autoLoad : true,
//	sorters : ['description'],

	proxy : {
		type : 'ajax',
		api : {
			read : '/app/service/profile/associations'
		},
		reader : {
			type : 'json'
		}
	}

});
