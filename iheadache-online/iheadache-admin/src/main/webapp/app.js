Ext.application({
    name: 'IH',
    appFolder:'exapp',
    controllers: ["Admin"],
	launch : function() {		
		
        Ext.require([
                     'exapp.view.LookupForm'
                 ]);
		
		var store = Ext.create('Ext.data.TreeStore', {
		    root: {
		        expanded: true,
		        children: [
				    { text: "Manage Approval Queue", leaf: true , view:"IH.view.UnapprovedUsers" },
		            { text: "Manage Users", leaf: true,  view: "IH.view.Users"},
		            { text: "Manage HCP Users", leaf: true, view: "IH.view.HCPUsers"},
		            { text: "Upload Lookup Values", leaf: true, view:"IH.view.LookupForm" }
		        ]
		    }
		});
		
		 Ext.Ajax.defaultHeaders = {
    			 'Accept': 'application/json'
    			};
		 
		 
		 this.addEvents('select-user'); //fired when a user is selected
		 IH.app = this;
		 
		 IH.futonUrl = "http://localhost:5984/_utils/document.html?headache-development/"
		 
		 
		 
		
		Ext.create('Ext.container.Viewport', {
			layout : 'border',
			items : [ {
				title : 'Navigation',
				xtype:"treepanel",
				region : 'west',
				floatable : false,
				margins : '5 0 0 0',
				store: store,
				rootVisible: false,
				width : 175,
				minWidth : 100,
				maxWidth : 250,
				listeners: {
					itemclick: function(node, data){
						var mainPanels = Ext.ComponentQuery.query('panel[title="Main Content"]');
				        var newPanel = data.raw.view;
				        if (newPanel.split(":")[0] == "url") {
				        	console.log("then jump!");
				        	return;
				        }
				        	
						mainPanels[0].items.items[0].close();
						mainPanels[0].add(Ext.create(newPanel));
				    }
				}
			},

			{
				title : 'Main Content',
				collapsible : false,
				region : 'center',
				margins : '5 5 5 5',
				tools:[{
				    type:'close',
				    tooltip: 'Log out',
			
				    handler: function(event, toolEl, panel){
				    	Ext.create ('IH.core.EmptyPost','/app/service/profile/logout');
				    	window.location.reload();
				    }
				}],
				items: [Ext.create('IH.view.Welcome')
				        ]
			} ]
		});
		


		Ext.create('Ext.tree.Panel', {
		    title: 'Simple Tree',
		    width: 200,
		    height: 150,
		    store: store,
		    rootVisible: false,
		});
	}
});
