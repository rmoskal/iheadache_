Ext.define('IH.view.editor.Window', {
			extend : 'Ext.Window',
			title : 'Edit Headache',
			alias: "widget.editor_window",
			//closable:true,
			autoShow : true,
			height : 612,
			width : 600,
			modal : true,
			fbar : [{
				type : 'button',
				text : 'Ok',
				action : 'submit'
			}, {
				type : 'button',
				text : 'Cancel',
				handler : function() {
					var p = this.up('window');
					p.close();
				}
			}]
		});