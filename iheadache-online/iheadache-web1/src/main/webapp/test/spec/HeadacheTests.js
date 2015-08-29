describe('Headache Tests', function () {
	
it('Maps a headache from the server', function () {
	   var store = Ext.create('IH.store.Headaches');
	   var callback = jasmine.createSpy();
	   store.load ({
		   callback: callback,
		   params: {'startDate': '2011-01-01', 'endDate': '2011-11-30'}});
	   waitsFor(function() {
		        return callback.callCount > 0;
		    });

	   runs(function() {
	        expect(callback).toHaveBeenCalled();
	        expect(store.getCount()<1000).toBeTruthy();
	        var o = store.first();
			expect(o.get('id')).toEqual("f54bb011-13a6-4311-a7fc-e131e49116bd");
			var dt = o.get('start');
			expect(dt.getDate()).toEqual(5);
			expect(dt.getFullYear()).toEqual(2011);
			expect(dt.getHours()).toEqual(3);
	    });
		
	});

});