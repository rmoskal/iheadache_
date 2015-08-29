Ext.define('IH.controller.Preventative', {
	extend : 'Ext.app.Controller',
	stores : ['PTUsagesInactive','PTUsageFull','PTreatmentProfile'],
	views: ['preventative.Window', 'preventative.Calendar','preventative.PTPanel','preventative.InactivePTGrid',
	        'preventative.ActivePTGrid'], 
	
	renderMe: function () {
		
		return Ext.create('IH.view.preventative.Window', {
            renderTo:'main-content7',
            items:[Ext.create('IH.view.preventative.Calendar'),
                   Ext.create('IH.view.preventative.PTPanel')]});
		
		
	}
});