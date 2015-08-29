Ext.define(
		'IH.view.calendar.NavCalendar',
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
									read : 'app/service/calendar/nav'
								},
								reader : {

									type : 'json',
									root : 'evts'
								}
							}
						});

				var calendarStore = Ext.create('IH.store.CalendarTypes');
				
		
				var results =  Ext.create('Extensible.calendar.CalendarPanel',
						{
					eventStore : eventStore,
					calendarStore : calendarStore,
					alias: 'widget.navcalendar',
			     	id: 'navcalendar',
					title: IH.monthNames[new Date().getMonth()],
					viewConfig :{
						startDay :1,
						
						getMoreText: function(o){
							return '<h2>&#x2639;</h2>';
					    }
							
						},
				    showMultiDayView: false,
				    showMonthView: true,
				    showMultiWeekView:false,
				    showDayView: false,
				    showWeekView:false,
				    showHeader: false,
				    readOnly:false,
				 	showTodayText:false,
					showNavToday:false,
					showTime:false,
				 	showNavJump:false,
					width:'50%',
					height : 240,
					listeners: {
					    eventclick : {fn:function ( thisEv,rec,el ) {
					    	var c = thisEv.up('panel').up('panel');
					    	IH.app.fireEvent('nav-event',"diary",thisEv,rec, el); 
					        return false;
					    },   scope: this},
					    dayclick : {fn:function ( thisEv, dt, allday, el ){
					    	var c = thisEv.up('panel').up('panel');
					    	IH.app.fireEvent('nav-day-change',c,thisEv,dt, el); 
					        return false;
					    } ,scope: this},
					    datechange : {fn:function ( thisEv,startDate, viewStart, viewEnd){
					    	var c = thisEv.up('panel').up('panel');
					    	thisEv.up('panel').setTitle(IH.monthNames[startDate.getMonth()]);
					    	IH.app.fireEvent('nav-month-change',c,startDate, viewStart, viewEnd); 
					    } ,scope: this},
					    rangeselect : {fn:function ( thisEv, dt, allday, el ){
							 
					        return false;
					    } ,scope: this}
					}
						
						}
					);
				IH.app.addListener({
					'refresh-calendars' : function() {
						this.getActiveView().refresh(true);;

					},
					scope : results
				});
				
				return results;

			}

		});

IH.view.Popup = function(){
    var msgCt;

    function createBox(t, s){
       return '<div class="msg"><h3>' + t + '</h3><p>' + s + '</p></div>';
    }
    return {
        msg : function(title, format){
            if(!msgCt){
                msgCt = Ext.DomHelper.insertFirst(document.body, {id:'msg-div'}, true);
            }
            var s = Ext.String.format.apply(String, Array.prototype.slice.call(arguments, 1));
            var m = Ext.DomHelper.append(msgCt, createBox(title, s), true);
            m.hide();
            m.slideIn('t').ghost("t", { delay: 1000, remove: true});
        }
    };
}();

