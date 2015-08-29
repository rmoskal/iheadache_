Ext.define('IH.controller.Patients', {
	extend : 'Ext.app.Controller',
	stores : ['UserPrincipal','PhysicianAssociations'],
	models : ['UserPrincipal'],
	//views : ['patients.Window', 'patients.PatientInfo', 'patients.PatientGrid'],
			
	init: function() {
		this.application.addListener({
			'change_user' : function(user) {
				IH.app.fireEvent('refresh-calendars');
			},
			scope : this
		});
	
	},

	counter: runCount = 0,
	renderMe: function() {
 
		var p = Ext.create('IH.view.patients.Window', {
			hideMode : 'offsets',
			items : [ 
{
	xtype: "LiveSearchGridPanel",
    store: "PhysicianAssociations",
    columnLines: true,
	columns : [{
					xtype:'actioncolumn',
					flex : .5,
					items: [{
						icon: 'resources/images/icons/icon-Add.png',
						tooltip: 'Add',
						handler: function (dataview, rowIndex, colIndex, item, e, rec, row) {
						  runCount++;
						  if (runCount < 2) {
							var patRec = dataview.store.getAt(rowIndex);

			        		Ext.Ajax.request({ 
	        					method: 'PUT',
				        		url: '/app/service/hcp/switch/' + patRec.internalId, 
	        					success: function(o){
				        			IH.currentPatient = patRec.internalId;
				        			IH.dataFor = patRec.raw;
	        						document.title = "IHeadache MD";
	        						IH.view.Popup.msg('Success', "Changed patient to "  + patRec.get("firstName") + " "+ patRec.get("lastName")) ;
	        						document.getElementById("patientName").innerHTML= "Patient: " + patRec.get("firstName") + " "+ patRec.get("lastName");
	        						IH.app.fireEvent('change_user', patRec.internalId); 
	        						dataview.up("window").close();
	        			
	        					},
	        					failure: function(response, opts) {
	        						runCount = 0;
	        						Ext.Msg.alert('Error', response.statusText);
	        	    			}
	        				});  // request end
	        			  }  // if runCount end
	        			  else {
	        			  	//Ext.Msg.alert('handler', 'runCount' + runCount);
	        			  }
                 		}  // handler end
					}]  // items end
				},
		{
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
	   viewConfig: {
        stripeRows: true,
        listeners: {
        	itemdblclick: function(dataview, item, index, e) {
			  runCount++;
			  if (runCount < 2) {
        		
        		Ext.Ajax.request({ 
	        		method: 'PUT',
	        		url: '/app/service/hcp/switch/' + item.internalId, 
	        		success: function(o){
	        			IH.currentPatient = item.internalId;
	        			IH.dataFor = item.raw;
	        			document.title = "IHeadache MD";
	        			IH.view.Popup.msg('Success', "Changed patient to "  + item.get("firstName") + " "+ item.get("lastName")) ;
	        			document.getElementById("patientName").innerHTML= "Patient: " + item.get("firstName") + " "+ item.get("lastName");
	        			IH.app.fireEvent('change_user', item.internalId); 
	        			dataview.up("window").close();
	        			
	        		},
	        		failure: function(response, opts) {
	        			runCount = 0;
	        			Ext.Msg.alert('Error', response.statusText);
	        	    }  // failure end
	        		});  // request end
			  }  // if end
			  else {
	        	//Ext.Msg.alert('handler', 'runCount' + runCount);
	          }
			  
            }  // double click end
      }  // listeners end
    }  // view config end
}
			]

		});

		return p;
		
	}
});
