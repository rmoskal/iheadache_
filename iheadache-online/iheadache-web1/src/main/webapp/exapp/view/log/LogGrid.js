Ext.define('IH.view.log.LogGrid', {
			extend : 'Ext.grid.Panel',
			alias : 'widget.loggrid',
			width: "100%",
			store : 'Log',
			selModel:Ext.create('Ext.selection.CheckboxModel'),
			columns : [ {
						header : "Start",
						dataIndex : 'start',
						flex : 2,
					    sortable: false, 
						renderer : Ext.util.Format.dateRenderer('Y-M-d h:i A'),
						tdCls: 'wrap'
					}, {
						header : "End",
						dataIndex : 'end',
						flex : 2,
					    sortable: false, 
						renderer : Ext.util.Format.dateRenderer('Y-M-d h:i A'),
						tdCls: 'wrap'
					}, {
						header : "Type",
						flex : 2,
					    sortable: false, 
						dataIndex : 'kind',
						tdCls: 'wrap'
					}, {
						header : "Disabled",
						flex : 2,
					    sortable: false, 
						dataIndex : 'disabled'
					},

					{
						header : "Treatments",
						flex : 1,
					    sortable: false, 
						dataIndex : 'hasTreatments',
						renderer: function(value, metaData, record, rowIndex, colIndex, store) {
			                return value ? '<img src="/resources/images/icons/tick_16.png" />':'<img src="/resources/images/icons/block_16.png" />';
			            }
					}, {
						header : "Symptoms",
						flex : 1,
					    sortable: false, 
						dataIndex : 'hasSymptoms',
						renderer: function(value, metaData, record, rowIndex, colIndex, store) {
			                return value ? '<img src="/resources/images/icons/tick_16.png" />':'<img src="/resources/images/icons/block_16.png" />';
			            }
					}, {
						header : "Triggers",
						flex : 1,
					    sortable: false, 
						dataIndex : 'hasTriggers',
						renderer: function(value, metaData, record, rowIndex, colIndex, store) {
			                return value ? '<img src="/resources/images/icons/tick_16.png" />':'<img src="/resources/images/icons/block_16.png" />';
			            }
					}, {
						header : "Disability",
						flex : 1,
					    sortable: false, 
						dataIndex : 'hasDisability',
						renderer: function(value, metaData, record, rowIndex, colIndex, store) {
			                return value ? '<img src="/resources/images/icons/tick_16.png" />':'<img src="/resources/images/icons/block_16.png" />';
			            }
					}, {
						header : "Pain",
						flex : 1,
					    sortable: false, 
						dataIndex : 'hasPains',
						renderer: function(value, metaData, record, rowIndex, colIndex, store) {
			                return value ? '<img src="/resources/images/icons/tick_16.png" />':'<img src="/resources/images/icons/block_16.png" />';
			            }
						
					}, {
						header : "Note",
						flex : 1,
					    sortable: false, 
						dataIndex : 'hasNote',
						renderer: function(value, metaData, record, rowIndex, colIndex, store) {
				                return value ? '<img src="/resources/images/icons/tick_16.png" />':'<img src="/resources/images/icons/block_16.png" />';
				            }
				        }
					

			],
			fbar : [{
						type : 'button',
						text : 'Recent',
						action: 'previous'
					}, {
						type : 'button',
						text : 'Older',
						action: 'next'
					}],
					
			dockedItems: [{
			            xtype: 'toolbar',
			            items: [{
			                itemId: 'delete',
			                text: 'Delete',
			                iconCls: 'icon-delete',
			                disabled: true,
			                scope: this,
			                handler: function(button){
			                	var grid = button.up('panel');
			                	var selection = grid.getView().getSelectionModel().getSelection();
			                	if(selection.length==0) 
			                		return;
			                    Ext.MessageBox.confirm('Confirm', 'Are you sure you want to delete the selected headaches?', 
			                    function(id){
			                    		 if (id=='yes') {
			                    			 var controller = IH.app.getController('Editor');
			                    			 for(var i=0;i<selection.length;i++) 
			                    				 controller.remove(selection[i].internalId);
			                                    
			                    			 controller = IH.app.getController('Log');
			                    			 controller.refresh();
			                    			 
			                    			 
			                    		 }
			                    	 });
			                    }
			            }, ]
			        }],
					
			listeners: {
					   itemdblclick: function(dataview, rec, item, e) {
						  IH.app.fireEvent('nav-event',"calendar",null,rec, e); 
					    }
					    }
					  ,

			initComponent : function() {
				this.callParent(arguments);
				this.getSelectionModel().on('selectionchange', function(selModel, selections){
			        this.down('#delete').setDisabled(selections.length === 0);
			    }, this);

				
				
			}
		});