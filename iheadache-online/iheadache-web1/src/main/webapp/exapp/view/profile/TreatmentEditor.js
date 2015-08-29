Ext.define('IH.view.profile.TreatmentEditor', {

	extend : 'Ext.TabPanel',
	title : 'Acute Treatment Profile',
	layout : 'vbox',
	height : 480,
	width : 700,
	autoscroll : true,
	activeTab : 0,
	tools:[	{
						type:'help',
	    				tooltip: 'Get Help',
	    				handler: function(event, toolEl, panel){
	    				IH.app.fireEvent('show-help',"treatment_profile.html");}
				}
			],
	items : [          
	{
  	  	bbar : [{
					text : 'The presence of a medication in this list does not constitute an endorsement for its use.',
					iconCls : 'incompleteStatus'
				}],

		xtype : 'grid',
		title : "Common",
		store : 'treatments',
		listeners : {
			show : {
		fn : function(e) {
			this.store.clearFilter();
			this.store.filter([{
						property : 'custom',
						value : false
					}]);
		}
	}
},
			columns : [{
						xtype : 'checkcolumn',
						header : 'Included',
						flex : 1,
						sortable : true,
						dataIndex : 'in',
						editor : {
							xtype : 'checkbox',
							cls : 'x-grid-checkheader-editor'
						}
					}, {

						text : 'Trade Name',
						flex : 2,
						sortable : true,
						dataIndex : 'description'
					}, {
						text : 'Generic Name',
						flex : 2,
						sortable : true,
						dataIndex : 'genericName'
						,
					}, {
						text : 'Form',
						flex : 1,
						sortable : true,
						dataIndex : 'form'
					}, {
						text : 'Dose/Units',
						flex : 1,
						sortable : true,
						dataIndex : 'uom'
					}, {
						text : 'Migraine Specific',
						flex : 1,
						sortable : true,
						dataIndex : 'migraineTreatment',
						renderer: function(value, metaData, record, rowIndex, colIndex, store) {
			                return value ? "Yes": "No";
			            }
					}

			]
		}
				
				,{
					xtype : 'grid',
					title : "Custom",
					store : 'treatments',
					plugins : [Ext.create('Ext.grid.plugin.RowEditing',
							{listeners:
								{canceledit: function(context){
									var data = context.record.data;
									if ( data.description == "Name of Treatment")
										if ( data.genericName == "Name of Treatment")
											context.record.store.remove(context.record);    
								}}})],
					listeners : {
					show : {
						fn : function(e) {
							this.store.clearFilter();
							this.store.filter([{
										property : 'custom',
										value : true
									}]);
						}
					}
					},
					tbar : [{
								text : 'Add',
								handler : function() {
									var grid = this.up('panel');
									var rowEditing = grid.plugins[0];
									rowEditing.cancelEdit();

									var r = Ext.ModelManager.create({
												description : 'Name of Treatment',
												genericName : 'Name of Treatment',
												uom : 'i.e. 10 mg',
												form : 'i.e. oral tablet',
												migraineTreatment : false,
												'in' : true,
												custom : true
											}, 'IH.model.Treatment');

									grid.store.insert(0, r);
									rowEditing.startEdit(0, 0);

								}
							}, {
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
								header : 'Included',
								flex : 1,
								sortable : true,
								dataIndex : 'in',
								editor : {
									xtype : 'checkbox',
									cls : 'x-grid-checkheader-editor'
								}
							},
							{
								text : 'Trade Name',
								flex : 2,
								sortable : true,
								dataIndex : 'description',
								editor : {
									allowBlank : false
								}
							}, 
							{
								text : 'Generic Name',
								flex : 2,
								sortable : true,
								dataIndex : 'genericName',
								editor : {
									allowBlank : false
								}
							},
							{
								text : 'Form',
								flex : 1,
								sortable : true,
								dataIndex : 'form',
								editor : {
									allowBlank : false
								}
							}, {
								text : 'Dose/Units',
								flex : 1,
								sortable : true,
								dataIndex : 'uom',
								editor : {
									allowBlank : false
									,
								}
							}, {
								text : 'Migraine Specific',
								xtype : 'checkcolumn',
								flex : 1,
								sortable : true,
								dataIndex : 'migraineTreatment',
								editor : {
									xtype : 'checkbox',
									cls : 'x-grid-checkheader-editor'
								}
							}]

				}
				
				],

	listeners : {
		render : {
			fn : function(e) {
				var store = Ext.StoreMgr.lookup('treatments');
				store.clearFilter();
				store.filter([{
							property : 'custom',
							value : false
						}]);
			}
		}
	},
	buttons: [
{
    xtype: 'button',
	text : 'OK',
	handler : function() {
		var store = Ext.StoreMgr.lookup('treatments');
		var filters = store.filters.clone();
		store.clearFilter();
		var payload = [];
		store.each( function(o){
			o.data["_id"] = o.data["id"];
			//console.log(o.data);
			payload.push(o.data);
		});
		Ext.create('IH.core.PostEntity',"/app/service/profile/treatments",payload);
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
	          
	          ]
	
	/*bbar: {
		xtype:"toolbar",
        height: 30,
        items: [
{
    xtype: 'button',
	text : 'OK',
	handler : function() {
		var store = Ext.StoreMgr.lookup('treatments');
		var filters = store.filters.clone();
		store.clearFilter();
		var payload = [];
		store.each( function(o){
			console.log(o.data);
			payload.push(o.data);
		});
		Ext.create('IH.core.PostEntity',"/app/service/profile/treatments",payload);
		IH.ClearStore(store); 
		store.filter(filters.items);

	}
},

{
    xtype: 'button',
    text: 'Cancel'
}
        ]
       } */
});