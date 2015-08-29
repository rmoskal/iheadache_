Ext.define('IH.controller.Help', {
	extend : 'Ext.app.Controller',
	views : ["IH.view.help.HelpViewer"],
	
	init : function() {
		this.template = new Ext.Template('<iframe frameborder="0", width="100%" height="100%" src="{target}"></iframe>'),
		 
		 this.application.addListener({
				'show-help' : function(identifier) {
	
							var view = Ext.create('IH.view.help.HelpViewer');
							target = IH.help_route + identifier;
							view.html = this.template.applyTemplate({target:target});
							view.show();	
						
				},
				scope : this
			}); 
	}
});