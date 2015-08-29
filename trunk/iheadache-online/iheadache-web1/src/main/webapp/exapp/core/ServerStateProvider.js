Ext.define('IH.core.ServerStateProvider', {
    /* Begin Definitions */
 
    extend : 'Ext.state.Provider',
    alias : 'state.jsonpstorage',
 
    config: {
       userId : null,
       url: "/app/service/storage/",
       timeout: 30000
    },
 
    constructor : function(config) {
        this.initConfig(config);
        var me = this;
 
        me.restoreState();
        me.callParent(arguments);
    },
    set : function(name, value) {
        var me = this;
 
        if( typeof value == "undefined" || value === null) {
            me.clear(name);
            return;
        }
        me.persist(name, value);
        me.callParent(arguments);
    },
    // private
    restoreState : function() {
        var me = this;
        Ext.Ajax.request({
            headers: { 'Content-Type': 'application/json' },
            url : this.getUrl() +'owner',
            disableCaching : true,
            success : function(results, o) {
            	var res = JSON.parse(results.responseText);
        		res.forEach(function(o){
        			me.state[o.key] = JSON.parse(o.data);
        		})
            },
            failure : function() {
                console.log('failed', arguments);
            },
            scope : this
        });
    },
    // private
    clear : function(name) {
        this.clearKey(name);
        this.callParent(arguments);
    },
    // private
    persist : function(name, value) {
        var me = this;
        Ext.Ajax.request({
    	   	url: this.getUrl() + name,
    	    headers: { 'Content-Type': 'application/json' },
    	   	method: 'PUT',
    	   	jsonData: value,
    	   	success: function(transport){
    	   	},
    	   	failure: function(transport){
    	   	    console.log('failed', arguments);
    	   	}
    	});
        
    },
    // private
    clearKey : function(name) {
        Ext.Ajax.request({
    	   	url: this.getUrl() + name,
    	    headers: { 'Content-Type': 'application/json' },
    	   	method: 'DELETE',
    	   	success: function(transport){
    	   	},
    	   	failure: function(transport){
    	   	    console.log('failed', arguments);
    	   	}
    	});
    }
});