Ext.define('IH.view.editor.DisabilityEditor', {
			extend : 'Ext.form.Panel',
			alias : 'widget.edit_disability',
			title : null,
			width : "100%",
			bodyPadding : 5,
			layout : 'column',
			
			getHours: function(n) {
	
				return Math.floor(n/60);
		
			},
			
			getMinutes: function(n) {
	
				return n%60;
		
			},
				
			initComponent : function() {
			 this.items = [

					{
						xtype : 'fieldcontainer',
						layout : 'hbox',
						columnWidth : 1,
						fieldLabel : 'Length of Time Completely Disabled',
						labelAlign : 'right',
						labelWidth : 230,
						items : [

								Ext.create('IH.core.HourChooser',{value:this.getHours(IH.current.headache.disability.completelyDisabled),
								name: 'completelyDisabled_hours'}),
								Ext.create('IH.core.MinuteChooser',{value:this.getMinutes(IH.current.headache.disability.completelyDisabled),
								name: 'completelyDisabled_minutes'})]
					}, {
						xtype : 'fieldcontainer',
						columnWidth : 1,
						layout : 'hbox',
						labelWidth : 230,
						fieldLabel : 'Length of Time Partially Disabled',
						labelAlign : 'right',
						items : [Ext.create('IH.core.HourChooser',{value:this.getHours(IH.current.headache.disability.partiallyDisabled),
								name: 'partiallyDisabled_hours'}),
								Ext.create('IH.core.MinuteChooser',{value:this.getMinutes(IH.current.headache.disability.partiallyDisabled),
								name: 'partiallyDisabled_minutes'})]
					}, {
						xtype : 'label',
						columnWidth : 1,
						style : 'font-weight:bold;',
						text : 'Because of your headache were you...',
						margin: '10 0 0 0'
							
					}, 

					Ext.create('IH.core.YesNoEditor3', {
								_contents:IH.current.headache.disability.responses,
								border : 0, title:null,
								columnWidth : 1,
						        itemHeight:30,
						        labelWidth : 350
							})
							];


				this.callParent(arguments);
	
			}

		});
