Ext.define('IH.controller.Diary', {
	extend : 'Ext.app.Controller',
	stores : ['DailyHeadaches'],
	models : ['HeadacheShort'],
	views : ['diary.Window', 'diary.DiaryHeader', 'calendar.NavCalendar',
			'diary.HeadacheInfo'],
			
	onLaunch : function(application ) {
				//this.getStore('DailyHeadaches').load({params: {date:Ext.Date.format(new Date(),"Y-m-d"),userId : IH.user._id}
			//});
	},
			
	initialView : function(){return new Ext.Template('<iframe frameborder="0", scrolling="no" width="100%" height="100%" src="'+ IH.initial_view+
			'"></iframe>');},
	

	init : function() {

		Ext.Ajax.defaultHeaders = {
			'Accept' : 'application/json'
		};
					
		this.renderer = Ext.util.Format.dateRenderer('Y-m-d');
		

		this.application.addListener({
			'nav-day-change' : function(tab, cal, current, el) {
				if (tab.id != 'diary')
					return;
				var todays = this.getDaysEvents(cal,current);
				this.getStore('DailyHeadaches').loadRawData(todays);
				var panel = Ext.ComponentQuery.query('#diary panel [title=Headache Summary]');
				panel = panel[0].ownerCt;
				this.initialView().overwrite(panel.body);
				IH.current.date = current;
				this.updateCalendar(cal,current, el);
				this.refreshHeader(todays,current,true);

			},
			scope : this
		});
		
		this.application.addListener({
			'refresh-calendars' : function() {
				if (!IH.hcp_site) return;
				var panel = Ext.ComponentQuery.query('#diary panel [title=Headache Summary]');
				panel = panel[0].ownerCt;
				this.initialView().overwrite(panel.body);
				var g = Ext.get('diary_label');
				g.dom.innerHTML = '<h2 class="diary_header">Headache(s) on ' + this.renderer(IH.current.date) +'</h2>';

			},
			scope : this
		});
		
		
		this.application.addListener({
			'nav-month-change' : function(c,startDate, viewStart, viewEnd) {	
				$.ajax({
					url : "/app/service/diary/ptusage?year="+startDate.getFullYear()+"&month="+(startDate.getMonth()+1),
					dataType : "json",
					success : function(json) {
						var month = IH.monthNames[startDate.getMonth()];
						var templateLoader = Ext
						.create('IH.core.TemplateLoader');
						templateLoader.get('/resources/templates/diary_header.js',
						function(tpl) {
							Ext.fly('monthly_treatments').update(tpl.apply({"pt_start":json[0],
								"pt_end":json[1],"pt_ongoing":json[2], "month": month}), false);

						});
					},
					/*error : function(resp, x, y) {
						 console.log(resp,x,y);
						 IH.view.Popup.msg("Error Communicating with Server", y);
					} */

				});		
			}, 
			scope:this
		});
		
		this.application.addListener({
					'nav-event' : function(tab, cal, rec, el) {
						if (tab != 'diary')
							return;
						if (cal){
							this.updateCalendar(cal, rec.get('StartDate'), el);
						}
						
						IH.current.headache = Ext.create('IH.core.FetchEntity',
								"/app/service/headache/" + rec.internalId);
						
						var todays = this.getDaysEvents(cal,rec.get('StartDate'));
						this.getStore('DailyHeadaches').loadRawData(todays);
						
						$.each(this.getStore('DailyHeadaches').data.items, function(i,o) {
							if (o.internalId == rec.internalId){
								var hlist = Ext.ComponentQuery.query('#h_h_list');
								hlist[0].select(o);
							}
						 });
		
						
						
						this.refreshSummary();
						

					},
					scope : this
				});
	},
	
	
	refreshHeader: function(todays, current, doit) {
		var g = Ext.get('diary_label');
		g.dom.innerHTML = '<h2 class="diary_header">Headache(s) on ' + this.renderer(current) +'</h2>';
		
		if (todays.length==0)
	        	return;
		var hlist = Ext.ComponentQuery.query('#h_h_list');
		hlist[0].select(0);

        if (!doit)
        	return; 
        
        IH.current.headache = todays[0];
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


		
	},
	
	refreshSummary: function () {
		var templateLoader = Ext
		.create('IH.core.TemplateLoader');
		templateLoader.get('/resources/templates/headache2.js',
		function(tpl) {
			var panel = Ext.ComponentQuery
					.query('diary_h_info');
			panel = panel[0];
			panel.update(tpl.apply(IH.current.headache));
			panel.doLayout();

		});

	},
	
	updateCalendar: function(cal, current, el) {
		
				cal.setStartDate(current);
				IH.current.date = current;
				Ext.select('.ext-cal-day-today').removeCls('ext-cal-day-today');
				var ee = Ext.get(el.id);
				ee.addCls('ext-cal-day-today');
		
	},
	
	getDaysEvents: function (cal, today) {
	
		evts = cal.store.queryBy(function(rec){
            var thisDt = Ext.Date.clearTime(today, true).getTime(),
                M = Extensible.calendar.data.EventMappings,
                recStart = Ext.Date.clearTime(rec.data[M.StartDate.name], true).getTime(),
            	startsOnDate = (thisDt == recStart),
				spansDate = false,
                calId = rec.data[M.CalendarId.name],
                calRec = cal.calendarStore ? cal.calendarStore.getById(calId) : null;
                
            if(calRec && calRec.data[Extensible.calendar.data.CalendarMappings.IsHidden.name] === true){
                // if the event is on a hidden calendar then no need to test the date boundaries
                return false;
            }
			
			if(!startsOnDate){
                var recEnd = Ext.Date.clearTime(rec.data[M.EndDate.name], true).getTime();
            	spansDate = recStart < thisDt && recEnd >= thisDt;
			}
            return startsOnDate || spansDate;
        }, cal);
		var res = [];
		$.each(evts.items, function(i,o) {
			res.push(
			Ext.create('IH.core.FetchEntity',
					"/app/service/headache/" + o.internalId)
			);});
		return res;
		
		
	},
	
	renderMe: function() {

		var p = Ext.create('IH.view.diary.Window', {
			renderTo : 'main-content2',
			hideMode : 'offsets',
			items : [ 
			         Ext.create('IH.view.calendar.NavCalendar'),
			         Ext.create('IH.view.diary.DiaryHeader'),
			         Ext.create('IH.view.diary.HeadacheInfo', {html : this.initialView().html})
			]

		});

		IH.app.fireEvent('nav-month-change',null,new Date(), new Date(), new Date())
		return p;
		
		
	}
});
