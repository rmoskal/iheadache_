Ext.define('IH.view.editor.TreatmentChooser', {
			extend : 'Ext.Window',
			title : 'Add a Treatment',
			layout : 'fit',
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
						/*The extjs time field doesn't store anything but the hours and 
						 * minutes correctly.  Years are always set to 2008.  So we fix
						 * the date up 
						 */
						var real_day = new Date(IH.current.headache.start);  
						real_day.setHours(dt.getValue().getHours());
						real_day.setMinutes(dt.getValue().getMinutes());
						
						var r = Ext.ModelManager.create({
									description : inrec.get('description'),
									form : inrec.get('form'),
									genericName : inrec.get('genericName'),
									uom : inrec.get('uom'),
									revision : inrec.get('revision'),
									migraineTreatment : inrec.get('migraineTreatment'),
									treatmentId : b.inputValue,
									dose : real_day
								}, 'IH.model.HeadacheTreatment');
						
						store.add(r);
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
				var drugRuleSort = IH.user.appSettings.drugRule;
				if (drugRuleSort == "BOTH") 
					store.sort('description','ASC');
				if (drugRuleSort == "GENERIC") 
					store.sort('genericName','ASC');
				if (drugRuleSort == "TRADE")
					store.sort('description','ASC');
						
						
				var itemsInGroup = [];

				store.each(function(o) {
							var isMigraine = o.get("migraineTreatment")?" [Migraine Specific]":"";
							var treatmentName = o.get('description');
							var drugRule = IH.user.appSettings.drugRule;
							if (drugRule == "BOTH") 
									treatmentName = o.get('description') + ' (' + o.get('genericName') + ')';
							if (drugRule == "GENERIC") 
									treatmentName = o.get('genericName');
							if (drugRule == "TRADE")
									treatmentName = o.get('description');
							var labelTreatment = treatmentName + " " + o.get('uom') + " " + o.get('form');
							itemsInGroup.push({
										//boxLabel : o.get('description') + ' ('
										//		+ o.get('genericName') + ')/'
										//		+  o.get('uom')+ " " + o.get('form')
										//		+ isMigraine,
										boxLabel : labelTreatment,
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