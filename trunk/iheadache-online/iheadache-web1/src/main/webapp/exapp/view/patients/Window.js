Ext.define('IH.view.patients.Window', {
			extend : 'Ext.window.Window',
			alias: 'widget.patient_window',
			title:"Please select a patient",
			width : 700,
			height : 400,
			modal : true,
	        closable : false,
			autoShow:true,
			layout : {
				type : 'fit'
			},
			tools:[{
			    type:'help',
			    tooltip: 'Get Help',
			    handler: function(event, toolEl, panel){
			    	IH.app.fireEvent('show-help',"patients.html");
			    }}]
		});