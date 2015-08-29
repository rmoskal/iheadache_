Ext.define('IH.store.Treatments', {
	extend : 'Ext.data.Store',
	model : 'IH.model.Treatment',
	requires : ['IH.model.Treatment'],
	autoLoad:true,
	sorters : ['description', 'form', 'uom'],
	proxy : {
		type : 'ajax',
		api : {
			read : '/app/service/profile/treatments'
		},
		reader : {
			type : 'json'
		}
	}

});