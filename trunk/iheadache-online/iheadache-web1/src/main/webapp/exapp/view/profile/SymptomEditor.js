Ext.define('IH.view.profile.SymptomEditor', {

			extend : 'Ext.grid.Panel',
			title : 'Custom Symptom Profile',
			layout : 'fit',
			height : 300,
			width : 400,
			tools:[	{
						type:'help',
	    				tooltip: 'Get Help',
	    				handler: function(event, toolEl, panel){
	    				IH.app.fireEvent('show-help',"symptoms_profile.html");}
					}
				],
			store : 'SymptomsProfile',
			scroll :'vertical',
			selType: 'rowmodel',
			plugins : [Ext.create('Ext.grid.plugin.RowEditing', {
			})],
			tbar : [{
				text : 'Add',
				handler : function() {
					var grid = this.up('panel');
					var rowEditing = grid.plugins[0];
					rowEditing.cancelEdit();

					var r = Ext.ModelManager.create({
						description : 'New Custom Symptom',
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
						header : 'Include',
						flex : 1,
						sortable : true,
						dataIndex : 'in',
						editor : {
							xtype : 'checkbox',
							cls : 'x-grid-checkheader-editor'
						}
					},
					{

						header : 'Symptoms',
						sortable : true,
						flex:2,
						dataIndex : 'description',
						editor : {
							 xtype: 'textfield',
							allowBlank : false
						}
					}

			],
			buttons : [

			{
						text : 'Ok',
						handler : function() {
					

							var store = Ext.StoreMgr.lookup("SymptomsProfile");
							var payload = [];
							IH.user.customSymptoms = [];
							store.each( function(o){
									IH.user.customSymptoms.push({id:o.id,description:o.description});
									payload.push(o.data);
							});
							Ext.create('IH.core.PostEntity',"/app/service/profile/custom_symptoms",payload);
							
							
							IH.ClearStore(store); 
							
						}
					},
					
					{
		                xtype: 'button',
		                text: 'Cancel',
		                handler: function() {
   		            		var grid = this.up('panel');
 		            		grid.store.load();
 		            		grid.getView().refresh();
		                }
		            }
					],


initComponent : function(args) {
	this.callParent(arguments);
	var store = Ext.StoreMgr.lookup("SymptomsProfile");
	store.load();
	
}

		});