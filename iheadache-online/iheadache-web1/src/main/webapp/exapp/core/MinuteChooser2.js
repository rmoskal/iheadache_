Ext.define('IH.core.MinuteChooser2', {
			extend : 'Ext.form.ComboBox',
			store : Ext.create('Ext.data.Store', {
						fields : ['id', 'displayText'],
						data : [{
							        id: 0,
							        displayText: '0 minutes'
								},
								{
									id : 5,
									displayText : '5 minutes'
								}, {
									id : 10,
									"displayText" : '10 minutes'
								}, {
									id : 15,
									"displayText" : '15 minutes'
								}, {
									id : 20,
									"displayText" : '20 minutes'
								}, {
									id : 25,
									"displayText" : '25 minutes'
								}, {
									id : 30,
									"displayText" : '30 minutes'
								}, {
									id : 35,
									"displayText" : '35 minutes'
								}, {
									id : 40,
									"displayText" : '40 minutes'
								}, {
									id : 45,
									"displayText" : '45 minutes'
								}, {
									id : 50,
									"displayText" : '50 minutes'
								}, {
									id : 55,
									"displayText" : '55 minutes'
								}]
					}),
			queryMode : 'local',
			displayField : 'displayText',
			valueField : 'id',
			width : 80
			,
		}

);