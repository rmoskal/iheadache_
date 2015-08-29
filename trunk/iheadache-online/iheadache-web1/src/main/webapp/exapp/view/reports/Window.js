Ext.define('IH.view.reports.Window', {
			extend : 'Ext.panel.Panel',
			id: 'reports',
			width : 700,
			title: "Reports",
			alias : 'widget.report_window',
			height : 960,
			listeners : {
				render : {
					fn : function(e) {
					      this.down('toolbar').down('datefield[name=from_date]').setValue(IH.dash_from_date);
				           this.down('toolbar').down('datefield[name=to_date]').setValue(IH.dash_to_date);
				           this.down("field[name='chunk']").setValue(IH.dash_chunk);
						}
				}},
				tools:[{
				    type:'help',
				    tooltip: 'Get Help',
				    handler: function(event, toolEl, panel){
				    	IH.app.fireEvent('show-help',"reports.html");
				    }}],
			tbar : [{
				xtype : 'datefield',
				fieldLabel : 'From',
				width : 200,
				labelAlign : 'right',
				format : 'Y-M-d',
				name:'from_date',
				vtype: 'daterange',
				endDateField: 'to_date' 
				
			},

			{
				xtype : 'datefield',
				width : 200,
				fieldLabel : 'To',
				labelAlign : 'right',
				format : 'Y-M-d',
				maxValue : new Date(),
				name:'to_date',
				vtype: 'daterange',
				startDateField: 'from_date'
			},

			{
				xtype : 'combo',
				fieldLabel : 'Group by',
				labelAlign : 'right',
				width : 200,
				store : Ext.create('Ext.data.Store', {
							fields : ['type', 'displayText'],
							data : [{
										"type" : "_7",
										"displayText" : "Weekly"
									}, {
										"type" : "_28",
										"displayText" : "28 days"
									}, {
										"type" : "_30",
										"displayText" : "30 days"
									}, {
										"type" : "MONTH",
										"displayText" : "Monthly"
									}, {
										"type" : "QUARTER",
										"displayText" : "Quarterly"
									}]
						}),
				queryMode : 'local',
				value : 'MONTH',
				displayField : 'displayText',
				valueField : 'type',
				name:'chunk',
				margins : '2 0 2 10'
			}, {
				text : 'Create',

				action : 'create'
			}]
		});