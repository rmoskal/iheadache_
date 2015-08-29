Ext.define('IH.store.MidasChart', {
	extend : 'Ext.data.Store',
	model : 'IH.model.MidasChartItem',
	proxy : {
		type : 'ajax',
		api : {
			read : '/app/service/dashboard/midaschart'
		},
		reader : {
			type : 'json'
		}
	}
});
