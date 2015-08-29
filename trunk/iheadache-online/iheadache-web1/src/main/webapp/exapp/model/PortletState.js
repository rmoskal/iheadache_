Ext.define('IH.model.PortletState', {
			extend : 'Ext.data.Model',
			fields : [{
						name : 'id',
						type : 'string'
					}, {
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
				url : '/app/service/storage',
				reader : {
					type : 'json'
				}
			}
		});
