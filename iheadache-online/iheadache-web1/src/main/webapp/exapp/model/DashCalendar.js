Ext.define('IH.model.DashCalendar', {
			extend : 'Ext.data.Model',
			fields : [
			          {
						name : 'Date',
						type : 'string',
						mapping: 'Date'
					}, 
			          {
						name : 'HeadacheType',
						type : 'string',
						mapping: 'HeadacheType'
					}, {
						name : 'HeadacheId',
						type : 'string',
						mapping: 'HeadacheId'
					}]
		});
