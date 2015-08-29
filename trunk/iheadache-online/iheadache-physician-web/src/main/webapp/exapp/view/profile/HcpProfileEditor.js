Ext.define('IH.view.profile.UserProfileEditor', {
    extend: 'Ext.window.Window',

    height: 600,
    width: 800,
    layout: {
        type: 'column'
    },
    listeners : {
		render : {
			fn : function(e) {
				 var p  = Ext.create ('IH.core.FetchEntity',"/app/service/profile/hcp-principal");
				 this.down('form').down('textfield[name=firstName]').setValue(p.firstName);
				 this.down('form').down('textfield[name=lastName]').setValue(p.lastName);
				 this.down('form').down('textfield[name=institution]').setValue(p.institution);
				 this.down('form').down('textfield[name=birthdate]').setValue(new Date(p.birthdate));
				 this.down('form').down('textfield[name=gender]').setValue(p.gender);
				 this.down('form').down('textfield[name=address1]').setValue(p.address1);
				 this.down('form').down('textfield[name=address2]').setValue(p.address2);
				 this.down('form').down('textfield[name=city]').setValue(p.city);
				 this.down('form').down('textfield[name=state]').setValue(p.state);
				 this.down('form').down('textfield[name=zipcode]').setValue(p.zipcode);			 
				 this.down('form').down('textfield[name=country]').setValue(p.country);	
				 this.down('form').down('textfield[name=degree]').setValue(p.degree);	
				 this.down('form').down('textfield[name=specialty]').setValue(p.specialty);	
				 this.down('form').down('textfield[name=ucns_certified_in_Headache]').setValue(p.ucns_certified_in_Headache);	
				 this.down('form').down('textfield[name=include_in_physician_finder]').setValue(p.include_in_physician_finder);	
				 this.down('form').down('textfield[name=american_academy_of_neurology]').setValue(p.american_academy_of_neurology);	
				 this.down('form').down('textfield[name=american_headache_society]').setValue(p.american_headache_society);	
				 this.down('form').down('textfield[name=headache_cooperative_of_new_england]').setValue(p.headache_cooperative_of_new_england);	
				 this.down('form').down('textfield[name=headache_cooperative_of_the_pacific]').setValue(p.headache_cooperative_of_the_pacific);	
				 this.down('form').down('textfield[name=national_headache_foundation]').setValue(p.national_headache_foundation);	
				 this.down('form').down('textfield[name=southern_headache_society]').setValue(p.southern_headache_society);	
				 this.down('form').down('textfield[name=american_academy_of_neurology_meeting]').setValue(p.american_academy_of_neurology_meeting);	
				 this.down('form').down('textfield[name=american_headache_society_meeting]').setValue(p.american_headache_society_meeting);	
				 this.down('form').down('textfield[name=headache_cooperative_of_new_england_meeting]').setValue(p.headache_cooperative_of_new_england_meeting);	
				 this.down('form').down('textfield[name=headache_cooperative_of_the_pacific_meeting]').setValue(p.headache_cooperative_of_the_pacific_meeting);	
				 this.down('form').down('textfield[name=national_headache_foundation_meeting]').setValue(p.national_headache_foundation_meeting);	
				 this.down('form').down('textfield[name=southern_headache_society_meeting]').setValue(p.southern_headache_society_meeting);	
				 this.down('form').down('textfield[name=phone]').setValue(p.phone);	
				 this.down('form').down('textfield[name=fax]').setValue(p.fax);	
				 this.down('form').down('textfield[name=website]').setValue(p.website);	
				}
		}
	},
    autoShow:true,
    title: 'Physician Information',
    items: [
            {
                xtype: 'form',
                bodyPadding: 10,
                monitorValid:true,
                title: '',
                fieldDefaults: {
        			msgTarget: 'side',
        			labelWidth: 250
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
        			fieldLabel: 'Institution',
        			name: 'institution',
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
        			fieldLabel: 'Address 1',
        			name: 'address1',
        			allowBlank: false 
        		},
        		{
        			fieldLabel: 'Address 2',
        			name: 'address2',
        			allowBlank: true 
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
        		},
        		{
        			fieldLabel: 'Degree',
        			name: 'degree',
        			allowBlank: false 
        		},
        		{
        			fieldLabel: 'specialty',
        			name: 'specialty',
        			allowBlank: false 
        		},
        		{
        			fieldLabel: 'ucns_certified_in_Headache',
        			name: 'ucns_certified_in_Headache',
        			allowBlank: false 
        		},
        		{
        			fieldLabel: 'include_in_physician_finder',
        			name: 'include_in_physician_finder',
        			allowBlank: false 
        		},
        		{
        			fieldLabel: 'american_academy_of_neurology',
        			name: 'american_academy_of_neurology',
        			allowBlank: false 
        		},
        		{
        			fieldLabel: 'american_academy_of_neurology_meeting',
        			name: 'american_academy_of_neurology_meeting',
        			allowBlank: false 
        		},
        		{
        			fieldLabel: 'american_headache_society',
        			name: 'american_headache_society',
        			allowBlank: false 
        		},
        		{
        			fieldLabel: 'american_headache_society_meeting',
        			name: 'american_headache_society_meeting',
        			allowBlank: false 
        		},
        		{
        			fieldLabel: 'headache_cooperative_of_new_england',
        			name: 'headache_cooperative_of_new_england',
        			allowBlank: false 
        		},
        		{
        			fieldLabel: 'headache_cooperative_of_new_england_meeting',
        			name: 'headache_cooperative_of_new_england_meeting',
        			allowBlank: false 
        		},
        		{
        			fieldLabel: 'headache_cooperative_of_the_pacific',
        			name: 'headache_cooperative_of_the_pacific',
        			allowBlank: false 
        		},
        		{
        			fieldLabel: 'headache_cooperative_of_the_pacific_meeting',
        			name: 'headache_cooperative_of_the_pacific_meeting',
        			allowBlank: false 
        		},
        		{
        			fieldLabel: 'national_headache_foundation',
        			name: 'national_headache_foundation',
        			allowBlank: false 
        		},
        		{
        			fieldLabel: 'national_headache_foundation_meeting',
        			name: 'national_headache_foundation_meeting',
        			allowBlank: false 
        		},
        		{
        			fieldLabel: 'southern_headache_society',
        			name: 'southern_headache_society',
        			allowBlank: false 
        		},
        		{
        			fieldLabel: 'southern_headache_society_meeting',
        			name: 'southern_headache_society_meeting',
        			allowBlank: false 
        		},
        		{
        			fieldLabel: 'phone',
        			name: 'phone',
        			allowBlank: false 
        		},
        		{
        			fieldLabel: 'fax',
        			name: 'fax',
        			allowBlank: false 
        		},
        		{
        			fieldLabel: 'website',
        			name: 'website',
        			allowBlank: false 
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
        	        	Ext.create('IH.core.PostEntity',"/app/service/profile/hcp-principal", values);
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