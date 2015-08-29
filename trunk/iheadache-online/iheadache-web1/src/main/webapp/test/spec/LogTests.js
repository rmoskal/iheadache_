describe('Log Tests', function () {
	
it('Maps a log entry from the server', function () {
	   var store = Ext.create('IH.store.Log');
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
			//expect(o.get('id')).toEqual("f65762e9-11b1-4965-a3c5-913c13af078b_2011_1_5");
			var dt = o.get('start');
			//expect(dt.getDate()).toEqual(5);
			expect(dt.getFullYear()).toEqual(2011);
			//expect(dt.getHours()).toEqual(1);
	    });
		
	});

});