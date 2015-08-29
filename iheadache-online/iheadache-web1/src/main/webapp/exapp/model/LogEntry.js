Ext.define('IH.model.LogEntry', {
			extend : 'Ext.data.Model',
			fields : [{
						name : 'id',
						type : 'string'
					}, {
						name : 'start',
						type : 'date'
					}, {
						name : 'end',
						type : 'date'
					}, "kind", "disabled",{
						name : 'midas',
						type : 'integer'
					}, {
						name : 'hasTreatments',
						type : 'boolean'
					}, {
						name : 'hasSymptoms',
						type : 'boolean'
					}, {
						name : 'hasTriggers',
						type : 'boolean'
					}, {
						name : 'hasDisability',
						type : 'boolean'
					}, {
						name : 'hasPains',
						type : 'boolean'
					}, {
						name : 'hasNote',
						type : 'boolean'
					}

			]
		});