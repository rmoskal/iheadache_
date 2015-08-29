Ext.define('IH.view.profile.PTreatmentEditor', {
	extend: 'Ext.TabPanel',

    height: 480,
    width: 700,
	activeTab : 0,
	title: 'Preventative Treatment Profile',
	layout: "vbox",
	 tools:[{
                	    type:'help',
                	    tooltip: 'Get Help',
                	    handler: function(event, toolEl, panel){
                	    	IH.app.fireEvent('show-help',"ptreatments1.html");
                	    }}],
                	    
                	    buttons : [

                         			{
                         						text : 'Ok',
                         						handler : function() {
                         							
                         							var store = Ext.StoreMgr.lookup("PTreatmentProfile");
                         							var filters = store.filters.clone();
                         							store.clearFilter();
                         							var payload = [];
                         							store.each( function(o){
                         								o.data["_id"] = o.data["id"];
                       									payload.push(o.data);
                       							});
                       							Ext.create('IH.core.PostEntity',"/app/service/profile/ptreatments",payload);
                       							IH.ClearStore(store); 
                       							store.filter(filters.items);
                       							//var grid = this.up('panel');
                       							//grid.close();
                         		
                         				
                         						}
                         					},
                         					
                         					   {
                         		                xtype: 'button',
                         		                text: 'Cancel',
                         		               handler : function() {
                         		            	   
                         		            		var grid = this.up('panel').activeTab;
                         		            		grid.store.load();
                         		            		grid.getView().refresh();
                         		            	   
                         		               }
                         		            }
                         	],
                             listeners: {
                         		
                         		tabchange: { fn: function (tp,newTab) {
                         			
                         			var store = Ext.StoreMgr.lookup('PTreatmentProfile');
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
	items:[

  	  						{	bbar : [{
									text : 'The presence of a medication in this list does not constitute an endorsement for its use.',
									iconCls : 'incompleteStatus',
							}],
                            
       			         //{
     						xtype : 'grid',
     						title: 'Common',
     						store : 'PTreatmentProfile',
     						scroll :'vertical',
     						selModel : {
     							selType : 'cellmodel'
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
     									text : 'Name',
     									header : 'Trade Name',
     									flex : 2,
     									sortable : true,
     									dataIndex : 'ptname',
     									field : {
     										xtype : 'textfield'
     									}//,
     									//renderer: function(value, p, record){
     									//	return value + "/" + record.data['genericName'];
     									//}
     									
     								}
     								, 
     								{
     									header : 'Generic Name',
     									sortable : true,
     									flex:2,
     									dataIndex : 'genericName',
     									field : {
     										 xtype: 'textfield'
     									}
     								}

     						]
     					},
     					
     					{
     						xtype : 'grid',
     						title: 'Custom',
     						store : 'PTreatmentProfile',
     						scroll :'vertical',
     						selType: 'rowmodel',
     						plugins : [Ext.create('Ext.grid.plugin.RowEditing',
     								{listeners:
    								{canceledit: function(context){
    									var data = context.record.data;
    									if ( data.ptname == "Name of Treatment")
    											context.record.store.remove(context.record);    
    								}}})],
     						tbar : [{
     							text : 'Add',
     							handler : function() {
     								var grid = this.up('panel');
     								var rowEditing = grid.plugins[0];
     								rowEditing.cancelEdit();

     								var r = Ext.ModelManager.create({
     											ptname : 'Name of Treatment',
     											genericName : 'Name of Treatment',
     											custom : true,
     											in: true
     										}, 'IH.model.PreventativeTreatment');

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
     									header : 'Trade Name',
     									sortable : true,
     									flex:2,
     									dataIndex : 'ptname',
     									editor : {
     										 xtype: 'textfield',
     										allowBlank : false
     									}
     								},
     								{
     									header : 'Generic Name',
     									sortable : true,
     									flex:2,
     									dataIndex : 'genericName',
     									editor : {
     										 xtype: 'textfield',
     										allowBlank : true
     									}
     								}

     						]
     					}

     			                            
                       
                    ],
    initComponent: function() {
        var me = this;
        me.callParent(arguments);
    }
});