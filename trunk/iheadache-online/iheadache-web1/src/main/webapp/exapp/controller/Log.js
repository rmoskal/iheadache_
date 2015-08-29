Ext.define('IH.controller.Log', {
	extend : 'Ext.app.Controller',
	models : ['LogEntry'],
	stores : ['Log'],
	views : ['log.Window', 'log.DateChooser', 'log.LogGrid','log.LogHeader'],

	refs : [{
				ref : 'dateForm',
				selector : 'logwindow'
				
			},
			{
				ref : 'criteriaForm',
				selector : 'logheader'
			}],

	init : function() {
		 

		this.control({
					'logwindow [action=refresh]' : {
						click : this.refresh
					}
				});

		this.control({
					'loggrid toolbar button[text=Older]' : {
						click : this.next
					}
				});
				
		this.control({
					'loggrid toolbar button[text=Recent]' : {
						click : this.previous
					}
				});
		


	},
	
	onLaunch: function() {
		var x = new Date();
		x.setUTCFullYear(x.getUTCFullYear()-1);
		this.getLogStore().load({
					params : {
						'startDate' :  Ext.Date.format(x, 'Y-m-d'),
						'endDate' :  Ext.Date.format(new Date(), 'Y-m-d')
					}
				});
	},
	
	getDates: function() {
		var renderer = Ext.util.Format.dateRenderer('Y-m-d');
		var panel = this.getDateForm();
		var from_date = renderer(panel.down("field[name='from_date']").value);
		var to_date = renderer(panel.down("field[name='to_date']").value);
		return {"startDate" : from_date, "endDate": to_date};
		
	},
	
	paramHelper: function(pageLink) {
		var results = {};
		var values = this.getDates();
		results.startDate = values.startDate;
		results.endDate = values.endDate;
		if (pageLink)
			results.pageLink = pageLink;
		if (this.getCriteriaForm().getValues().criteria)
			results.criteria = this.getCriteriaForm().getValues().criteria;
		if (this.getCriteriaForm().getValues().types)
			results.types = this.getCriteriaForm().getValues().types;
		
		return results;
		
	},
	next : function() {
		if (this.getLogStore().proxy.reader.rawData.nextLink) {
			this.getLogStore().load({
				params : this.paramHelper(this.getLogStore().proxy.reader.rawData.nextLink)
			});

		}
	},

	previous : function() {
		if (this.getLogStore().proxy.reader.rawData.previousLink) {
			var values = this.getDates();
			this.getLogStore().load({
				params : this.paramHelper(this.getLogStore().proxy.reader.rawData.previousLink)
			});

		}
	},

	refresh : function() {
		var values = this.getDates();
		this.getLogStore().load({
					params :  this.paramHelper()
				});
		

	}
	

});