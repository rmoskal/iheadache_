Ext.define('IH.view.dashboard.calendar.BackWindow', {
			extend : 'Ext.Panel',
			width : 700,
			cls : 'x-portal',
			//height:1000,
			alias : 'widget.dashboardcalendar_BackWindow',
			bodyCls : 'x-portal-body',
			//autoScroll:true,
			//bodyStyle: 'overflow-y:scroll; overflow-x:hidden',
			layout : 'fit',
			listeners : {
				render : {
					fn : function(e) {
					      this.down('toolbar').down('datefield[name=from_date]').setValue(IH.dash_from_date);
				           this.down('toolbar').down('datefield[name=to_date]').setValue(IH.dash_to_date);
				           this.down("field[name='chunk']").setValue(IH.dash_chunk);
						}
				}
			},

			tbar : [{
					xtype : 'datefield',
					fieldLabel : 'From',
					labelAlign : 'right',
					width : 200,
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
						fieldLabel : '',
						cls: 'calendarCombo',
						value : IH.dash_chunk,
						name:'chunk',
						margins : '2 0 2 10',
						bodyStyle: 'display:none'
					},{
						text : 'Refresh',
						iconCls : 'icon-refreshDash',
						action : 'refresh-dash'
					}]
			,

		});