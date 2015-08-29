Ext.define('IH.controller.Profile', {
	extend : 'Ext.app.Controller',
	stores : ['PTUsagesInactive','PTUsageFull','TriggersProfile','SymptomsProfile','PhysicianProfile','PTreatmentProfile', 'States'],
	models : ['PTUsage','ProfileItem','Physician','PreventativeTreatment',"Treatment"],
	views : ['profile.Window','profile.TriggerEditor','profile.TreatmentEditor',
	         'profile.PreferencesEditor', 'profile.PhysicianEditor',
	         "profile.PhysicianChooser", "profile.PTreatmentChooser"],
	
	//initialView : new Ext.Template('<iframe frameborder="0", scrolling="no" width="100%" height="100%" src="http://mostmedia.com"></iframe>'),
	     	
	         
	init: function(){

		this._contents = {Pro_Preference: 'IH.view.profile.PreferencesEditor',pro_Acute:'IH.view.profile.TreatmentEditor',
				Pro_Trigger : 'IH.view.profile.TriggerEditor', Pro_Symptoms :'IH.view.profile.SymptomEditor', Pro_Doctor: 'IH.view.profile.PhysicianEditor', 
				Pro_PT_Profile:'IH.view.profile.PTreatmentEditor'};


		this.control({
			'physicianchooser [action=refresh]' : {
				click : this.refresh
			}
		});
		
		
		this.application.addListener({ 
        'change-profiletab' : function(o) {
			//if( confirm('Did you save your changes?')) {
        	    for (var i in this._contents) {
          	    	var mneuItem = Ext.get(i);
        	    	mneuItem.removeCls('current_page_item');
        	    	if (o==i){
	        			mneuItem.addCls('current_page_item');
        	    	}
      
        	    }
        	    this.show(this._contents[o]);
        	//}
        },
//        edit : function(){ // 2nd; data items populated, controls still empty
//            //console.log('dataChanged');
//            alert('change listener');
//        },
		 scope:this
    }); 
	},
	show : function(name) {
		
		if (this.current)
			this.current.close();
		
		
		if (name =='IH.view.profile.ActivePTGrid') {
			this.current = Ext.create(name, 'main-content6');
			return;
		}
		
		if (name =='IH.view.profile.PhysicianEditor') {
			this.current = Ext.create(name);
			return;
		}
		
		
		
		this.current = Ext.create(name, {renderTo: 'main-content6'});
		

	}


});