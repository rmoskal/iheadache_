Ext.define('IH.view.profile.Window', {
			extend : 'Ext.Window',
			id: 'profile',
			title : 'Preventative Treatments',
			width : 700,
			height : 400,
			autoShow : true,
			layout : {
				type : 'column'
			},
			items : [{
				xtype : 'profile_ActivePTGrid'
			}]
		});