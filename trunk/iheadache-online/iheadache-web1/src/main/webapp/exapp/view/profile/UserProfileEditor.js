Ext.define('IH.view.profile.UserProfileEditor', {
    extend: 'Ext.window.Window',

    height: 340,
    width: 400,
    layout: {
        type: 'fit'
    },
    listeners : {
		render : {
			fn : function(e) {
				 var p  = Ext.create ('IH.core.FetchEntity',"/app/service/profile/principal");
				 this.down('form').down('textfield[name=firstName]').setValue(p.firstName);
				 this.down('form').down('textfield[name=lastName]').setValue(p.lastName);
				 this.down('form').down('textfield[name=birthdate]').setValue(new Date(p.birthdate));
				 this.down('form').down('textfield[name=gender]').setValue(p.gender);
				 this.down('form').down('textfield[name=address]').setValue(p.address);
				 this.down('form').down('textfield[name=city]').setValue(p.city);
				 this.down('form').down('textfield[name=state]').setValue(p.state);
				 this.down('form').down('textfield[name=zipcode]').setValue(p.zipcode);			 
				 this.down('form').down('textfield[name=country]').setValue(p.country);	
				}
		}
	},
    autoShow:true,
    title: 'Personal Information',
    items: [
            {
                xtype: 'form',
                bodyPadding: 10,
                monitorValid:true,
                title: '',
                fieldDefaults: {
        			msgTarget: 'side',
        			labelWidth: 100
        		},
        		defaultType: 'textfield',
        		defaults: {
        			anchor: '100%'
        		},
        		
        		items: [{
        			fieldLabel: 'First Name',
        			name: 'firstName',
        			allowBlank: false 
        		},
        		{
        			fieldLabel: 'Last Name',
        			name: 'lastName',
        			allowBlank: false 
        		},
        		 {
                    xtype: 'datefield',
                    fieldLabel: 'Date of Birth',
                    name:'birthdate',
                    anchor: '75%'
                },
                {
                    xtype: 'combobox',
                    name:'gender',
                    fieldLabel: 'Gender',
                    store: ["Male","Female"],
                    displayField:'id',
                    valueField: 'id',
                    allowBlank: false,
                    forceSelection : true,
                    triggerAction: 'all',
                    typeAhead: true,
                    emptyText: 'Tell us your gender',
                    anchor: '75%'
                    
                },
        		{
        			fieldLabel: 'Address',
        			name: 'address',
        			allowBlank: false 
        		},
        		{
        			fieldLabel: 'City',
        			name: 'city',
        			allowBlank: false ,
        		},
        		{
	                xtype: 'combo',
	                name: 'state',
	                fieldLabel: 'State',
                    anchor: '50%',
	                store:  Ext.create('IH.store.States'),
                    displayField:'description',
                    valueField: 'id',
                    queryMode: 'local',
                    allowBlank: false,
                    forceSelection : true,
                    triggerAction: 'all',
                    typeAhead: true,
                    width:200,
                    emptyText: 'Select a state'
	            },
        		{
        			fieldLabel: 'Zipcode',
        			name: 'zipcode',
        			allowBlank: false ,
                    anchor: '75%'
        		},
        		{
        			fieldLabel: 'Country',
        			name: 'country',
        			allowBlank: false ,
                    anchor: '75%'
        		}
        		],
        		
        		buttons:[ {
        	        xtype: 'button',
        	        text: 'OK',
        	        formBind:true,
        	        handler : function() {
        	        	var form = this.up('window').down('form');
        	        	bd = form.down('datefield[name=birthdate]')
        	        	var values = form.getValues();
        	        	values.birthdate = bd.getValue().getTime(); //fix the date
        	        	Ext.create('IH.core.PostEntity',"/app/service/profile/principal", values);
        	        	form.up('window').close();
        	        	
        	        }
        	    },
        	    {
        	        xtype: 'button',
        	        text: 'Cancel',
        	        handler : function() {
        	        	this.up('window').close();
        	        }
        	    }]
   
        		
            }
        ],

    initComponent: function() {
        var me = this;

        me.callParent(arguments);
    }
});