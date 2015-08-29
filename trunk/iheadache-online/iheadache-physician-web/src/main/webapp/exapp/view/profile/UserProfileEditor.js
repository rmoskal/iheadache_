Ext.define('IH.view.profile.UserProfileEditor', {
    extend: 'Ext.window.Window',

    height: 800,
    width: 800,
    layout: 'fit',
    listeners : {
		render : {
			fn : function(e) {
				 var p  = Ext.create ('IH.core.FetchEntity',"/app/service/profile/hcp-principal");
				 this.down('form').down('textfield[name=firstName]').setValue(p.firstName);
				 //this.down('form').down('textfield[name=website]').setValue(p.website);	
				}
		}
	},
    autoShow:true,
    title: 'Physician Information',
    items: [
      {
    	xtype: 'panel',
    	title: 'Parent',
    	//height: 400,
    	//width: 700,
    	autoShow: true,
    	layout: 'column',
    		items: [
    		{
    			xtype: 'panel',
    			title: 'Child 1',
    			//height: 400,
    			columnWidth: 0.60,
    			
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
			        ]
			    			
			    			
		        },
		        {
	    			xtype: 'panel',
	    			title: 'Child 2',
	    			//height: 400,
	    			columnWidth: 0.40,
    			
				    items: [
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
				        ]
		        }
		        ]
		     }],
	        
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