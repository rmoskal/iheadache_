Ext.define('IH.view.dashboard.UnwoundChart', {

	constructor : function(title, url,start, end) {
				var store = Ext.create("IH.store.UnwoundChart");
				store.getProxy().url = url;
				store.load({
							params : {
								'startDate' : start,
								'endDate' : end
							}
						});

				var o =  Ext.create('Ext.chart.Chart', {
							store : store,
							width : 700,
							height : 250,
							axes: [{
				                type: 'Numeric',
				                position: 'bottom',
				                fields: ['value'],
				                label: {
				                    renderer: Ext.util.Format.numberRenderer('0,0')
				                },
				                title: 'Occurrence',
				                grid: true,
				                minimum: 0
				            }, {
				                type: 'Category',
				                position: 'left',
				                fields: ['name'],
				                title: title
				            }],
				            series: [{
				                type: 'bar',
				                axis: 'bottom',
				                highlight: true,
				                tips: {
				                  trackMouse: true,
				                  width: 140,
				                  height: 28,
				                  renderer: function(storeItem, item) {
				                    this.setTitle(storeItem.get('name') + ': ' + storeItem.get('value'));
				                  }
				                },
				                label: {
				                  display: 'insideEnd',
				                  'text-anchor': 'middle',
				                    field: 'value',
				                    renderer: Ext.util.Format.numberRenderer('0'),
				                    orientation: 'vertical',
				                    color: '#333'
				                },
				                xField: 'name',
				                yField: 'value'
				            }]
						});
				
				return o;
			}

		});