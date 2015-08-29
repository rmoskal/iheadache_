Ext.define('IH.view.headacheViewer', {
    extend: 'Ext.window.Window',

    height: 600,
    width: 600,
	autoScroll : true,
	autoShow:true,
	modal:true,
	bodyCls: 'headache-preview',
    title: 'Headache Details',

    initComponent: function() {
        var me = this;

        me.callParent(arguments);
    }
});