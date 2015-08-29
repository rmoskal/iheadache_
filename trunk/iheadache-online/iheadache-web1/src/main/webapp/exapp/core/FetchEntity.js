Ext.define('IH.core.FetchEntity', {

			constructor : function(url) {
			var data;
			$.ajax({
							url : url,
							dataType : "json",
							cache:false,
							success : function(json) {
								data = json;
							},
							async : false

						});
			return data;
			}
		});