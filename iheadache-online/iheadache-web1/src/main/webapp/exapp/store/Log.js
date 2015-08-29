Ext.define('IH.store.Log', {
			extend : 'Ext.data.Store',
			model : 'IH.model.LogEntry',
			requires : ['IH.model.LogEntry'],
			//autoLoad:true,
			// autoSync: true,

			proxy : {
				type : 'ajax',
				api : {
					read : '/app/service/log/paged'
				},
				reader : {
					root: 'payload',
					type : 'json'
				}
			}

		});
