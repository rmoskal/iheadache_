Ext.define('IH.view.profile.PhysicianChooser', {

			extend : 'Ext.Window',
			title : 'Find a Physician',
			alias : 'widget.physicianchooser',
			layout : 'fit',
			autoShow : true,
			height : 600,
			width : 800,
			draggable: false,
			modal : true,
			listeners:{
				
				beforeclose: function( panel, opts ) {
					var store = Ext.StoreMgr.lookup('PhysicianProfile');
					store.clearFilter();
					store.filter([{
						property : 'in',
						value : true
					}]);
				}
				
			},
			
			
			
			items:     [{
				xtype: "LiveSearchGridPanel",
		        store: "PhysicianProfile",
		        columnLines: true,
		        columns: [{
					text : 'Name',
					flex : 2,
					sortable : true,
					dataIndex : 'lastName',
					renderer: function(value, metaData, record, rowIndex, colIndex, store) {
						return record.get("lastName") + ", " + record.get("firstName");
					}
					
				}, 
				{
					text : 'Specialty',
					flex : 1,
					sortable : true,
					dataIndex : 'specialty'
				},
				
				{
					text : 'Institution',
					flex : 2,
					sortable : true,
					dataIndex : 'institution'
				},
				{
					text : 'State',
					flex : 1,
					sortable : true,
					dataIndex : 'state'
				},
				
				{
					text : 'City',
					flex : 2,
					sortable : true,
					dataIndex : 'city'
				},
				{
					text : 'Zipcode',
					flex : 2,
					sortable : true,
					dataIndex : 'zipcode'
				},
				{
					xtype:'actioncolumn',
					flex : 1,
					items: [{
						icon: 'resources/images/icons/icon-Add.png',
						tooltip: 'Add',
						handler: function (dataview, rowIndex, colIndex, item, e, rec, row) {
							var phyRec = dataview.store.getAt(rowIndex);
							phyRec.set("in", true);
							phyRec.commit();
							dataview.store.clearFilter();
							dataview.store.filter([{
								property : 'in',
								value : false
							}]);
						}

					}]
				}
				
				],
				height : 600,
				width : 800,
		        viewConfig: {
		            stripeRows: true,
		            listeners: {
		            	itemdblclick: function(dataview, item, index, e) {
		            		item.set("in", true);
		            		item.commit();
		            		dataview.store.clearFilter();
		            		dataview.store.filter([{
		    					property : 'in',
		    					value : true
		    				}]);
		            		dataview.up('window').close();
		                     },
		               beforerender: function ( grid, opts ){
		            	   grid.store.clearFilter();
		            	   grid.store.filter([{
		    					property : 'in',
		    					value : false
		    				}]);
		            	   
		               }
		                 }
		        }
		    }]

});