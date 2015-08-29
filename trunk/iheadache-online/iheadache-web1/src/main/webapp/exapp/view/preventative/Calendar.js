Ext.define(
		'IH.view.preventative.Calendar',
		{

			constructor : function() {

				var eventStore = Ext
				.create(
						'Extensible.calendar.data.EventStore',
						{
							restful : true,
							autoLoad : true,
							proxy : {
								type : 'ajax',
								api : {
									read : 'app/service/calendar/ptreatments'
								},
								reader : {

									type : 'json',
									root : 'evts'
								}
							}
						});

				var calendarStore = Ext.create('IH.store.CalendarTypes');
				
			/*	Ext.override(Extensible.calendar.view.Month, {
				    getEventBodyMarkup: function(o){
				        console.log(o);
				    	return 'foo';
				    }
				}); */

				var results =  Ext.create('Extensible.calendar.CalendarPanel',
						{
					eventStore : eventStore,
					calendarStore : calendarStore,
					renderTo : 'main-content7',
					cls: 'calendartab_calendarpanel',
					title: IH.monthNames[new Date().getMonth()],
					viewConfig :{
						startDay :1,
						enableContextMenus: false,
						getMoreText: function(o){
				    	return 'More';
						}
					},
					todayText: 'Go To Today',
				    showMultiDayView: false,
				    showMonthView: true,
				    showMultiWeekView:false,
				    showDayView: false,
				    showWeekView:false,
				    readOnly:true,
					//title : 'My Calendar',
					width : 700,
					height : 600,
					listeners: {
					    eventclick : {fn:function ( thisEv,rec,el ) {
				    		 if (rec.internalId.substring(0, 4)=='none')
				    			 return false;
				    		 if (rec.internalId.substring(0, 6)=='start_')
				    			 return false;
				    		 IH.app.fireEvent('nav-event',"calendar",thisEv,rec, el); 
					        return false;
					    },   scope: this},
					    dayclick : {fn:function ( thisEv, dt, allday, el ){
					 
					        return false;
					    } ,scope: this},
					    datechange : {fn:function ( thisEv,startDate, viewStart, viewEnd){
					    	thisEv.up('panel').setTitle(IH.monthNames[startDate.getMonth()]);
					    } ,scope: this}
					}
						
						
						});
				
				IH.app.addListener({
					'top-tab-change' : function() {
						 this.getActiveView().refresh(true);
						//console.log(this.getActiveView());

					},
					scope : results
				});
				
				IH.app.addListener({
					'refresh-calendars' : function() {
						this.getActiveView().refresh(true);;

					},
					scope : results
				});
				return results;

			}

		});