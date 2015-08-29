Ext.define('IH.store.DashCalendar', {
			extend : 'Ext.data.Store',
			model : 'IH.model.DashCalendar',
			autoLoad : false,
			proxy : {
				type : 'ajax',
				api : {
					read : '/app/service/dashboard/test_cal?startDate=2012-03-23'
				},
				reader : {
					type : 'json'
				}
			}
//			constructor : function(cfg) {
//				cfg = cfg || {};
//
//				var startDate = '';
//				if (cfg['_startDate'] != null) {
//
//					var startDate = cfg["_startDate"];
//
//					if (cfg['storeId'] == null)
//						cfg['storeId'] = startDate;
//
//					//startDate = '/for/' + end;
//				}
//
//				cfg['proxy'] = {
//					type : 'ajax',
//					api : {
//						read : '/app/service/dashboard/test_cal?startDate=' + startDate
//					},
//					reader : {
//						type : 'json',
//						record: 'calendarItem'
//					}
//				};
//
//				//this.callParent(arguments);
//				//this.initConfig(cfg);
//			}
		});