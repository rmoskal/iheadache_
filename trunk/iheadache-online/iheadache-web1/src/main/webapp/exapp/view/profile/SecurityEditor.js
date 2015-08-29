Ext.define('IH.view.profile.SecurityEditor', {
    extend: 'Ext.window.Window',

    height: 300,
    width: 400,
    layout: {
        type: 'fit'
    },
    autoShow:true,
    title: 'Credentials',
    listeners : {
		render : {
			fn : function(e) {
			     this.down('form').down('textfield[name=email]').setValue(IH.user.name);
		         this.down('form').down('textfield[name=email2]').setValue(IH.user.name);
				}
		}
	},
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
        		items: [
        		        
        		 {
        			 xtype: 'fieldset',
        			 title: 'Change email',
        			 labelWidth: 100,
        			 defaults: {
              			anchor: '100%'
              		},
        			 items:[
        		{
        			fieldLabel: 'Email',
        			xtype: 'textfield',
        			name: 'email',
        			allowBlank: false ,
        			vtype: 'email'
        		},
        		{
        			fieldLabel: 'Verify email',
        			name: 'email2',
        			xtype: 'textfield',
        			allowBlank: false ,
        			vtype: 'email',
        			validator: function (value) {
        				var f1 = this.up('form').down('textfield[name=email]');
        				return f1.getValue()==value ?  true : "The email addresses must match";
        			}
        		}]},
        		{
       			 xtype: 'fieldset',
       			 title: 'Change password',
       			 labelWidth: 100,
    			 defaults: {
           			anchor: '100%'
           		},
       			 items:[
         		{
        			fieldLabel: 'Password',
        			xtype: 'textfield',
        			name: 'password',
        			vtype: 'pw_strong'
        		},
         		{
        			fieldLabel: 'Verify Password',
        			xtype: 'textfield',
        			name: 'password2',
        			vtype: 'password',
        			initialField: 'password' ,
  
        		}
        		]}
        		],
        		
        		buttons:[ {
        	        xtype: 'button',
        	        text: 'OK',
        	        formBind:true,
        	        handler : function() {
        	        	var form = this.up('window').down('form');
        	        	var values = form.getValues();
        		        Ext.Ajax.request({
        		            url: '/app/service/profile/credentials',
        		            params : values,
        		            method:'POST',
        		            success: function(response, opts) {
        		            	IH.view.Popup.msg("Sucess", "Updated credentials" );
        		            	IH.user.name = values.email;
        		            	form.up('window').close();
        		            },
        		            failure:function(res,opt) {
        		            	if (res.status = 500) {
        		            		form.down('textfield[name=email]').markInvalid('This email isalready registered!');
        		            		IH.view.Popup.msg("Duplicate Mail",  res.statusText);
        		            		return;
        		            	}
        		            	IH.view.Popup.msg("Error communicating with server",  res.statusText);
        		            		
       
        		            }
        		            });
        	        	
        	        }
        	    },
        	    {
        	        xtype: 'button',
        	        text: 'Cancel',
        	        handler: function() {
        	        	this.up('window').close();
        	        }
        	    }],

   
        		
            }
        ],
    initComponent: function() {
        var me = this;

        me.callParent(arguments);
    }
});