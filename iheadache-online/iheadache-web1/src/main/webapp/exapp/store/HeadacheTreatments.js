 Ext.define('IH.store.HeadacheTreatments', {
			extend : 'Ext.data.Store',
			model : 'IH.model.HeadacheTreatment',
			requires : ['IH.model.HeadacheTreatment'],
			storeId : 'HeadacheTreatments',
			proxy : {
				type : 'memory',
				reader : {
					type : 'json'
				}
			}
		});