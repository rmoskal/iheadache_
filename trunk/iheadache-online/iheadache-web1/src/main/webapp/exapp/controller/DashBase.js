Ext.define('IH.controller.DashBase', {
	extend : 'Ext.app.Controller',
	requires : ['IH.view.dashboard.StackedChart'],
	
	init : function() {
		
		Ext.Ajax.defaultHeaders = {
				'Accept' : 'application/json'
			};
		
		this._contents = {DashFront: 'IH.controller.DashFront',DashDisability:'IH.controller.DashDisability', DashAcute:'IH.controller.DashAcute',
				DashPreventative : 'IH.controller.DashPreventative', DashTrigger :'IH.controller.DashTrigger', DashCalendar: 'IH.controller.DashCalendar',
				DashSymptoms: 'IH.controller.DashSymptom', DashPain: 'IH.controller.DashPain'};

		this.application.addListener({ 
	        'change-tab' : function(o) {
	        		var controller = IH.app.getController(this._contents[o]);
	        		var old = [null]; //This is the previous controller if there is one
	        		var mneuItem;
	        		if (this.currentController)
	        			if (this.currentController != o) {
	        				mneuItem = Ext.get(this.currentController);
	        				mneuItem.removeCls('current_page_item');
	        				old = Ext.ComponentQuery.query('#d_'+ this.currentController);
	        				if (old[0])
	        					old[0].setVisible(false);
	        			}
	        		mneuItem = Ext.get(o);
	        		mneuItem.addCls('current_page_item');
	        		
	        		var _new = Ext.ComponentQuery.query('#d_'+ o);
	        		if (_new[0]) {
	        			//_new[0].destroy();
	        			var is_unchanged = this.checkDateChange(_new[0]);
	        			if (is_unchanged==false) {
	        				_new[0].destroy();
	        			} 
	        				
	        		}

	        		controller.renderMe();
	        		_new = Ext.ComponentQuery.query('#d_'+ o);
	        		if (_new[0])
        	    		_new[0].setVisible(true); 
        	    	
        	    	this.currentController = o; // We save the previous controller so we can see whether the date range has changed
	        },
			 scope:this
	    }); 
		
		this.application.addListener({
			'refresh-calendars' : function() {
				$.each(this._contents, function(key,o) {
					var panel = Ext.ComponentQuery.query('#d_'+ key);
					if (panel[0]) {
						panel[0].destroy();
					};
				});
			},
			scope : this
		});

		
	},
	
	
	refreshDash : function(button) {
		if (!button.up('panel'))
			return;
		this._refreshDash(button.up('panel'), this);
	},
	
	initShow : function() {
		var renderer = Ext.util.Format.dateRenderer('Y-m-d');
		return this.reload(renderer(IH.dash_from_date),renderer(IH.dash_to_date),IH.dash_chunk);
		
	},
	
	checkDateChange: function (_new) {
		var renderer = Ext.util.Format.dateRenderer('Y-m-d');
		var unchanged_date = true;
		
		var _new_from_date = _new.down("field[name='from_date']");
		var _new_to_date = _new.down("field[name='to_date']");
		var _new_chunk = _new.down("field[name='chunk']");
		
		unchanged_date = (renderer(_new_from_date.value)==renderer(IH.dash_from_date)) & 
			(renderer(_new_to_date.value)== renderer(IH.dash_to_date)) &
			(_new_chunk.value==IH.dash_chunk);
			
		return unchanged_date==true ? true: false;
		
	},
	
	_refreshDash: function(panel) {
		IH.dash_from_date = panel.down("field[name='from_date']").value;
		IH.dash_to_date = panel.down("field[name='to_date']").value;
		IH.dash_chunk = panel.down("field[name='chunk']").value;
		this.refresh_low_level(IH.dash_from_date,IH.dash_to_date,IH.dash_chunk,panel,this);
	},

	/**
	 * Since we sometimes refresh from the ui and sometimes from the backend, we 
	 * have a low level function that lets us specify all the values independently of
	 * how it is called. There may be a better way to do this!
	 * 
	 * @param from_date
	 * @param to_date
	 * @param chunk
	 * @param panel
	 * @param controller  
	 */
	refresh_low_level: function(from_date, to_date,chunk, panel, controller) {
		panel.destroy();
		controller.renderMe();
		//_new = Ext.ComponentQuery.query('#d_'+ o);
		//if (_new[0])
    		//_new[0].setVisible(true); 	
	}

});