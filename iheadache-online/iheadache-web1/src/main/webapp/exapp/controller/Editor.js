Ext.define('IH.controller.Editor', {
	extend : 'Ext.app.Controller',
	stores : ['HeadacheTreatments', 'Treatments', 'TriggersProfile',
	          'HeadachePains', 'Lookups'],
	          models : ['HeadachePain', 'HeadacheTreatment', 'Lookup', 'ProfileItem'],
	          views : ['diary.Window', 'diary.DiaryHeader', 'editor.TreatmentEditor2',
	                   'editor.PainEditor', 'editor.PossibleTriggersEditor',
	                   'editor.DisabilityEditor', 'editor.NotesEditor',
	                   'editor.PainChooser', 'editor.TreatmentChooser', 'editor.Window',
	                   'IH.core.ListEditor', 'calendar.NavCalendar'],
	                   refs : [{
	                	   ref : 'edit_header',
	                	   selector : 'edit_header'
	                   }, {
	                	   ref : 'edit_symptom',
	                	   selector : '#symptoms-editor'
	                   }, {
	                	   ref : 'edit_disability',
	                	   selector : 'edit_disability'
	                   }, {
	                	   ref : 'edit_triggers',
	                	   selector : 'edit_triggers'
	                   }, {
	                	   ref : 'edit_notes',
	                	   selector : 'edit_notes'
	                   },

	                   {
	                	   ref : 'main_window',
	                	   selector : 'editor_window'
	                   }

	                   ],
	                   buildToolTip : function(complete) {
	                	   if (complete) return {tooltip:"Complete",type:"complete"};
	                	   return  {tooltip:"Incomplete",type:"incomplete"};
	                   },

	                   getPainTooltip: function(headache) {

	                	   if(headache.recordComplete == true)
	                		   return this.buildToolTip(true);

	                	   if(headache.pains == null)
	                		   return this.buildToolTip(false);

	                	   return  this.buildToolTip(headache.pains.length > 0);

	                   },
	                   getSymptomTooltip: function(headache){

	                	   if(headache.recordComplete == true)
	                		   return this.buildToolTip(true);

	                	   var standardSymptoms = this.getStore('SYMPTOM');

	                	   if(standardSymptoms == null || headache.symptoms == null)
	                		   return this.buildToolTip(false);  		

	                	   if(standardSymptoms.data.length == 0 || headache.symptoms.length == 0)
	                		   return this.buildToolTip(false);  

	                	   var completeFlag = true;

	                	   $.each(standardSymptoms.data.items, function(i, symptomItem){
	                		   $.each(headache.symptoms, function(j, symptom){
	                			   if(symptom.id == symptomItem.data.id){
	                				   if(symptom.no == false && symptom.yes == false){
	                					   completeFlag = false;
	                				   }
	                			   }
	                		   });
	                	   });
	                	   return this.buildToolTip(completeFlag);

	                   },
	                   
	                   ensureStandardSymptoms: function() {
	                	   	  var standardSymptoms = this.getStore('SYMPTOM');
	                	   	  var symptomCounter = 0;
	                	   	  var headerVars = this.getEdit_header().getValues();
	                	   	  if ("rb" in headerVars)
	                	   		  if ($.inArray("noHeadache", headerVars.rb) >-1)
	                	   			  return true;
	                	   	  var values = this.getEdit_symptom().getValues();
	                	   	  $.each(standardSymptoms.data.items, function(i,o) {
	                	   		  if (o.internalId in values) 
	                	   			symptomCounter ++;
	                	   		 
	                	   	  });
	                	   	  if (symptomCounter >= standardSymptoms.data.items.length )
	                	   		  return true;
	                	   	  
	                	   	  return false;

	                   },
	                   getTreatmentToolTip: function(headache){

	                	   if(headache.recordComplete == true)
	                		   return this.buildToolTip(true);

	                	   if(headache.treatments == null)
	                		   return this.buildToolTip(false);

	                	   return this.buildToolTip((headache.treatments.length > 0));

	                   },
	                   getDisabilityToolTip: function(headache){

	                	   if(headache.recordComplete == true)
	                		   return this.buildToolTip(true);

	                	   if(headache.disability == null)
	                		   return this.buildToolTip(false);

	                	   var isComplete = false;

	                	   isComplete = (headache.disability.completelyDisabled > 0 || headache.disability.partiallyDisabled > 0);
	                	   $.each(headache.disability.responses, function(idx, o)
	                			   {
	                		   if (o.yes ==true) isComplete = true;
	                		   if (o.no ==true) isComplete = true;
	                			   });
	                	   return this.buildToolTip(isComplete);

	                   },
	                   getTriggerToolTip: function(headache){

	                	   if(headache.recordComplete == true)
	                		   return this.buildToolTip(true);

	                	   if(headache.triggers == null)
	                		   return this.buildToolTip(false);

	                	   return this.buildToolTip(headache.triggers.length > 0);

	                   },
	                   getNoteToolTip: function(headache){

	                	   if(headache.recordComplete == true)
	                		   return this.buildToolTip(true);

	                	   if(headache.note == null)
	                		   return this.buildToolTip(false);

	                	   return this.buildToolTip((headache.note != ''));

	                   },
	                   fixupCustomSymptoms: function (symptoms) {
	                	   
	                	       var comparator = {};
	        				   $.each(IH.user.customSymptoms, function(i,o){comparator[o.id] = o;});
	        				   $.each(symptoms, function(i,o) {
	        					   if (o.id in comparator)
	        						   o.custom = true;
	        				   });
	        				   return symptoms;
	                	   
	                   },
	                   displayEditor : function(headache) {

	                	   var label = headache._id == "new"?"New Headache":"Edit Headache";
	                	   Ext.create('IH.view.editor.Window', {
	                		   title: label,
	                		   x:100,
	                		   y:100,
	                		   items: [
	                		           Ext.create('IH.view.editor.Header', headache),

	                		           Ext.create('Ext.panel.Panel', {
	                		        	   title: null,

	                		        	   layout:'accordion',
	                		        	   height: 430,
	                		        	   defaults: {
	                		        		   // applied to each contained panel
	                		        		   layout:'fit'
	                		        	   },
	                		        	   layoutConfig: {
	                		        		   // layout-specific configs go here
	                		        		   titleCollapse: false,
	                		        		   animate: true,
	                		        		   activeOnTop: true
	                		        	   },
	                		        	   items: [{
	                		        		   title: 'Symptoms',
	                		        		   iconCls: 'icon-symptoms',
	                		        		   bodyStyle: 'padding-left:5px',
	                		        		   tools : [ IH.buildHelpTool("symptoms_editor.html"), this.getSymptomTooltip(headache)],
	                		        		   //listeners: {
	                		        		//	   collapse: function(){
	                		        		//		   var me = this;
	                		        		//		   if (this.ensureStandardSymptoms())
	                		        		//			   return;
	                		        		//     		  Ext.MessageBox.show({
	                			            //    	          title: 'Standard Symptoms not filled out',
	                			            //    	          msg: "You have not answered all of the symptom questions. iHeadache needs this information to classify the type of headache. Your headache may be considered an Unclassified Headache because this information is missing. Click CANCEL to return to your symptoms or click OK to continue. ",
	                			            //    	          buttons: Ext.MessageBox.OKCANCEL,
	                			            //    	          icon: Ext.Msg.WARNING,
	                			            //    	          fn: function(btn){
	                			            //    	        	  if (btn != "ok")
	                			            //    	        		  return false;
	                			            //    	          }
	                			            //    	      });
	                		        		//	   },
	                		        		//	   scope:this
	                		        		//   },
	                		        		   items:[Ext.create('IH.core.YesNoEditor', {
	                		        			   _contents : this.fixupCustomSymptoms(IH.current.headache.symptoms),
	                		        			   id : "symptoms-editor", 
	                		        			   itemHeight:20,
	                		        			   border:0
	                		        		   })]
	                		        	   },{
	                		        		   title: 'Pain',

	                		        		   tools : [IH.buildHelpTool("pain_editor.html"), this.getPainTooltip(headache)],
	                		        		   listeners: {
	                		        			   expand: function(){
	                		        			   }
	                		        		   },
	                		        		   iconCls: 'icon-pain',
	                		        		   items:[Ext.create('IH.view.editor.PainEditor',
	                		        				   headache.pains)]
	                		        	   },
	                		        	   {
	                		        		   title: 'Treatments',
	                		        		   iconCls: 'icon-treatments',
	                		        		   tools : [IH.buildHelpTool("treatments_editor.html"),this.getTreatmentToolTip(headache)],
	                		        		   listeners: {
	                		        			   expand: function(){
	                		        			   }
	                		        		   },
	                		        		   items:[Ext.create('IH.view.editor.TreatmentEditor2',
	                		        				   headache.treatments)]
	                		        	   },
	                		        	   {
	                		        		   title: 'Disability',
	                		        		   tools : [IH.buildHelpTool("disability_editor.html"), this.getDisabilityToolTip(headache)],
	                		        		   listeners: {
	                		        			   expand: function(){
	                		        			   }
	                		        		   },
	                		        		   iconCls: 'icon-disability',
	                		        		   items:[Ext.create('IH.view.editor.DisabilityEditor')]
	                		        	   },
	                		        	   {
	                		        		   title: 'Triggers',
	                		        		   iconCls: 'icon-triggers',
	                		        		   id: 'editor_triggers',
	                		        		   tools : [IH.buildHelpTool("triggers_editor.html"), this.getTriggerToolTip(headache)],
	                		        		   listeners: {
	                		        			   collapse: function(){
	                		        			   }
	                		        		   },
	                		        		   items:[Ext.create('IH.view.editor.PossibleTriggersEditor',
	                		        				   {
	                		        			   selected : headache.triggers
	                		        				   })]
	                		        	   },
	                		        	   {
	                		        		   title: 'Notes',
	                		        		   tools : [IH.buildHelpTool("notes_editor.html"), this.getNoteToolTip(headache)],
	                		        		   listeners: {
	                		        			   collapse: function(){
	                		        			   }
	                		        		   },
	                		        		   iconCls: 'icon-notes',
	                		        		   items: [Ext.create('IH.view.editor.NotesEditor')]
	                		        	   }]
	                		           })]

	                	   });


	                   },
	                   
	                   checked_submit: function() {
	                	   me = this;
	                 	   if (this.ensureStandardSymptoms()) {
        					   this.submit();
        					   return;
	                 	   }
	                 	   
	                 	  Ext.MessageBox.show({
                	          title: 'Headache Symptoms are Incomplete',
                	          msg: "You have not answered all of the symptom questions. iHeadache&#174 needs this information and a Pain score to classify the type of headache. Your headache may be considered an Unclassified Headache because this information is missing. Click CANCEL to return to your symptoms or click OK to continue. ",
                	          buttons: Ext.MessageBox.OKCANCEL,
                	          icon: Ext.Msg.WARNING,
                	          fn: function(btn){
                	        	  if (btn == "ok")
                	        		  me.submit();
                	          }
                	      });
	                 	   
	                	   
	                   },

	                   submit : function() {
	                	               	   
	                	   var mapper = Ext.create('IH.core.HeadacheMapper', IH.current.headache);
	                	   mapper.mapHeader(this.getEdit_header().getValues());
	                	   mapper.mapPains(Ext.StoreMgr.lookup("HeadachePains"));
	                	   mapper.mapTreatments(Ext.StoreMgr.lookup("HeadacheTreatments"));
	                	   mapper.mapSymptoms(this.getEdit_symptom().getValues());
	                	   mapper.mapDisability(this.getEdit_disability().getValues());
	                	   mapper.mapTriggers(this.getEdit_triggers().getValues());
	                	   mapper.mapNotes(this.getEdit_notes().getValues());

	                	   mapper.save(this.getStore('SYMPTOM'));
	                	   this.getMain_window().close();			

	                   },

	                   remove: function(id) {

	                	   Ext.Ajax.request({
	                		   url : '/app/service/headache/' + id,
	                		   method: "DELETE",
	                		   timeout : 60000
	                	   });
	                   },

	                   init : function() {

	                	   Ext.Ajax.defaultHeaders = {
	                			   'Accept' : 'application/json'
	                	   };

	                	   var t = this.getTriggersProfileStore();
	                	   t.filter([{
	                		   property : 'in',
	                		   value : true
	                	   }]);

	                	   Ext.create('IH.store.Lookups', {
	                		   _kind : 'PAIN_TYPE'
	                	   });

	                	   Ext.create('IH.store.Lookups', {
	                		   _kind : 'PAIN_LOCATION'
	                	   });

	                	   Ext.create('IH.store.Lookups', {
	                		   _kind : 'SYMPTOM'
	                	   });
	                	   Ext.create('IH.store.Lookups', {
	                		   _kind : 'DISABILITY_QUESTION'
	                	   });

	                	   Ext.create('IH.store.HeadacheTreatments');

	                	   Ext.create('IH.store.Lookups', {
	                		   _kind : 'TRIGGER'
	                	   });

	                	   Ext.create('IH.store.Treatments', {
	                		   storeId : "treatments"
	                	   });

	                	   this.application.addListener({
	                		   'create-headache' : function(somedate) {
	                			   
	                			   if (IH.hcp_site)
	                				   return;  //don't allow adding if hcp site.
	                			   IH.current.headache = Ext
	                			   .create('IH.core.FetchEntity',
	                			   '/app/service/diary/empty');
	                			   if (somedate) {
	                				   somedate.setHours(12);
	                				   IH.current.headache.start = somedate;
	                				   IH.current.headache.end = somedate;
	                			   }
	                			   IH.current.headache.treatments = [];
	                			   this.displayEditor(IH.current.headache);
	                		   },
	                		   scope : this
	                	   });

	                	   this.control({
	                		   'button[action=submit]' : {
	                			   click : this.checked_submit
	                		   }
	                	   });

	                   }
});
