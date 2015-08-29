Ext.define('IH.core.YesNoEditor2', {
	extend : 'Ext.form.Panel',
	height : 300,
	alias : "widget.yesnoeditor2",
	width : '50%',
	layout : 'anchor',
	items:[],
	defaults : {
		anchor : '100%'
	},
	constructor : function(cfg) {
		cfg = cfg || {};
		if (cfg['_store'] == null)
			throw "We need a store";
		
		this.store = cfg['_store'];
		
		
		this.callParent(arguments);
		this.initConfig(cfg);
	},
	
	initComponent: function() {
		
		if (this.store.data.getCount == 0)
			store.on({
			    'load':{
			        fn: this.load,
			        scope:this
			    }});
			else
				this.load(this.store);
			
		this.callParent(arguments);
		
	},
	
	load : function(store){
		var isChecked = false;
		var me = this;
		store.each(function(o) {
			me.items.push(Ext.create('Ext.form.RadioGroup', {
				height : 20,
				fieldLabel : o.get('description'),
				name : o['id'],
				labelWidth : 230,
				labelSeparator : '',
				items : [ {
					boxLabel : 'Yes',
					name : o['id'],
					inputValue : 'yes',
					checked : isChecked
				}, {
					boxLabel : 'No',
					name : o['id'],
					inputValue : 'no',
					checked : isChecked
				} ]
			})); 
			
		});
		me.doLayout();
		
    }
		
	

});
