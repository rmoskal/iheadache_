Ext.define('IH.core.HourChooser2', {
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
								}, {
									"id" : 11,
									"displayText" : "11 hours"
								}, {
									"id" : 12,
									"displayText" : "12 hours"
								}, {
									"id" : 13,
									"displayText" : "13 hours"
								}, {
									"id" : 14,
									"displayText" : "14 hours"
								}, {
									"id" : 15,
									"displayText" : "15 hours"
								}, {
									"id" : 16,
									"displayText" : "16 hours"
								}, {
									"id" : 17,
									"displayText" : "17 hours"
								}, {
									"id" : 18,
									"displayText" : "18 hours"
								}, {
									"id" : 19,
									"displayText" : "19 hours"
								}, {
									"id" : 20,
									"displayText" : "20 hours"
								}, {
									"id" : 21,
									"displayText" : "21 hours"
								}, {
									"id" : 22,
									"displayText" : "22 hours"
								}, {
									"id" : 23,
									"displayText" : "23 hours"
								}]
					}),
			queryMode : 'local',
			displayField : 'displayText',
			valueField : 'id',
			width : 80
		}

);