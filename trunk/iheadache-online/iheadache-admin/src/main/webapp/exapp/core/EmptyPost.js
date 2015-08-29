Ext.define('IH.core.EmptyPost', {

			constructor : function(url, data) {
			$.ajax({
				  			type:"PUT",
							url : url,
							dataType : "json",
							contentType:"application/json; charset=utf-8",
							async : false,
							error : function(resp, x, y) {
								IH.view.Popup.msg("Error Communicating with Server", "Your changes could not be saved");

							}

						});
			}
		});