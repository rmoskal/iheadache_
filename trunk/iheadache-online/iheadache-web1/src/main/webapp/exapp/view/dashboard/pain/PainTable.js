Ext.define('IH.view.dashboard.pain.PainTable', {

	constructor : function(start, end) {
				var store = Ext.create("IH.store.ValAvg");
				store.getProxy().url = "/app/service/dashboard/pain-location-table";
				store.load({
							params : {
								'startDate' : start,
								'endDate' : end
							}
						});
				
				var store2 = Ext.create("IH.store.ValAvg");
				store2.getProxy().url = "/app/service/dashboard/pain-type-table";
				store2.load({
							params : {
								'startDate' : start,
								'endDate' : end
							}
						});
				
				var o = Ext.create('Ext.Panel', {
					
					layout:'column',
					items:[
					       {xtype: "gridpanel",
					    	   
					    	   store : store,
					    	   columnWidth:.5,
								columns : [{
											header : 'Location',
											flex : 1,
											dataIndex : 'name'
										}, {
											header : 'Headaches',
											dataIndex : 'count'
										}, {
											header : 'Avg. Pain',
											dataIndex : 'avg',
											flex : 1
										}]
					       
					       },
					       
					       {xtype: "gridpanel",
					    	   
					    	   store : store2,
					    	   columnWidth:.5,
								columns : [{
											header : 'Type',
											flex : 1,
											dataIndex : 'name'
										}, {
											header : 'Headaches',
											dataIndex : 'count'
										}, {
											header : 'Avg. Pain',
											dataIndex : 'avg',
											flex : 1
										}]
					       
					       }
					       
					       
					       ]
					
					
					
				});
				
				Ext.apply(o, {
					widgetName : 'IH.view.dashboard.pain.PainTable',
					});
				
				return o;
			}

		});