Ext.define('IH.core.BaseDurationChart', {
			extend : 'Ext.chart.Chart',
			width : 700,
			height : 200,
			animate : true,
			constructor : function(cfg) {
				cfg = cfg || {};

				if (cfg['startDate'] == null)
					throw ("Require a start date");

				if (cfg['endDate'] == null)
					throw "Require an  end date";

				cfg['store'].load();
				this.callParent(arguments);
				this.initConfig(cfg);
			}

		});