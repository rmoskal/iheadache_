Ext.define('IH.view.UnapprovedUsers', {
	extend : 'Ext.form.Panel',
	title : 'Approve new HCP Users',
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
			IH.app.fireEvent('select-user', {
				pk : email,
				findUrl : "/app/service/profile/hcp-principal/email/",
				deleteUrl : "deleteHcpUser.svc?userId=",
				store : "NewUser",
				approve:true
			});

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
			IH.app.fireEvent('select-user', {
				pk : pk,
				findUrl : "/app/service/profile/hcp-principal/id/",
				deleteUrl : "deleteHcpUser.svc?userId=",
				store : "NewUser",
				approve:true
			});

		}
	} ],
	items : [ {
		xtype : "user-grid",
		store : "NewUser",
		height : 400,
		listeners : {
			itemclick : function(dv, record, item, index, e) {
				IH.app.fireEvent('select-user', {
					pk : record.internalId,
					findUrl : "/app/service/profile/hcp-principal/id/",
					deleteUrl : "deleteHcpUser.svc?userId=",
					store : "NewUser",
					approve:true
				});
			}
		}

	} ]
});