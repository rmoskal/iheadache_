Ext.define('IH.view.profile.TriggerEditor', {

			extend : 'Ext.TabPanel',
			title : 'Trigger Profile',
			layout : 'fit',
			height : 500,
			width : 300,
			activeTab : 0,
			tools:[	{
						type:'help',
	    				tooltip: 'Get Help',
	    				handler: function(event, toolEl, panel){
	    				IH.app.fireEvent('show-help',"trigger_profile.html");}
					}
					],
			listeners : {
				show : {
					fn : function(e) {
						var store = Ext.StoreMgr.lookup('TriggersProfile');
						store.clearFilter();
						store.filter([{
							property : 'custom',
							value : false
						}]);
					}
				},
				

				tabchange: { fn: function (tp,newTab) {
					
					var store = Ext.StoreMgr.lookup('TriggersProfile');
					if (newTab.title == "Common") {
						store.clearFilter();
						store.filter([{
							property : 'custom',
							value : false
						}]);
					}
					else {
						
						store.clearFilter();
						store.filter([{
							property : 'custom',
							value : true
						}]);
						
					}
						
				}
					
				}
			},
			


			buttons : [

			{
						text : 'Ok',
						handler : function() {
							var store = Ext.StoreMgr.lookup("TriggersProfile");
							var filters = store.filters.clone();
							store.clearFilter();
							var payload = [];
							store.each( function(o){
									
									payload.push(o.data);
							});
							Ext.create('IH.core.PostEntity',"/app/service/profile/triggers",payload);
							IH.ClearStore(store); 
							store.filter(filters.items);
		
				
						}
					},
					
					   {
		                xtype: 'button',
		                text: 'Cancel',
		                handler: function() {
   		            		var grid = this.up('panel').activeTab;
 		            		grid.store.load();
 		            		grid.getView().refresh();
		                }
		            }
					],
	
	items : [
			         
			         {
						xtype : 'grid',
						title: 'Common',
						store : 'TriggersProfile',
						scroll :'vertical',
						selModel : {
							selType : 'cellmodel'
						},
						columns : [{
									xtype : 'checkcolumn',
									header : 'Track',
									flex : 1,
									sortable : true,
									dataIndex : 'in',
									editor : {
										xtype : 'checkbox',
										cls : 'x-grid-checkheader-editor'
									}
								}, {

									text : 'Common Triggers',
									flex : 2,
									sortable : true,
									dataIndex : 'description',
									field : {
										xtype : 'textfield'
									}

								}

						]
					},
					
					{
						xtype : 'grid',
						title: 'Custom',
						store : 'TriggersProfile',
						scroll :'vertical',
						selType: 'rowmodel',
						plugins : [Ext.create('Ext.grid.plugin.RowEditing', {
//							listeners : {
//								'edit' : function (editor, context, eOpts) { alert('edited'); }
//							}
						})],
						tbar : [{
							text : 'Add',
							handler : function() {
								var grid = this.up('panel');
								var rowEditing = grid.plugins[0];
								rowEditing.cancelEdit();

								var r = Ext.ModelManager.create({
											description : 'New Trigger',
											custom : true,
											in: true
										}, 'IH.model.ProfileItem');

								grid.store.insert(0, r);
								rowEditing.startEdit(0, 0);

							}
						},
						{
							text : 'Delete',
							handler : function() {

								var grid = this.up('panel');
								$.each(
										grid
										.getSelectionModel().getSelection(),
										function(i, o) {
											grid.store.removeAt(grid.store.indexOf(o));

												});

							}
						}
						],
						columns : [{
									xtype : 'checkcolumn',
									header : 'Track',
									flex : 1,
									sortable : true,
									dataIndex : 'in',
									editor : {
										xtype : 'checkbox',
										cls : 'x-grid-checkheader-editor'
									}
								},
								{
									header : 'Custom Triggers',
									sortable : true,
									flex:2,
									dataIndex : 'description',
									editor : {
										 xtype: 'textfield',
										allowBlank : false
									}
								}
								
						]
					}
],

initComponent : function(args) {
	this.callParent(arguments);
	var store = Ext.StoreMgr.lookup('TriggersProfile');
	store.clearFilter();
	store.filter([{
		property : 'custom',
		value : false
	}]);
	
}

		});