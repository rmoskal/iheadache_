Ext.define('IH.store.DailyHeadaches', {
			extend : 'Ext.data.Store',
			model : 'IH.model.HeadacheShort',
			requires : ['IH.model.HeadacheShort'],
			storeId:'DailyHeadaches',
			proxy : {
				type : 'memory',
				reader : {
					type : 'json'
				}
			}

		});
