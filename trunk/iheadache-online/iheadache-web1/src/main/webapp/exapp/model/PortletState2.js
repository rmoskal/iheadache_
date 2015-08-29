Ext.define('IH.model.PortletState2', {
			extend : 'Ext.data.Model',
			fields : [{
						name : 'name',
						type : 'string'
					}, {
						name : 'description',
						type : 'string'
					}, {
						name : 'img',
						type : 'string'
					}, {
						name : 'container',
						type : 'string'
					}],

			proxy : {
				type : 'rest',
				url : '/app/service/storage/multi/newItem',
				reader : {
					type : 'json'
				}
			}
		});