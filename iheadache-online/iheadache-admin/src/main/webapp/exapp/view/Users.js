Ext.define('IH.view.Users', {
	extend : 'Ext.form.Panel',
    title: 'Manage Users',
    layout : 'fit',
    bodyPadding: 5,
    width: "100%",
    tools:[{
        id: 'gear',
        handler:  function(){}
    }],
    tbar: [
                {
            xtype    : 'textfield',
            name     : 'email',
            emptyText: 'enter email'
        },
        {
            text   : 'Search',
            handler: function(o) {
            	
            	var ff = o.up("toolbar").down('textfield[name="email"]');
            	var  email = ff.getValue();
           	 	IH.app.fireEvent('select-user',{pk:email, findUrl: "/app/service/profile/principal/email/", 
           	 		deleteUrl:"deleteUser.svc?userId=",
           	 		store:"User" });      	
            }
        },
        {
            xtype    : 'textfield',
            name     : 'uid',
            emptyText: 'enter user id'
        },
        {
            text   : 'Search',
            handler: function(o) {
            	
            	var ff = o.up("toolbar").down('textfield[name="uid"]');
            	var  pk = ff.getValue();
           	 	IH.app.fireEvent('select-user',{pk:pk, findUrl: "/app/service/profile/principal/id/", 
           	 		deleteUrl:"deleteUser.svc?userId=",
           	 		store:"User"});    
              
            }
        }     
           ],
   items:[{xtype:"user-grid", store:"User", height:400, listeners : {
	    itemdblclick: function(dv, record, item, index, e) {
	    	IH.app.fireEvent('select-user',{pk:record.internalId, findUrl: "/app/service/profile/principal/id/", 
	    		deleteUrl:"deleteUser.svc?userId=",
	    		store:"User"});    
	    }
	}}]
});

