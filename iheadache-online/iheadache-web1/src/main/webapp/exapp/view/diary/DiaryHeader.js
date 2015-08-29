
Ext.define('IH.view.diary.DiaryHeader', {
	extend : 'Ext.form.Panel',
	bodyPadding : 5,
	height : 240,
	width: '50%',
	layout : {
		type : 'vbox',
		 pack : 'center'
	},
	items : [ 
	     {
	    	xtype:'label',
	    	id: 'diary_label',
	 		html: '<h2 class="diary_header">Today</h2>',
			//style : 'font-weight:bold;',
			width:'100%'
	    	 
	     },
	     {
	     	xtype:'dataview',
	    	autoScroll : true,
			width:'100%',
	     	store:'DailyHeadaches',
	     	id: 'h_h_list',
	     	itemSelector: 'li.headache-wrap',
	     	selectedItemCls: 'headache-selected',
	     	emptyText: '<b>No headaches</b>',
	     	tpl: ['<ul class="h_h_list"><tpl for=".">',
	     	'<li  class="headache-wrap diary_header"><image src="/resources/images/{kind}.png"/>&nbsp;{description}</li>',
    		'</tpl></ul><p></p>'],
    		 listeners: {
    			 
      			itemclick: function(view,rec,node){
      				
      				IH.current.headache = Ext.create('IH.core.FetchEntity',
								"/app/service/headache/" + rec.internalId);
      				var templateLoader = Ext
								.create('IH.core.TemplateLoader');
						templateLoader.get('/resources/templates/headache2.js',
								function(tpl) {
									var panel = Ext.ComponentQuery
											.query('diary_h_info');
									panel = panel[0];
									tpl.overwrite(panel.body,
											IH.current.headache);

								});
       			 }}
	     },

		{
			 xtype:'panel',
			 height: 140,
			 width:'100%',
			 id : 'monthly_treatments'
			
		}
	],
	tools: [
	             {
	                 xtype: 'button',
	                 text: 'Add headache for today',
	                 handler: function(btn){
	                	 IH.app.fireEvent('create-headache',null); 
	                 }
	             },
	             {
	                 xtype: 'button',
	                 text: 'Add headache for selected date',
	                 handler: function(btn){
	                	 if (IH.current.date > new Date()) {
	                		 IH.view.Popup.msg('Error', "You haven't had that headache yet");
	                		 return;
	                	 } 
	                	 IH.current.date.setHours(12);
	                	 IH.current.date.setMinutes(0);
	        			 IH.app.fireEvent('create-headache',IH.current.date); 
	                 }
	             }
	          ]

});
