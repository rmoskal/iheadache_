Ext.define('IH.view.profile.PreferencesEditor', {
    extend: 'Ext.FormPanel',
	tools:[	{
						type:'help',
	    				tooltip: 'Get Help',
	    				handler: function(event, toolEl, panel){
	    				IH.app.fireEvent('show-help',"preferences_editor.html");}
					}
				],
    height: 364,
    width: 500,
    title: 'Preferences',
    buttons:[ {
        xtype: 'button',
        text: 'OK',
        handler : function() {
        	Ext.create('IH.core.PostEntity',"/app/service/profile/settings", this.up('form').getValues()); 
        	IH.user.appSettings = this.up('form').getValues();
        }
    },
    {
        xtype: 'button',
        text: 'Cancel',
        handler : function() {
        	IH.app.fireEvent('change-profiletab', "Pro_Preference");
        	
        }
    }]
	,

    initComponent: function() {
        var me = this;
        me.items = [
            {
                xtype: 'fieldset',
                height: 73,
                title: 'Headache Order Test Preference',
                border: 1,
					style: {
					    borderColor: 'white',
					    borderStyle: 'solid'
					},
                items: [
                    {
                        xtype: 'radiogroup',
                        height: 111,
                        width: 368,
                        layout: {
                            align: 'stretch',
                            type: 'vbox'
                        },
                        fieldLabel: '',
                        border: false,
                        items: [
                            {
                                xtype: 'radiofield',
                                boxLabel: 'Tension Headache Classification First',
                				name : 'sortRule',
								inputValue : 'TENSION_PROBABLE',
                                checked : IH.user.appSettings.sortRule == "TENSION_PROBABLE"
                            },
                            {
                                xtype: 'radiofield',
                                boxLabel: 'Probable Migraine Classification First',
                				name : 'sortRule',
								inputValue : 'PROBABLE_TENSION',
                                checked : IH.user.appSettings.sortRule == "PROBABLE_TENSION"
                            }
                        ]
                    }
                ]
            },
            {
                xtype: 'fieldset',
                height: 102,
                title: 'Headache Duration Rules',
                border: 1,
					style: {
					    borderColor: 'white',
					    borderStyle: 'solid'
					},
                items: [
                    {
                        xtype: 'radiogroup',
                        height: 120,
                        layout: {
                            align: 'stretch',
                            type: 'vbox'
                        },
                        fieldLabel: '',
                        items: [
                            {
                                xtype: 'radiofield',
                                boxLabel: 'Use duration if entered',
                  				name : 'durationRule',
								inputValue : 'NOT_REQUIRED',
                                checked : IH.user.appSettings.durationRule == "NOT_REQUIRED"
                            },
                            {
                                xtype: 'radiofield',
                                boxLabel: 'Ignore Duration',
                  				name : 'durationRule',
								inputValue : 'IGNORED',
                                checked : IH.user.appSettings.durationRule == "IGNORED"
                            },
                            {
                                xtype: 'radiofield',
                                boxLabel: 'Require Duration',
                  				name : 'durationRule',
								inputValue : 'REQUIRED',
                                checked : IH.user.appSettings.durationRule == "REQUIRED"
                            }
                        ]
                    }
                ]
            },
            {
                xtype: 'fieldset',
                height: 94,
                title: 'Display Medication Names in Reports',
                border: 1,
					style: {
					    borderColor: 'white',
					    borderStyle: 'solid'
					},
                items: [
                    {
                        xtype: 'radiogroup',
                        height: 78,
                        layout: {
                            align: 'stretch',
                            type: 'vbox'
                        },
                        fieldLabel: '',
                        items: [
                            {
                                xtype: 'radiofield',
                                boxLabel: 'Trade Name',
                  				name : 'drugRule',
								inputValue : 'TRADE',
                                checked : IH.user.appSettings.drugRule == "TRADE"
                            },
                            {
                                xtype: 'radiofield',
                                boxLabel: 'Generic Name',
                  				name : 'drugRule',
								inputValue : 'GENERIC',
                                checked : IH.user.appSettings.drugRule == "GENERIC"
                            },
                            {
                                xtype: 'radiofield',
                                boxLabel: 'Both',
                  				name : 'drugRule',
								inputValue : 'BOTH',
                                checked : IH.user.appSettings.drugRule == "BOTH"
                            }
                        ]
                    }
                ]
            }
        ];
        me.callParent(arguments);
    }
});