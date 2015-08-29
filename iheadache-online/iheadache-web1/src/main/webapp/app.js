Ext.application({
    name: 'IH',
    appFolder:'exapp',
    controllers: [
       'Log','Diary','Editor', 'DashBase', 'Help','Reports', 'Patients', 'Preventative'
    ],

    launch: function() {
    	
    	
        Ext.require([
                     'Extensible.calendar.data.EventMappings',
                     'exapp.core.Overrides',
                     'exapp.view.headacheViewer',
                     'exapp.local',
                     'exapp.view.Popup'
                 ]);
    	
	   Extensible.calendar.data.EventMappings = {
			   	 CalendarId:  {name: 'CalendarId', mapping: 'cid', type: 'string'},
	    		 EventId:     {name: 'EventId', mapping:'id', type:'string'}, // int by default
	    	     StartDate:   {name: 'StartDate', mapping: 'start', type: 'date',dateFormat: 'time'},
	    	     EndDate:     {name: 'EndDate', mapping: 'end', type: 'date', dateFormat: 'time'},
	    	     Reminder:    {name: 'Reminder', mapping: 'reminder'},
	    	     IsAllDay:    {name: 'IsAllDay', mapping: 'ad', type: 'boolean'},
	    	     Title:       {name: 'Title', mapping: 'title'},
	    	     Notes:       {name: 'Notes', mapping: 'notes'},
	    	     Location:    {name: 'Location', mapping: 'location'},
	    	     Url:         {name: 'LinkUrl', mapping: 'link_url'},
	    	     RRule:       {name: 'RecurRule', mapping: 'recur_rule'}
	    	       
	        };
	    Extensible.calendar.data.EventModel.reconfigure();

    	
    	
    	 Ext.Ajax.defaultHeaders = {
    			 'Accept': 'application/json'
    			};
    	 
    	 Ext.JSON.encodeDate = function(d) {
			    return parseInt(Ext.Date.format(d, 'U'))*1000;};
    	 
    			
    	
    	IH.ClearStore = function(store) {
    		
    		store.each( function(o){
			       if (o.dirty) 
                     o.commit();
			       if (o.phantom) 
                     o.phantom=false;
			}); 
    		
    	};
    	
    	IH.buildHelpTool = function(identifier) {		
    		return {
    		    type:'help',
    		    tooltip: 'Get Help',
    		    handler: function(event, toolEl, panel){
    		    	IH.app.fireEvent('show-help',identifier);
    		    }
    		};
    	};
    	
    	//Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
   
    	Ext.state.Manager.setProvider(
    	   Ext.create('IH.core.ServerStateProvider')
    	); 
    	IH.monthNames = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December']
    	
    	IH.user = Ext.create ('IH.core.FetchEntity',"/app/service/profile/user");
    	IH.dataFor = IH.user;
    	
   	 	IH.principal  = Ext.create ('IH.core.FetchEntity',"/app/service/profile/principal");
   		IH.dataFor = IH.principal;
   		document.getElementById("patientName").innerHTML= "Welcome " + IH.dataFor.firstName+ " " + IH.dataFor.lastName;
     	IH.app = this;
    	IH.current = {};
    	IH.current_dash = {};
    	
    	var x = new Date();
		x.setUTCFullYear(x.getUTCFullYear()-1);
    	IH.dash_from_date = x;
		IH.dash_to_date = new Date();
		IH.dash_chunk = "MONTH";
		IH.current.date = new Date();
		IH.help_route = "https://iheadache.com/help/";
    	
    	
    	this.addEvents('change-tab'); //fired when a dasboard tab is changed
    	this.addEvents('add-front');   //fired when a portlet is added to the front dashboard page
    	this.addEvents('nav-month-change'); //fired when the month in the nav calendar changes
    	this.addEvents('nav-day-change'); //fired when the user selects a day with no headache in the nav calendar
    	this.addEvents('nav-event');  //fired when a user selects an event
    	this.addEvents('edit-headache'); //fires when the user wants to edit the current headache
    	this.addEvents('create-headache');
    	this.addEvents('refresh-calendars');
    	this.addEvents('top-tab-change');
    	this.addEvents('show-help'); //fires when an app component wants to render help
    	

    	this.enableBubble('change-tab');
    	this.enableBubble('add-front');
    	this.enableBubble('nav-month-change');
    	this.enableBubble('nav-day-change');
    	this.enableBubble('nav-event');
    	this.enableBubble('edit-headache');
    	this.enableBubble('create-headache');
    	this.enableBubble('refresh-calendars');
    	this.enableBubble('top-tab-change');
    	this.enableBubble('show-help');
    	//this.fireEvent('change-tab', 'DashFront'); 
        
        Ext.create ("IH.local");
      
        
    } 
    
        
});