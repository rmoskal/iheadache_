Ext.define('IH.view.preventative.ActivePTGrid', {
	alias : 'widget.profile_ActivePTGrid',
	constructor : function() {
		

		
		var editor;
		
		//if (!IH.hcp_site)
			rowEditing = Ext.create('Ext.grid.plugin.RowEditing');
		 var ls = Ext.create("IH.store.PTreatmentProfile");
		 ls.clearFilter();
	
		 ls.filter([{
				property : 'in',
				value : true
			}]);
		 
		 
		 var store = Ext.StoreMgr.lookup("PTUsageFull");
		 store.sort('start', 'DESC');
		 store.on('beforesync', function(data) {
			 var u = data.update;
			 var c = data.create;
			 
			 var f = function (o) {
				 if (!o) return;
				 $.each(o, function(i,oo){
			     var pt = ls.getById(oo.data.treatmentId);
				 oo.data.treatmentDescription = pt.data.ptname;
				 oo.data.genericDescription = pt.data.genericName;
				 });
			 };
			 
			 f(u);
			 f(c);
			 
		 });
		 var grid = Ext.create('Ext.grid.Panel', {
		        plugins: [rowEditing],
		    	tools:[	IH.buildHelpTool("preventative_treatment_profile.html")],
				store : store,
                title: 'Active Treatments',
                flex:1,
                autoscroll:true,
				columns : [{
							header : 'Name',
							flex : 2,
							dataIndex : 'treatmentId',
							renderer:function(value, meta, rec) {

								var treatmentName = rec.data.treatmentDescription;
								var drugRule = IH.user.appSettings.drugRule;
								if (drugRule == "BOTH") 
									treatmentName = rec.data.treatmentDescription + ' (' + rec.data.genericDescription + ')';
								if (drugRule == "GENERIC") 
									treatmentName = rec.data.genericDescription;
								if (drugRule == "TRADE")
									treatmentName = rec.data.treatmentDescription;
								return treatmentName;


//								var drugRule = IH.user.appSettings.drugRule;
//								if (drugRule == "BOTH") 
//									return rec.data.treatmentDescription + "/" + rec.data.genericDescription;
//								if (drugRule == "GENERIC") 
//									return rec.data.genericDescription;
//								return rec.data.treatmentDescription;
							},
							field: {
				                xtype: 'combo',
				                store:ls,
		                        displayField:'ptname',
		                        valueField: 'id',
		                        mode: 'local',
		                        typeAhead: true,
		                        editable : false,
		                        triggerAction: 'all',
		                        lazyRender: true,
		                        emptyText: 'Select action',
		                        listeners: {
		                            beforequery: function(qe){
		                                delete qe.combo.lastQuery;
		                            }
		                        }
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
						                xtype: 'datefield',
								        allowBlank : true
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
				                xtype: 'combo',
				                store: new Ext.data.ArrayStore({
		                               fields: ['description'],
		                               data : [                                         
		                                       ['Changed to Higher Dose'],
		                                       ['Changed to Lower Dose'],
		                                       ['Cost'],
		                                       ['One Time Treatment'],
		                                       ['Not Effective'],
		                                       ['Side Effects' ],
		                                       ['Unsure If I Still Need']
		                                      ]
		                                }),
		                        displayField:'description',
		                        valueField: 'description',
		                        mode: 'local',
		                        typeAhead: true,
		                        editable : true,
		                        triggerAction: 'all',
		                        lazyRender: true,
		                        emptyText: 'Select action'
				            }
						}],
						
						 dockedItems: [{
					            xtype: 'toolbar',
					            items: [{
					                text: 'Add',
					                iconCls: 'icon-add',
					                disabled : IH.hcp_site,
					                handler: function(){
					                    // empty record
					                    store.insert(0, new Ext.create('IH.model.PTUsage'));
					                    rowEditing.startEdit(0, 0);
					                }
					            }
					            ,'-', {
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
					            }, ]
					        }]
			 
		 });
		 	if (! IH.hcp_site)
		    grid.getSelectionModel().on('selectionchange', function(selModel, selections){
		        grid.down('#delete').setDisabled(selections.length === 0);
		    });
		    
		 
		 grid.on('beforeedit',function(o) {return ! IH.hcp_site;},this);
		 return grid;
		
	}
});