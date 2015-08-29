Ext.define('IH.view.UserDetail', {
	extend : 'Ext.Window',
	modal:true,
	title: "User Detail",
    width: 600,
    height: 400,
    layout: 'fit',
//    futonUrl: "http://localhost:5984/_utils/document.html?headache-development/",
    futonUrl: "https://cloudant.com/futon/database.html?betterqol.cloudbees%2Fheadache-development/",
    deleteUrl: "deleteUser.svc?userId=",
    store: "User",
    autoShow : false,
    fbar: [
           {
               xtype: 'button',
               text: 'Ok',
               handler: function(btn) {
            	   btn.up("window").close();
               }
           },
           
           {
               xtype: 'button',
               text: 'Go to Futon',
               handler: function(btn) {
            	  var uId = btn.up("window").down("propertygrid").source._id;  
            	  window.open(btn.up("window").futonUrl + uId);
            	 
               }
           },
           
           {
               xtype: 'button',
               text: 'Delete',
               handler: function(btn) {
			   		Ext.MessageBox.confirm('Confirm', 'Are you sure you want to delete the selected user?', 
			   			function(id){
			   				if (id=='yes') {
			                    			 
			                    			 
               
                 				var uId = btn.up("window").down("propertygrid").source._id;
                 				Ext.Ajax.request({
                     				url:btn.up("window").deleteUrl + uId,
                     				method: "POST",
                     				success: function(o){
                        				var store = Ext.data.StoreManager.lookup(btn.up("window").store);
                        				store.remove(store.getById(uId));
                        				btn.up("window").close();
                     				},
                     					failure: function(response, opts){
                        					alert("failed");
                     					},
                     				headers: { 'Content-Type': 'application/json' }
                 				});
			        		}
			  			});                 				
               }
           }
           
        ],
        
        items:[{xtype:"propertygrid"}],
        
		constructor : function(cfg, xtra) {
			cfg = cfg || {};
			
			this.callParent(arguments);
			this.initConfig(cfg);
			console.log(IH);
			this.futonUrl = IH.futonUrl;  //Global variable!!!
			if (xtra.deleteUrl)
				this.deleteUrl = xtra.deleteUrl;
			if (xtra.store)
				this.store = xtra.store;
			
	
				
			
		}



});