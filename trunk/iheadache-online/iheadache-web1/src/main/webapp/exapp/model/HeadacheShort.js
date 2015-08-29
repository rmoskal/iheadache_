Ext.define('IH.model.HeadacheShort', {
			extend : 'Ext.data.Model',
			fields : [{
						name : 'id',
						type : 'string',
						mapping:'_id'
					}, {
						name : 'partiallyDisabled',
						type : 'integer'
					},
					{
						name : 'completelyDisabled',
						type : 'integer'
					},{
						name : 'midas',
						type : 'integer'
					},
					{
						name: 'diaryHeadacheComplete',
						type: 'integer'	
					},
					{name:'description',
					 mapping:'kind.description'
					}, 
					{name: 'kind',
					 mapping:'kind.id'	
					}
					]
		});