Ext.define('IH.core.MinuteChooser', {
			extend : 'Ext.form.ComboBox',
			store : Ext.create('Ext.data.Store', {
						fields : ['id', 'displayText'],
						data : [{
							        id: 0,
							        displayText: '0 minutes'
								},
								{
									id : 15,
									displayText : '15 minutes'
								}, {
									id : 30,
									"displayText" : '30 minutes'
								}, {
									id : 45,
									"displayText" : '45 Minutes'
								}]
					}),
			queryMode : 'local',
			displayField : 'displayText',
			valueField : 'id',
			width : 80
			,
		}

);