Ext.define('IH.view.preventative.InactivePTGrid', {
	alias : 'widget.profile_InactivePTGrid',
	constructor : function() {
		
		
		 
		 var store = Ext.StoreMgr.lookup("PTUsagesInactive");
		 store.sort('start', 'DESC');
		
		
		 var grid = Ext.create('Ext.grid.Panel', {
				width : 700,
				height : 400,
                title: 'Inactive Treatments',
		    	tools:[	IH.buildHelpTool("preventative_treatment_profile.html")],
				store : store,
				autoscroll: true,
				columns : [{
							header : 'Name',
							flex : 2,
							dataIndex : 'treatmentDescription',
							field: {
				                xtype: 'textfield',
				               
				            }
							
						}, {
							header : 'Dose',
							dataIndex : 'dose',
							field: {
				                xtype: 'textfield'
				            }
						}, {
							header : 'Start Date',
							dataIndex : 'start',
							flex : 1,
							renderer : Ext.util.Format
									.dateRenderer('Y-M-d'),
							field: {
						                xtype: 'datefield'
						            }
						}, {
							header : 'Stop Date',
							dataIndex : 'end',
							flex : 1,
							renderer : Ext.util.Format
									.dateRenderer('Y-M-d'),
							field: {
								                xtype: 'datefield'
								            }
						}, {
							header : 'Reason for Stopping',
							dataIndex : 'stopReason',
							flex:3,
							field: {
				                xtype: 'textfield'
				            }
						}],
						
						 dockedItems: [{
					            xtype: 'toolbar',
					            items: [ {
					                itemId: 'delete',
					                text: 'Delete',
					                iconCls: 'icon-delete',
					                disabled: true,
					                handler: function(){
					                    var selection = grid.getView().getSelectionModel().getSelection()[0];
					                    if (selection) {
					                        store.remove(selection);
					                    }
					                }
					            }, 
					            
					            {
					                itemId: 'reactivate',
					                text: 'Reactivate',
					                iconCls: 'icon-reactivate',
					                disabled: true,
					                handler: function(){
					                	
					                	  var selection = grid.getView().getSelectionModel().getSelection()[0];
						                    if (selection) {
						                    	selection.set("end",null);
						                    }
					                    
					                }
					            }
					            ]
					        }]
			 
		 });
		 	if (! IH.hcp_site)
		 		grid.getSelectionModel().on('selectionchange', function(selModel, selections){
		 			grid.down('#delete').setDisabled(selections.length === 0);
		 			grid.down('#reactivate').setDisabled(selections.length === 0);
		 		});
		    
		 grid.on('beforeedit',function(o) {return ! IH.hcp_site;},this);
		 return grid;
		
	}
});