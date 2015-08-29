Ext.define('IH.core.ListEditor', {
			extend : 'Ext.grid.Panel',
			columnLines : true,
			requires:['Ext.selection.CheckboxModel'],
			frame : false,
			iconCls : 'icon-grid',
		/*	viewConfig:{
				loadMask: false
				},*/
			constructor : function(cfg) {
				cfg = cfg || {};
				cfg['selModel'] = Ext.create('Ext.selection.CheckboxModel');
				if (cfg["_callback"] == undefined)
					cfg["_callback"] = function(i, o) {
						o.store.remove(o);
				};
				cfg['dockedItems'] = [{
					xtype : 'toolbar',
					items : [{
								text : 'Add',
								tooltip : 'Add a new item',
								iconCls : 'icon-add',
								handler : function() {
									var grid = this.up('panel');
									Ext.create(cfg['_dialog']);
								}
							}, '-', {
								itemId : 'removeButton',
								text : 'Remove',
								tooltip : 'Remove the selected item',
								iconCls : 'icon-remove',
								handler : function() {
									var grid = this.up('panel');
									$.each(
													grid.getSelectionModel().getSelection(),
													cfg["_callback"]);

								}
							}]
				}];

				this.callParent(arguments);
				this.initConfig(cfg);
			}
		});
