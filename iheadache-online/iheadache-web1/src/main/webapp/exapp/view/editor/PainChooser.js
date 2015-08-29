Ext.define('IH.view.editor.PainChooser', {

			extend : 'Ext.Window',
			title : 'Describe Your Pain',
			layout : 'fit',
			autoShow : true,
			width : 600,
			height : 600,
			modal : true,

			initComponent : function() {
				this.buttons = [{
					text : 'Save',
					scope : this,
					handler : function() {
						
						if (! this.items.get(0).getForm().isValid())
							return;
						
						var dt = this.items.get(0).getForm().findField('at');
						var severity = this.items.get(0).getForm()
								.findField('severity');
						var items = this.items.get(0).getForm().items;
						var location = [];
						var locationId = [];
						var kind = [];
						var kindId = [];

						$.each(items[3].selModel.selected.items,
								function(i, o) {
									location.push(o.get('description'));
									locationId.push(o.get('id'));
								});

						$.each(items[4].selModel.selected.items,
								function(i, o) {
									kind.push(o.get('description'));
									kindId.push(o.get('id'));
								});
						
						/*The extjs time field doesn't store anything but the hours and 
						 * minutes correctly.  Years are always set to 2008.  So we fix
						 * the date up 
						 */
						var real_day = new Date(IH.current.headache.start);  
						real_day.setHours(dt.getValue().getHours());
						real_day.setMinutes(dt.getValue().getMinutes());

						var r = Ext.ModelManager.create({
									level : severity.getValue(),
									time : real_day,
									painType : kind.join(),
									painLocation : location.join(),
									painLocationId: locationId.join(),
									painTypeId : kindId.join()
								}, 'IH.model.HeadachePain');

						var store = Ext.StoreMgr.lookup("HeadachePains");
						store.add(r);
						store.sort('time','ASC');
						this.close();
					}
				}, {
					text : 'Cancel',
					scope : this,
					handler : this.close
				}];

				 this.items = [{
						xtype : 'form',
						bodyPadding:20,
						autoScroll: true,
						layout : 'column',
						items:[

				{
							xtype : 'timefield',
							name : 'at',
							fieldLabel : 'Reported At',
							labelAlign : 'right',
							increment : 15,
							columnWidth : .5,
							value: new Date()
						}, {
							xtype : 'numberfield',
							name : 'severity',
							id : 'fld_severity',
							fieldLabel : 'Severity',
							labelAlign : 'right',
							value : 0,
							maxValue : 10,
							minValue : 0,
							columnWidth : .5,
							listeners:{
								change :function(field, newValue, oldValue, eOpts) {
									field = Ext.ComponentQuery.query('#slider_severity');
									field[0].setValue(newValue);
								}
								
							}
						}, 
						//{xtype:"label", columnWidth : .5,text:"_"},
						{
							xtype : 'sliderfield',
							name : 'severitySlide',
							id : 'slider_severity',
							value : 0,
							maxValue : 10,
							columnWidth : .9,
							padding : '15 20 15 20',
							//border : 15,
							//style : {
							//	borderColor : 'red',
							//	borderStyle : 'solid'
							//},
							minValue : 0,
							//values: ["one", "two", "three"],
							listeners:{
								changecomplete :function( slider, newValue, thumb, options ) {
									field = Ext.ComponentQuery.query('#fld_severity');
									field[0].setValue(newValue);
								}
								
							}
						}, {
							xtype : 'grid',
							name : 'PAIN_LOCATION',
							selModel : Ext
									.create('Ext.selection.CheckboxModel'),
							store : Ext.StoreMgr.lookup("PAIN_LOCATION"),
							columnWidth : .5,
							columns : [{
										header : "Pain Location",
										dataIndex : 'description',
										flex:1
									}
									
									]
						},

						{
							xtype : 'grid',
							name : 'PAIN_TYPE',
							id: 'addPain_PainType',
							selModel : Ext
									.create('Ext.selection.CheckboxModel'),
							store : Ext.StoreMgr.lookup("PAIN_TYPE"),
							columnWidth : .5,
							columns : [{
										header : "Pain Type",
										dataIndex : 'description',
										flex:1
									}
									]
						}

				]
				 }];

				this.callParent(arguments);

			}
		});