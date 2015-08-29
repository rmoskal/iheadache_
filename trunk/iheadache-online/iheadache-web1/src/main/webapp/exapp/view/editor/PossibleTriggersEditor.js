Ext.define('IH.view.editor.PossibleTriggersEditor', {
			extend : 'Ext.grid.Panel',
			title : null,
			alias : 'widget.edit_triggers',
			store : 'TriggersProfile',
			columns : [{
						header : "Name",
						flex : 3,
						dataIndex : 'description'
					}],

			/*tbar : [{
						xtype : 'tbfill'
					},

					{
						text : 'Profile',
						iconCls : 'icon-profile',
						handler : function() {
							Ext.create('IH.view.profile.TriggerEditor');
						}
					}], */

			constructor : function(cfg) {
				cfg = cfg || {};
				this.selected = cfg['selected'];
				cfg['selModel'] =  Ext.create('Ext.selection.CheckboxModel')
				this.callParent(arguments);
				this.store.clearFilter();
				this.store.filter([{
					property : 'in',
					value : true
				}]);
				this.initConfig(cfg);
				
			},
			listeners : {
				'afterrender' : function(me) {
					 if (me.selected == null)
					 	return;
					 sel = [];
					 $.each(me.selected, function(i,o) { 
					  var s =me.store.getById(o.id); 
					  if (s)
					   sel.push(s);
					  }); 
					 me.getSelectionModel().select(sel);

				}
			},
			getValues : function() {
				
				return this.getSelectionModel().getSelection();
				
				
			}

		});
