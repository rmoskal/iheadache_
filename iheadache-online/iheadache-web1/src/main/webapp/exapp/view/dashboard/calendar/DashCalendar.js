
Ext.define('IH.view.dashboard.calendar.DashCalendar', {
			constructor : function( start, end, chunk) {
			
	var startDate = new Date(start);
	var endDate = new Date(end);
	 // Months between years.
	  var totalMonthCount = (endDate.getFullYear() - startDate.getFullYear()) * 12;
	    
	 // Months between... months.
	  totalMonthCount += endDate.getMonth() - startDate.getMonth();
	  
	 // Subtract one month if b's date is less that a's.
	 if (endDate.getDate() < startDate.getDate())
	 {
		 totalMonthCount--;
	 }
	 
		
			
	//	store.load();
		
		var panelMain = Ext.create('Ext.panel.Panel', {
			width:"100%",
			//height: 1000,
			cls: "module",
			layout: 'column',
			autoScroll:true
			//bodyStyle: 'overflow-y:hidden; overflow-x:scroll'
			

		});

		var html = '<div><div class="int"><div id="legend-wrap"><div id="legend">';
		html += '<span class="MIGRAINE">Migraine Headaches</span><span class="PROBABLE_MIGRAINE">Probable Migraines</span>';
		html += '<span class="TENSION_HEADACHE">Tension Headaches</span>';
		html += '<span class="">Unclassified Headaches</span>';
		html += '<span class="NO_HEADACHE">No Headaches</span></div>';
		html += '</div></div></div>';
		
		panelMain.html = html;
		
		var date = endDate;
		date.setDate(1);
		var subPanel = this.buildPanel();
		var monthCount = 0;
		panelMain.add(subPanel);

			
            var mainWindow = this;
			var templateLoader = Ext.create('IH.core.TemplateLoader');
			templateLoader.get('/resources/templates/dashCalendar.js',
			function(tpl) {
				
				mainWindow.createStore(mainWindow, tpl, panelMain, subPanel, monthCount, totalMonthCount, date);

			});
			
	
			return panelMain;
	},
	buildPanel: function(){
		
		return Ext.create('Ext.panel.Panel', {
			columnWidth: 1,
			height: 250,
			width: 720,
			layout: 'column',
			border: false,
			bodyStyle: 'overflow:hidden'
			
		});
	},
	buildPanelItem: function(){
		
		return Ext.create('Ext.panel.Panel', {
			columnWidth: .33,
			width: 240,
			height: 250,
			layout: 'column',
			border: false,
			bodyStyle: 'overflow:hidden'
			
		});
	},
	createStore: function(mainWindow, tpl, panelMain, subPanel, monthCount, totalMonthCount, date){
		var store = Ext.create('Ext.data.Store', {
			autoLoad : false,
			model: 'IH.model.DashCalendar',
			proxy : {
				type : 'ajax',
				api : {
					read : '/app/service/dashboard/calendar?startDate=' + Ext.Date.format(date,"Y-m-d")
				},
				reader : {
					type : 'json'
				}
			}
		});
		
		store.load(function(){
			
			mainWindow.buildUI(mainWindow, tpl, panelMain, subPanel, monthCount, totalMonthCount, date, this);
		});
	},
	buildUI: function(mainWindow, tpl, panelMain, subPanel, monthCount, totalMonthCount, date, store){
		
		if(monthCount%3 == 0 && monthCount > 0) {
			subPanel = mainWindow.buildPanel();
			panelMain.add(subPanel);
		}
		
		var panelItem = mainWindow.buildPanelItem();
		subPanel.add(panelItem);
		tpl.overwrite(panelItem.body,store.data);
		
		var month = IH.monthNames[date.getMonth()] + ' ' + date.getFullYear();
		panelItem.body.dom.childNodes[1].childNodes[1].innerHTML = month;
		
		
		date.setMonth((date.getMonth() - 1));
		monthCount++;
		
		if(monthCount <= totalMonthCount){
			mainWindow.createStore(mainWindow, tpl, panelMain, subPanel, monthCount, totalMonthCount, date);
		}
	}
});