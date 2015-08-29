Ext.define('IH.view.profile.PhysicianEditor', {
	
		constructor : function() {
			
			var store = Ext.StoreMgr.lookup('PhysicianProfile');
			store.clearFilter();
			store.filter([{
				property : 'in',
				value : true
			}]);
			return Ext.create('IH.core.ListEditor',{
			_dialog : 'IH.view.profile.PhysicianChooser',
			_callback : function(i, o) {
				o.set("in", false);
				o.commit();
				o.store.filter([{
					property : 'in',
					value : true
				}]);
			},
			title : 'My Physicians  (please remember to hit OK after you choose your provider)',
			height : 400,
			width : 700,
			renderTo: 'main-content6',
			tools:[	IH.buildHelpTool("physician_profile.html")],
			store : "PhysicianProfile",
			selModel:Ext.create('Ext.selection.CheckboxModel'),
			columns : [{
						text : 'Name',
						flex : 1,
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
					}
					
					
			],
			
			
			buttons : [

			
			{
						text : 'Ok',
						handler : function() {
							var store = Ext.StoreMgr.lookup('PhysicianProfile');
							payload = [];
							store.each( function(o){
								if (o.get('in'))
									payload.push(o.get('id'));
							}); 
							
							Ext.create('IH.core.PostEntity',"/app/service/profile/associations",payload);
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
					]});

		}});