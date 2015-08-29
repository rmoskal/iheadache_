Ext.define('IH.view.help.HelpViewer', {
    extend: 'Ext.window.Window',

    height: 450,
    width: 600,
	autoScroll : false,
    title: 'Help Window',

    initComponent: function() {
        var me = this;

        me.callParent(arguments);
    }
});