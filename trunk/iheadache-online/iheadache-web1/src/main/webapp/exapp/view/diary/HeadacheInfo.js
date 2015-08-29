Ext.define('IH.view.diary.HeadacheInfo', {
			extend : 'Ext.Panel',
			alias : 'widget.diary_h_info',
			bodyPadding : 5,
			width:"100%",
			height:"720px",
			title : 'Headache Summary',
			//autoScroll: true,
			tools : [
			{

				tooltip : 'Edit',
				xtype:'button',
				text    : 'Edit',
				iconCls : 'icon-editHeadache',
				handler : function() {
				//console.log(IH.current.headache._id);
				if (!IH.current.headache)
					return;
				if (IH.hcp_site)
					return;
				var controller = IH.app.getController('Editor');
				IH.current.headache = Ext.create('IH.core.FetchEntity',
					"/app/service/headache/" + IH.current.headache._id);
				controller.displayEditor(IH.current.headache);
			
		},
	} ,
	
	{
	    type:'help',
	    tooltip: 'Get Help',
	    handler: function(event, toolEl, panel){
	    	IH.app.fireEvent('show-help',"diary.html");
	 }
	},
	{
	    type:'print',
	    tooltip: 'Print',
// The below will add a better looking button to the ones in the toolbar
//				xtype:'button',
//				text    : 'Edit',
//				iconCls : 'icon-editHeadache',
	    handler: function(event, toolEl, panel){
	    	$("#headache-summary").jqprint();
	 }
	}
	 
	 
	]
		});

