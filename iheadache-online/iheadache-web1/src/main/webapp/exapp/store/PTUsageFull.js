Ext.define('IH.store.PTUsageFull', {
	extend : 'Ext.data.Store',
	model : 'IH.model.PTUsage',
	requires : ['IH.model.PTUsage'],
	autoLoad : true,
    autoSync: true,
	proxy : {
		type : 'rest',
		url : '/app/service/ptusage/active',
		reader : {
			type : 'json'
		},
        writer: {
            type: 'json'
        }
	},
	
/*	listeners:{
	    write: function(store, o) {
	    	if (o.response.responseText == '')
	    		return;
	    	var oo = Ext.decode(o.response.responseText);
	    	var rec = store.getById(oo.id);
	    	if (rec)
	    		rec.set("revision", oo.revision);
	    	
	    	}
	    } */
});