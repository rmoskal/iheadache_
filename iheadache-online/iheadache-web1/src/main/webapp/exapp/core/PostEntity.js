Ext.define('IH.core.PostEntity', {

			constructor : function(url, data) {
			var results;
			$.ajax({
				  			type:"PUT",
							url : url,
							dataType : "json",
							data:  Ext.encode(data),
							contentType:"application/json; charset=utf-8",
							success : function(json) {
								results = json;
								IH.view.Popup.msg("Success", "Successfully updated");
							},
							async : false

						});
			return results;
			}
		});