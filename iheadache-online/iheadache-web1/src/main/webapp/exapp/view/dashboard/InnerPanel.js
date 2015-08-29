Ext.define('IH.view.dashboard.InnerPanel', {
	extend : 'Ext.Panel',
	alias : 'widget.innerpanel',
	width : '100%',
	height : 220,
	layout : 'fit',
	tools : [ {

		tooltip : 'Add to dashboard',
		type : 'pin',
		handler : function() {
			var p = this.up('panel');
			IH.app.fireEvent('add-front', p.contents, p.title, p.height);  
		}
	} ],

	constructor : function(cfg) {
		cfg = cfg || {};
		this.contents = cfg['items'][0].widgetName;
		this.callParent(arguments);
		this.initConfig(cfg);

	}

});