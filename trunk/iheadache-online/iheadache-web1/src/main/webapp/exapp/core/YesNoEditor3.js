Ext.define('IH.core.YesNoEditor3', {
	extend : 'Ext.form.Panel',
    alias: 'widget.yesnoeditor3',
	layout : 'anchor',
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
											enableToggle: true,
											name : o.id,
											labelWidth : 400,
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
													}],
											listeners: {
													change: function(radio){
														
									                     if(radio.id == radio.container.dom.children[0].id){
									                    	 
									                    	 if(radio.items.get(0).checked == true || radio.items.get(1).checked == false){
									                    		 Ext.getCmp(radio.container.dom.children[1].id).items.get(1).setValue(true);
									                    	 }
//									                    	 if(radio.items.get(0).checked == false || radio.items.get(1).checked == true){
//									                    		 Ext.getCmp(radio.container.dom.children[1].id).items.get(0).setValue(true);
//									                    	 }
									                     }
									                     
									                     	if(radio.id == radio.container.dom.children[1].id){
									                    	 
									                    	 if(radio.items.get(0).checked == true || radio.items.get(1).checked == false){
									                    		 Ext.getCmp(radio.container.dom.children[0].id).items.get(1).setValue(true);
									                    	 }
//									                    	 if(radio.items.get(0).checked == false || radio.items.get(1).checked == true){
//									                    		 Ext.getCmp(radio.container.dom.children[0].id).items.get(0).setValue(true);
//									                    	 }
									                     }

									                     	if(radio.id == radio.container.dom.children[2].id){
	 
									                     		if(radio.items.get(0).checked == true || radio.items.get(1).checked == false){
									                     			Ext.getCmp(radio.container.dom.children[3].id).items.get(1).setValue(true);
									                     		}
//									                     		if(radio.items.get(0).checked == false || radio.items.get(1).checked == true){
//									                     			Ext.getCmp(radio.container.dom.children[3].id).items.get(0).setValue(true);
//									                     		}
									                     	}

									                     	if(radio.id == radio.container.dom.children[3].id){
	 
									                     		if(radio.items.get(0).checked == true || radio.items.get(1).checked == false){
									                     			Ext.getCmp(radio.container.dom.children[2].id).items.get(1).setValue(true);
									                     		}
//									                     		if(radio.items.get(0).checked == false || radio.items.get(1).checked == true){
//									                     			Ext.getCmp(radio.container.dom.children[2].id).items.get(0).setValue(true);
//									                     		}
									                     	}
													}
												}
										}));
							});

					cfg['items'] = items;
				}

		this.callParent(arguments);
		this.initConfig(cfg);
	}

});
