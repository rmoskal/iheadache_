Ext.define('IH.view.patients.PatientGrid', {
			extend : 'Ext.grid.Panel',
			store : 'PhysicianAssociations',
			height : 400,
			width : "100%",
			listeners : {
				afterrender : {
					fn : function(e) {
						var c = IH.app.getController("IH.controller.Patients");
						c.refreshSummary();
					}
				}
			},
			
	    	selModel: new Ext.selection.RowModel({
                singleSelect: true}),
			columns : [{
			    		   text : 'E-mail',
			    		   flex : 1,
			    		   sortable : true,
			    		   dataIndex : 'name'
				
			    	   }, {
			    		   text : 'Last Name',
			    		   flex : 1,
			    		   sortable : true,
			    		   dataIndex : 'lastName'
			    	   },{
			    		   text : 'First Name',
			    		   flex : 1,
			    		   sortable : false,
			    		   dataIndex : 'firstName'
			    	   }],
			       buttons:[ {
			           xtype: 'button',
			           text: 'Load Patient Data',
			           handler : function() {
				        	var grid = this.up('panel');
				        	if (grid.getSelectionModel().selected.length ==0){
				        		IH.view.Popup.msg('Warning', "Select a patient to load their information");
				        		runCount = 0;
				        		return;
				        	};
				        	var id = grid.getSelectionModel().selected.items[0].internalId;
				        	Ext.Ajax.request({ 
				        		method: 'PUT',
				        		url: '/app/service/hcp/switch/' + id, 
				        		success: function(o){
				        			IH.view.Popup.msg('Success', "Changed patient");
				        			IH.currentPatient = id;
				        			IH.app.fireEvent('change_user', id); 
				        		},
				        		failure: function(response, opts) {
				        			Ext.Msg.alert('Error', response.statusText);
				        	    }  // end failure
				        	});  // end request
			           }  // end handler
			       }]
			
});