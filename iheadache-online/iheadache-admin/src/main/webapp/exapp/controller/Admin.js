Ext.define('IH.controller.Admin', {
	extend : 'Ext.app.Controller',
	models : [ 'UserPrincipal' ],
	stores : [ 'User', 'HcpUser', 'NewUser' ],
	views : [ "HCPUsers", "Users", "UserGrid", "UnapprovedUsers" ],

	init : function() {
		
		this.control({
			'user-grid toolbar button[text=Next]' : {
				click : this.next
			}
		});
		
		this.control({
			'user-grid toolbar button[text=Previous]' : {
				click : this.previous
			}
		});


		this.application.addListener({
			'select-user' : function(_in) {		
				
				$.ajax({
					url : _in.findUrl + _in.pk,
					dataType : "json",
					success : function(json) {
						var o = Ext.create("IH.view.UserDetail", {
							items : [ Ext.create('Ext.grid.property.Grid', {
								source : json,
								frame : true,
								autoScroll : true
							})]}, _in
						);
						
						if (_in.approve)
							o.getDockedItems('toolbar')[0].add({xtype:"button", text:"Approve",
								
							handler: function(btn) {
								var uId = btn.up("window").down("propertygrid").source._id;
				                 Ext.Ajax.request({
				                     url:"approveHcpUser.svc?userId=" + uId,
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
						
						o.show();
					},
					error : function() {
						Ext.MessageBox.show({
							title : 'Sorry!',
							msg : "No such user " + email,
							buttons : Ext.Msg.OK,
							icon : Ext.Msg.WARNING,
						});

					},
					async : false

				});
			}
		});

	},


	next : function(btn) {
		
		if (btn.up("panel").store.proxy.reader.rawData.nextLink) {
			btn.up("panel").store.load({
				params : {pageLink:btn.up("panel").store.proxy.reader.rawData.nextLink}
			});

		} 
	},

	previous : function(btn) {
	if (btn.up("panel").store.proxy.reader.rawData.previousLink) {
		btn.up("panel").store.load({
				params :  {pageLink:btn.up("panel").store.proxy.reader.rawData.previousLink}
			});

		} 
	}
}

);