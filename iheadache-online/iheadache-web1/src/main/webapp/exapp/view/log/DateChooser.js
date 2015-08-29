Ext.define('IH.view.log.DateChooser', {
			extend : 'Ext.form.Panel',
			alias : 'widget.datechooser',
			bodyPadding : 5,
			columnWidth : 1,
			height : 100,
			layout : 'vbox',
			items : [{
						xtype : 'datefield',
						fieldLabel : 'Start',
						name : 'from_date',
						format : 'Y-M-d',
						vtype: 'daterange',
						endDateField: 'to_date'

					}, {

						xtype : 'datefield',
						fieldLabel : 'End',
						name : 'to_date',
						format : 'Y-M-d',
						vtype: 'daterange',
						startDateField: 'from_date',
					}

			],

			buttons : [{
						text : 'Refresh',
						action : 'refresh'
					}]
		});