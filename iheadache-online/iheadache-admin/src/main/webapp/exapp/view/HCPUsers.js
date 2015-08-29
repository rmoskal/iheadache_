Ext.define('IH.view.HCPUsers', (function(){
	
	var config = function(val, findUrl)
	{
		var res = {
			pk : val,
			findUrl : "/app/service/profile/hcp-principal/id/",
			deleteUrl : "deleteHcpUser.svc?userId=",
			store : "HcpUser", 
			
		};
		
		if (findUrl)
			res.findUrl = findUrl;
		return res;
		
	};
	
	return {extend : 'Ext.form.Panel',
	title : 'Manage HCP Users',
	bodyPadding : 5,
	width : "100%",
	tbar : [ {
		xtype : 'textfield',
		name : 'email',
		emptyText : 'enter email'
	}, {
		text : 'Search',
		handler : function(o) {

			var ff = o.up("toolbar").down('textfield[name="email"]');
			var email = ff.getValue();
			IH.app.fireEvent('select-user', config(email,  "/app/service/profile/hcp-principal/email/"));

		}
	}, {
		xtype : 'textfield',
		name : 'uid',
		emptyText : 'enter user id'
	}, {
		text : 'Search',
		handler : function(o) {

			var ff = o.up("toolbar").down('textfield[name="uid"]');
			var pk = ff.getValue();
			IH.app.fireEvent('select-user', config(pk));

		}
	} ],
	items : [ {
		xtype : "user-grid",
		store : "HcpUser",
		height : 400,
		listeners : {
		    itemdblclick: function(dv, record, item, index, e) {
		    	IH.app.fireEvent('select-user', config(record.internalId));    
		    }
		}
	}]}
	
	
}()));