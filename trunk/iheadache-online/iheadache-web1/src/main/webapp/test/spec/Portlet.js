describe('Portlet datastore tests', function() {

			it('should parse a date', function() {
						var dt = Ext.Date.parseDate("2011-09-10T04:00:00", 'c');
						expect(dt.getDate()).toEqual(10);
						expect(dt.getFullYear()).toEqual(2011);
						expect(dt.getHours()).toEqual(4);

					});

			it('should parse this sort of date too', function() {
						var dt = Ext.Date.parseDate("2011-09-10T08:30:00.000Z",
								'c');
						expect(dt.getDate()).toEqual(10);
						expect(dt.getFullYear()).toEqual(2011);
						expect(dt.getHours()).toEqual(4);
						expect(dt.getMinutes()).toEqual(30);

					});

			it('should load the component', function() {

						var dt = Ext.create('IH.core.HourChooser');
						expect(dt).toBeDefined();

					});

			it('should load the template loader', function() {

						var dt = Ext.create('IH.core.TemplateLoader');
						expect(dt).toBeDefined();
						var callback = jasmine.createSpy();
						dt.get('/resources/templates/headache.js', callback);
						waitsFor(function() {
									return callback.callCount > 0;
								});
						runs(function() {
									expect(callback).toHaveBeenCalled();

								});

					});

			/*
			 * Interesting the way extjs works, if an id property is included,
			 * this is put on the url like so:
			 * http://localhost:8080/app/service/storage/foo
			 * 
			 * Nice!
			 */
			it('Load saves and persists portlet state as singleton',
					function() {

						var state = Ext.create('IH.model.PortletState', {
									'id' : 'foo',
									'name' : 'some test thing'
								});
						runs(function() {
									state.save();
								});
						waits(500);
						var state2 = Ext.create('IH.model.PortletState', {
									'id' : 'foo',
									'name' : 'some other thing'
								});
						runs(function() {
									state2.save();
								});
						waits(500);
						var callback = jasmine.createSpy();
						IH.model.PortletState.load('foo', {
									success : callback
								});
						waitsFor(function() {
									return callback.callCount > 0;
								});
						runs(function() {

									expect(callback.mostRecentCall.args[0]
											.get('name'))
											.toEqual('some other thing');
								});

					});

			it('Load saves and persists portlet state as a collection',
					function() {

						var state = Ext.create('IH.model.PortletState2', {
									'name' : 'some test thing'
								});
						runs(function() {
									state.save();
								});
						waits(500);
						var state2 = Ext.create('IH.model.PortletState2', {
									'name' : 'some other thing'
								});
						runs(function() {
									state2.save();
								});
						waits(500);

						var store = Ext.create('IH.store.PortletState2');
						store.load();
						waitsFor(function() {
									return !store.isLoading();
								});
						runs(function() {
									expect(store.getCount() >= 2).toBeTruthy();
								});

					});

			it('Load all the lookups into one store', function() {

						store = Ext.create('IH.store.Lookups');
						var callback = jasmine.createSpy();
						store.load(callback);
						waitsFor(function() {
									return callback.callCount > 0;
								});
						runs(function() {
									expect(callback).toHaveBeenCalled();
									expect(store.getCount() > 20).toBeTruthy();
									var o = store.first();
									expect(o.get('lookupType'))
											.toEqual("TRIGGER");
								});

					});

			it('Load all the lookups into many stores', function() {

						store = Ext.create('IH.store.Lookups', {
									_kind : 'TRIGGER'
								});
						var callback = jasmine.createSpy();
						store.load(callback);
						waitsFor(function() {
									return callback.callCount > 0;
								});
						runs(function() {
									expect(callback).toHaveBeenCalled();
									expect(store.getCount() > 7).toBeTruthy();
									var o = store.first();
									expect(o.get('lookupType'))
											.toEqual("TRIGGER");
								});

					});

			it('registers the store with the store manager', function() {

						store = Ext.create('IH.store.Lookups', {
									_kind : 'TRIGGER'
								});
						expect(Ext.StoreMgr.lookup("TRIGGER")).toBeDefined();

					});

		});