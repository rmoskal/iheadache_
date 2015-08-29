Ext.define('IH.store.PTreatmentProfile', {
	extend : 'Ext.data.Store',
	model : 'IH.model.PreventativeTreatment',
	autoLoad : true,
	sorters : ['ptname'],
	// autoSync: true,

	proxy : {
		type : 'ajax',
		api : {
			read : '/app/service/profile/ptreatments'
		},
		reader : {
			type : 'json'
		}
	}

});
