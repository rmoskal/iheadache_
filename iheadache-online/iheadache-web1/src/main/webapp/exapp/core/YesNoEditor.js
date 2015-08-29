Ext.define('IH.core.YesNoEditor', {
	extend : 'Ext.form.Panel',
    alias: 'widget.yesnoeditor',
	layout : 'anchor',
	autoScroll:true,
	defaults : {
		anchor : '100%'
	},
	constructor : function(cfg) {
				cfg = cfg || {};
				var items = [];
				var itemHeight = (cfg['itemHeight'])? cfg['itemHeight'] : 20;
				if (cfg['_contents']) {
			
					$.each(cfg['_contents'], function(i, o) {
								items.push(Ext.create('Ext.form.RadioGroup', {
											height : itemHeight,
											fieldLabel : o.description,
											name : o.id,
											labelClsExtra : function(){return "custom" in o ? "custom_symptom": "";}(),
											labelWidth : 330,
											labelSeparator : '',
											items : [{
														boxLabel : 'Yes',
														name : o['id'],
														inputValue : 'yes',
														checked : o.yes == true
													}, {
														boxLabel : 'No',
														name : o['id'],
														inputValue : 'no',
														checked : o.no == true
													}]
										}));
							});

					cfg['items'] = items;
				}

		this.callParent(arguments);
		this.initConfig(cfg);
	}

});
