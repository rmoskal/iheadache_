Ext.define('IH.view.log.Window', {
			extend : 'Ext.panel.Panel',
			alias : 'widget.logwindow',
			title : 'Headache log',
			tools:[{
			    type:'help',
			    tooltip: 'Get Help',
			    handler: function(event, toolEl, panel){
			    	IH.app.fireEvent('show-help',"log.html");
			    }}],
			    listeners : {
					render : {
						fn : function(e) {
						      this.down('toolbar').down('datefield[name=from_date]').setValue(IH.dash_from_date);
					           this.down('toolbar').down('datefield[name=to_date]').setValue(IH.dash_to_date);
							}
					}
			    },
			width : 700,
			height : 1000,
			layout : {
				type : 'column'
			},
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
				format : 'Y-M-d',
				labelAlign : 'right',
				maxValue : new Date(),
				name:'to_date',
				vtype: 'daterange',
				startDateField: 'from_date'
			},
			{
				text : 'Refresh',

				action : 'refresh'
			}],
			items : [{xtype : 'logheader'},
					{xtype : 'loggrid'}]
		});