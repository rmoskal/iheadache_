Ext.define('IH.view.preventative.PTPanel', {
extend: 'Ext.TabPanel',

    height: 200,
    width: 700,
    layout: 'fit',
    title: 'Preventative Treatments',
    activeTab: 0,
    
	initComponent : function(args) {
  		this.callParent(arguments);
  		var store = Ext.StoreMgr.lookup('PTreatmentProfile');
  		store.clearFilter();
  		store.filter([{
  			property : 'custom',
  			value : false
  		}]);
  		
  	},
       listeners: {
   		
   		tabchange: { fn: function (tp,newTab) {
   			newTab.store.load();
   			newTab.getView().refresh();
   		}
   			
   		}},
    tools:[
       {
	    type:'help',
	    tooltip: 'Get Help',
	    handler: function(event, toolEl, panel){
	    	IH.app.fireEvent('show-help',"ptreatments.html");}
       }],
	    
	    items: [
                        {
                            xtype: 'profile_ActivePTGrid'
                        },
                        {
                            xtype: 'profile_InactivePTGrid'
                        }
                ] 
    }
);