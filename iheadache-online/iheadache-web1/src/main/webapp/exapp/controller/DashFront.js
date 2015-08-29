Ext.define('IH.controller.DashFront', {
	extend : 'IH.controller.DashBase',
	views : ['dashboard.BackWindow','dashboard.acute.StackedAcuteGraph',
			'dashboard.StackedHeadacheTypeGraph' ],
	requires : [ 'IH.view.dashboard.StackedChart', 'IH.view.dashboard.SimpleStackedChart' ],

	init : function() {
		this.callParent(arguments);
	},
	
	windows : Ext.state.Manager.get("dash_front",
			[['IH.view.dashboard.StackedHeadacheTypeGraph',"Number of Headaches by Type",220]]),
	
	renderMe: function() {
		if(Ext.ComponentQuery.query('#d_DashFront').length==1)
			return;
		var _w = Ext.create('IH.view.dashboard.BackWindow', {
			id : 'd_DashFront',
			title : 'Dashboard',
			renderTo : 'main-content',
			hideMode : 'offsets',
			items : this.initShow()

		});
		
		/* _w.addTool({
			    type:'print',
			    tooltip: 'Print',
			    handler: function(event, toolEl, panel){
			    	//console.log(_w.el.id);
			    	$("#d_DashFront-body").jqprint();
			 }}); */
		
		this.application.addListener({ 
	        'add-front' : function(o,t,h) {
	        	
	        	var len=this.windows.length;
	        	for(var i=0; i<len; i++) 
	        		if (t==this.windows[i][1]) return;

	        	this.windows.push([o,t,h]);
	        	var renderer = Ext.util.Format.dateRenderer('Y-m-d');
	        	Ext.state.Manager.set("dash_front",this.windows);
	        	panel = Ext.ComponentQuery.query('#d_DashFront')[0];
	        	panel.add(Ext.create('IH.core.Panel', {
					closable : true,
					'title' : t,
					height:h,
					width:700,
					items : [ Ext.create(
							o,
							renderer(IH.dash_from_date), renderer(IH.dash_to_date), IH.dash_chunk)]
				}));
 		
	        },
			 scope:this
	    });
		
		this.control({
			'#d_DashFront [action=refresh-dash]' : {
				click : this.refreshDash
			}
		,'#d_DashFront': {
			beforeremove: function(container,cmp,opt) {
				this.windows = $.grep(this.windows, function(o,i){
					return o[1]==cmp.title ? false: true;
				});
				Ext.state.Manager.set("dash_front",this.windows);
			}
		} }
		
		);

	},
	reload : function(start, end, chunk) {
		results = [];
		$.each(this.windows, function(i,o){
			var closeable = true;
			if (o[0] == "IH.view.dashboard.StackedHeadacheTypeGraph")
				closeable = false;
			if (o[0] == "IH.view.dashboard.StackedHeadacheDaysGraph")
				closeable = false;
			results.push(
					
					Ext.create('IH.core.Panel', {
						closable : closeable,
						'title' : o[1],
						height:o[2],
						items : [ Ext.create(
								o[0],
								start, end, chunk)]
					})
					);
		});
		
		return results;
		

	}

});