Ext.define('IH.core.HourChooser', {
			extend : 'Ext.form.ComboBox',
			store : Ext.create('Ext.data.Store', {
						fields : ['id', 'displayText'],
						data : [{
									"id" : 0,
									"displayText" : "0 hour"
								},
						        {
									"id" : 1,
									"displayText" : "1 hour"
								}, {
									"id" : 2,
									"displayText" : "2 hours"
								}, {
									"id" : 3,
									"displayText" : "3 hours"
								}, {
									"id" : 4,
									"displayText" : "4 hours"
								}, {
									"id" : 5,
									"displayText" : "5 hours"
								}, {
									"id" : 6,
									"displayText" : "6 hours"
								}, {
									"id" : 7,
									"displayText" : "7 hours"
								}, {
									"id" : 8,
									"displayText" : "8 hours"
								}, {
									"id" : 9,
									"displayText" : "9 hours"
								}, {
									"id" : 10,
									"displayText" : "10 hours"
								}]
					}),
			queryMode : 'local',
			displayField : 'displayText',
			valueField : 'id',
			width : 80
		}

);