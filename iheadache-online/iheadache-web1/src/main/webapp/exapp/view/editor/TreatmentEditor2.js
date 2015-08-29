Ext.define('IH.view.editor.TreatmentEditor2', {
	constructor : function() {
		
		var s =  Ext.create('IH.store.HeadacheTreatments', {data:IH.current.headache.treatments});
		s.sort('dose','ASC');

		var treatmentName = '{description} {uom} {form} ';
		var drugRule = IH.user.appSettings.drugRule;
		if (drugRule == "BOTH") 
				treatmentName = '{description}' + ' (' + '{genericName}' + ')' + ' {uom} {form}';
		if (drugRule == "GENERIC") 
				treatmentName = '{genericName} {uom} {form}';
		if (drugRule == "TRADE")
				treatmentName = '{description} {uom} {form}';

		var labelTreatment = '{description} {genericName} {uom} {form}';

		return Ext.create('Ext.grid.Panel', {
					columnLines : true,
					selModel : Ext.create('Ext.selection.CheckboxModel'),
					frame : false,
					iconCls : 'icon-grid',
					alias : 'widget.edit_treatments',
					// renderTo:'main-content',
					title : null,
					store : s,
					
					tbar :  [{
									text : 'Add',
									tooltip : 'Add a new item',
									iconCls : 'icon-add',
									handler : function() {
										Ext.create('IH.view.editor.TreatmentChooser');
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
												function(i,o) {o.store.remove(o);});

									}
								}/*, {
									text : 'Profile',
									iconCls : 'icon-profile',
									handler : function() {
										Ext.create('IH.view.profile.TreatmentEditor');
									} 
					} */],
					
					columns : [{
						text : "Name",
						flex : 3,
						xtype: 'templatecolumn',
						tpl: treatmentName
						//tpl: '{description} {genericName} {form} {uom}'
						
					}, {
						text : "Time",
						flex : 1,
						dataIndex : 'dose',
						renderer : Ext.util.Format.dateRenderer('g:i A')
					}]
				});
	}

});