Ext.define('IH.view.editor.PainEditor', {

						constructor : function(_data) {

							return Ext.create('IH.core.ListEditor', {
										title : null,
										width : "100%",
										store :  this.setupDataStore(_data),
										columns : [{
													header : "Time",
													flex : 1,
													dataIndex : 'time',
													renderer : Ext.util.Format
															.dateRenderer('g:i A')
												}, {
													header : "Severity",
													flex : 1,
													dataIndex : 'level'
												}, {
													header : "Type of Pain",
													flex : 3,
													dataIndex : 'painType',
													renderer: function(value, metaData, record, row, col, store, gridView){
														    return value.replace(/\,/g, ',&nbsp;');
														  }
												}, {
													header : "Location of Pain",
													flex : 2,
													dataIndex : 'painLocation',
													renderer: function(value, metaData, record, row, col, store, gridView){
													    return value.replace(/\,/g, ',&nbsp;');
													  }
												}],
										_dialog : 'IH.view.editor.PainChooser'

									});

						},
						setupDataStore : function(data) {
							var results = [];
							$.each(data, function(i, o) {
										var location = [];
										var kind = [];
										var locationId = [];
										var kindId = [];
										$.each(o.painLocation,
												function(ii, oo) {
													location.push(oo.description);
													locationId.push(oo.id);
												});
										$.each(o.painType,
												function(i1, o1) {
													kind.push(o1.description);
													kindId.push(o1.id);
												});
										results.push(Ext.create('IH.model.HeadachePain',{painType: kind.join(','),
										painTypeId: kindId.join(),
										painLocation: location.join(','),
										painLocationId: locationId.join(),
										level: o.level,
										time: new Date(o.time)}));
								
										

									});
							var store =  Ext.create(
									'IH.store.HeadachePains', {
										data :results});
							store.sort('time','ASC');
							return store;
							

						}
					}

			);
