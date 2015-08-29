Ext.define('IH.core.DynamicRadioGroup', {
			extend : 'Ext.form.Panel',
			title : 'Symptoms',
			height : 300,
			width : '50%',
			layout : 'anchor',
			defaults : {
				anchor : '100%'
			},
			constructor : function(cfg) {
				cfg = cfg || {};
				var items = [];
				if (cfg['_contents']) {
					$.each(cfg['_contents'], function(i, o) {
								items.push(Ext.create('Ext.form.RadioGroup', {
											height : 20,
											fieldLabel : o['question'],
											name : o['id'],
											labelWidth : 230,
											labelSeparator : '',
											items : [{
														boxLabel : 'Yes',
														name : o['id'],
														inputValue : 'yes'
													}, {
														boxLabel : 'No',
														name : o['id'],
														inputValue : 'no'
													}]
										}));
							});

					cfg['items'] = items;
				}

				this.callParent(arguments);
				this.initConfig(cfg);
			}

		});
