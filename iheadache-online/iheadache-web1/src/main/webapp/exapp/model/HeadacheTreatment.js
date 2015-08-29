Ext.define('IH.model.HeadacheTreatment', {
			extend : 'Ext.data.Model',
			fields : [

			{
						name : 'treatmentId',
						type : 'string',
						mapping : 'treatment._id'
					}, {
						name : 'revision',
						type : 'string',
						mapping : 'treatment._rev'
					}, {
						name : 'description',
						type : 'string',
						mapping : 'treatment.description'
					}, {
						name : 'form',
						type : 'string',
						mapping : 'treatment.form'
					}, {
						name : 'genericName',
						type : 'string',
						mapping : 'treatment.genericName'
					},
					{
						name : 'migraineTreatment',
						type : 'boolean',
						mapping : 'treatment.migraineTreatment'
					},	
						{
						name : 'uom',
						type : 'string',
						mapping : 'treatment.uom'
					},
					{
						name : 'dose',
						type : 'date',
						dateFormat: 'time'
					}]
		});
