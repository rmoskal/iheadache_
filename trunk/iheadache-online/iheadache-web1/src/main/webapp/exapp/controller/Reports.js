Ext.define('IH.controller.Reports', {
	extend : 'Ext.app.Controller',
	views: ['reports.Window', 'reports.ReportHeader','Popup'],
	refs : [{
		ref : 'reportHeader',
		selector : 'report_header'
	},
	{
		ref : 'reportWindow',
		selector : 'report_window'
	}
	],
	init: function() {
		//var self = this;
		this.control({
			'report_window button[text=Create]' : {
				click : this.create
			}
		});
		

	},
	

	
	create : function() {
		var panel = this.getReportWindow();
		var from_date = panel.down("field[name='from_date']").value;
		var to_date = panel.down("field[name='to_date']").value;
		var chunk = panel.down("field[name='chunk']").value;
		this._getFields("/app/service/report",from_date,to_date,chunk);

		
	},
	
	renderMe: function() {
		
		var p = Ext.create('IH.view.reports.Window', {
			renderTo : 'main-content5',
			hideMode : 'offsets',
			items : [ 
			         Ext.create('IH.view.reports.ReportHeader'),
			         Ext.create('IH.view.reports.ReportViewer')
			         
			]

		});
		return p;
		
	},
	
	
	_getFields : function(url, startDate, stopDate, chunk) {
		var renderer = Ext.util.Format.dateRenderer('Y-m-d');
		var self=this;
		var data = $.ajax({
			url : url + "?startDate=" + renderer(startDate)
					+ "&endDate=" + renderer(stopDate)+ "&chunk="
					+ chunk,
			dataType : "json",
			cache:false,
			success : function(json, a,rsp) {
				if (rsp.status == 204)
					Ext.MessageBox.show({
				          title: 'Warning',
				          msg: "There were no headaches during this period",
				          buttons: Ext.Msg.OK,
				          icon: Ext.Msg.WARNING
				      });
				var form = self.getReportHeader();
			    var vals = form.getValues();
			    json.opts = [].concat(vals.rb);
			    
			    json.total_headaches = 0;
			   
			    
			    /*Total up the headaches per period */
			    $.each(json, function(i,o) {
			    	o.headache_count = 0;
			    	o.no_headaches = 0;
			    	$.each(o.headache_types, function(i,inner) {
			    		if (inner.description =="No Headache"){
			    			o.no_headaches += parseInt(inner.value);
			    			return true;
			    		};
			    		json.total_headaches += parseInt(inner.value);
			    		o.headache_count += parseInt(inner.value);
			    	});
			    });
			    
	
			    
			    json.startDate = startDate;
			    json.stopDate = stopDate;
			    json.user = IH.dataFor; // Is either the user principal or the user the physician is looking at
			    
			    
				var templateLoader = Ext.create('IH.core.TemplateLoader');
				var view =  Ext.ComponentQuery
				.query("report_viewer");
				view = view[0];
				
				templateLoader.get('/resources/templates/report1.js',
				function(tpl) {
					view.update(tpl.applyTemplate(json));

	
				});
				
		       if (view.tools.length >0)
		    	   return;
					
				 view.addTool({
					    type:'print',
					    tooltip: 'Print',
					    handler: function(event, toolEl, panel){
					    	$("#headache-report").jqprint({footer:function(){return "printing";}});
					 }});
			},
			async : false

		});
		
	},
	
	
});