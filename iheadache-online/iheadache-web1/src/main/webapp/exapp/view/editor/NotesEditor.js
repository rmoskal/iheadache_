Ext.define('IH.view.editor.NotesEditor', {
			extend : 'Ext.form.Panel',
			bodyPadding : 5,
			height : 150,
			title : null,
			alias : 'widget.edit_notes',
			layout : 'fit',
			items : [{
						xtype : 'textareafield',
						grow : true,
						name : 'note'
					}],
			
			initComponent : function() {

				this.callParent(arguments);
				var o = this.down('textareafield');
				o.setValue(IH.current.headache.note);

				

			}

		});