Ext.define('IH.local', {

			constructor : function(user) {
				 IH.app.addListener({
						'nav-event' : function(tab, cal, rec, el) {
				
							if (tab =="diary")
								return;
							
							IH.current.headache = Ext.create('IH.core.FetchEntity',
									"/app/service/headache/" + rec.internalId);
							var templateLoader = Ext
							.create('IH.core.TemplateLoader');
							templateLoader.get('/resources/templates/headache2.js',
							function(tpl) {
								var view = Ext.create('IH.view.headacheViewer',{html : tpl.apply(IH.current.headache)});
							});

						},
						scope : this
					});
				 
				 	IH.app.addEvents('change_user');
				 	IH.app.enableBubble('change_user');
				 	IH.hcp_site = true; 
					IH.initial_view = 'https://iheadachemd.com/DefaultDiaryPage/DefaultDiary.html';
				    IH.current.diary = IH.app.getController('IH.controller.Diary');
				    IH.current.diary.renderMe();
				    
				    
			        //var curr = Ext.create('IH.core.FetchEntity',
					//"/app/service/hcp/currentPatient" );
			        //IH.currentPatient = curr.id;
				    IH.current.patients = IH.app.getController('IH.controller.Patients');
				    IH.current.patients.renderMe();

			  
				    
		
				    
				    
			        
			}
});