Ext.define('IH.view.dashboard.preventative.PTUsages', {

	constructor : function(start, end) {
				var store = Ext.create("IH.store.PTUsages");
				store.load({
							params : {
								'startDate' : start,
								'endDate' : end
							}
						});

				var o =  Ext.create('Ext.grid.Panel', {
							store : store,
							columns : [{
										header : 'Name',
										flex : 1,
										dataIndex : 'treatmentDescription',
										renderer:function(value, meta, rec) {
											var drugRule = IH.user.appSettings.drugRule;
											if (drugRule == "BOTH") 
												return rec.data.treatmentDescription + "/" + rec.data.genericDescription;
											if (drugRule == "GENERIC") 
												return rec.data.genericDescription;
											return rec.data.treatmentDescription;
										},
									}, {
										header : 'Dose',
										dataIndex : 'dose'
									}, {
										header : 'Start Date',
										dataIndex : 'start',
										flex : 1,
										renderer : Ext.util.Format
												.dateRenderer('Y-M-d')
									}, {
										header : 'Stop Date',
										dataIndex : 'end',
										flex : 1,
										renderer : Ext.util.Format
												.dateRenderer('Y-M-d')
									}, {
										header : 'Reason for Stopping',
										flex : 2,
										dataIndex : 'stopReason'
									}]
						});
				
				Ext.apply(o, {
					widgetName : 'IH.view.dashboard.preventative.PTUsages',
					});
				
				return o;
			}

		});