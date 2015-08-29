Ext.define('IH.local', {
			constructor : function(user) {
				IH.app.addListener({
					'nav-event' : function(tab, cal, rec, el) {
						if (tab=="diary")
							return;
						IH.current.headache = Ext.create('IH.core.FetchEntity',
								"/app/service/headache/" + rec.internalId);
						var controller = IH.app.getController('Editor');
						controller.displayEditor(IH.current.headache);

					},
					scope : this
				});
				
		       IH.hcp_site = false; 
		       IH.initial_view = 'https://iheadache.com/DefaultDiaryPage/DefaultDiary.html';
		       IH.current.diary = IH.app.getController('IH.controller.Diary');
		       IH.current.diary.renderMe();
		
				
			}

});