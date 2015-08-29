Ext.define('IH.view.preventative.Window', {
			extend : 'Ext.panel.Panel',
			alias : 'widget.preventativewindow',
			title : 'Preventative Treatments',
			tools:[{
			    type:'help',
			    tooltip: 'Get Help',
			    handler: function(event, toolEl, panel){
			    	IH.app.fireEvent('show-help',"preventative.html");
			    }}],
			width : 700,
			height : 800,
			layout : {
				type : 'column'
			}
		});