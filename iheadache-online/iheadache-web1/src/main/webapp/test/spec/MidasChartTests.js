describe('Midas chart datastore tests', function () {



	it('Load Midas store from server ', function() {

		var store = Ext.create('IH.store.MidasChart');
		var callback = jasmine.createSpy();
		store.load({ params: {'startDate': '2011-01-01', 'endDate': '2011-11-30', chunk:'MONTH'},
		callback : callback});
		waitsFor(function() {
			return callback.callCount > 0;
		});
		runs (function(){
			expect(store.getCount()).toEqual(11);
			var o = store.first();
			expect(o.get('midas')).toEqual(0); 
			//expect(o.get('Est')).toEqual(false); 
			var dt = new Date(o .get('axis'));
			expect(dt.getFullYear()).toEqual(2010);
		});

	}); 

	it('Test MidasChart ', function() {

		Ext.create('IH.view.dashboard.disability.MidasChart','2011-01-01', '2011-11-30', 'MONTH'); 


	});




});
