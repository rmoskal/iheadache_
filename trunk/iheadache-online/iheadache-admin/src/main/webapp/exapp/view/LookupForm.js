Ext.define('IH.view.LookupForm', {
	extend : 'Ext.form.Panel',
    title: 'Submit new Lookup Values',
    bodyPadding: 5,
    width: "100%",

    items: [{
        xtype: 'textarea',
        height:400,
        width:600,
        fieldLabel: 'Paste your json here',
        name: 'json'
    }],

    buttons: [{
        text: 'Submit',
        handler: function() {
            // The getForm() method returns the Ext.form.Basic instance:
            var form = this.up('form').getForm();
            if (form.isValid()) {
                // Submit the Ajax request and handle the response
                form.submit({
                    url: 'import.svc',
                    success: function(form, action) {
                       Ext.Msg.alert('Success', action.result.result);
                    },
                    failure: function(form, action) {
                        Ext.Msg.alert('Failed', action.result ? action.result : 'No response');
                    }
                });
            }
        }
    }]
});