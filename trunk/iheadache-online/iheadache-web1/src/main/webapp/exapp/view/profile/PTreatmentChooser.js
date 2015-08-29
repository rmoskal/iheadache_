Ext.define('IH.view.profile.PTreatmentChooser', {
			extend : 'Ext.Window',
			title : 'Add a Treatment',
			layout : 'fit',
			scroll :'vertical',
			autoShow : true,
			height : 330,
			width : 400,
			modal : true,
			items : [{
						xtype : 'form',
						bodyPadding: 5,
						autoScroll:true
					}],
			initComponent : function() {
				this.buttons = [{
					text : 'Save',
					scope : this,
					handler : function() {

						var bx = this.items.get(0).getForm().findField('items')
								.getChecked();
						var dt = this.items.get(0).getForm().findField('dose');
						
						var sourceStore = Ext.StoreMgr.lookup("treatments");
						var store = Ext.StoreMgr.lookup("HeadacheTreatments");
						
						$.each(bx,function(i,b) {
						var inrec = sourceStore.getById(b.inputValue);
						
						var r = Ext.ModelManager.create({
									description : inrec.get('description'),
									form : inrec.get('form'),
									genericName : inrec.get('genericName'),
									uom : inrec.get('uom'),
									revision : inrec.get('revision'),
									migraineTreatment : inrec.get('migraineTreatment'),
									treatmentId : b.inputValue,
									dose : dt.getValue()
								}, 'IH.model.HeadacheTreatment');
						
						store.insert(store.data.length, r);
						});
						store.sort('dose','ASC');
						this.close();
					
					}
				}, {
					text : 'Cancel',
					scope : this,
					handler : this.close
				}];

				var store = Ext.StoreMgr.lookup("treatments");
				store.clearFilter();
				store.filter([{
							property : 'in',
							value : true
						}]);
				var itemsInGroup = [];

				store.each(function(o) {
							var isMigraine = o.get("migraineTreatment")?" [Migraine Specific]":"";
							itemsInGroup.push({
										boxLabel : o.get('description') + ' ('
												+ o.get('genericName') + ')/'
												+  o.get('form')+ " " + o.get('uom')
												+ isMigraine,
										name : "item",
										inputValue : o.get('id')
									});

						});

				this.items[0].items = [

				{
							xtype : 'timefield',
							name : 'dose',
							fieldLabel : 'Taken at',
							increment : 15,
							width : 200,
							value : new Date()
						}, {
							xtype : 'checkboxgroup',
							columns : 1,
							vertical : true,
							items : itemsInGroup,
							name : 'items'
						}

				];

				this.callParent(arguments);

			}
		});