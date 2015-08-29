Ext.define('IH.store.Lookups', {
			extend : 'Ext.data.Store',
			model : 'IH.model.Lookup',
			autoLoad : true,
			requires : ['IH.model.Lookup'],

			constructor : function(cfg) {
				cfg = cfg || {};

				var end = '';
				if (cfg['_kind'] != null) {

					end = cfg["_kind"];

					if (cfg['storeId'] == null)
						cfg['storeId'] = end;

					end = '/for/' + end;
				}

				cfg['proxy'] = {
					type : 'ajax',
					api : {
						read : '/app/service/lookups' + end
					},
					reader : {
						type : 'json'
					}
				};

				this.callParent(arguments);
				this.initConfig(cfg);
			}
		});