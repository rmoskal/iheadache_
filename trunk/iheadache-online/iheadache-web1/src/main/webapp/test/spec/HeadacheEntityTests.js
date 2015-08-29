describe('Headache Crud Tests', function () {
	
	
	
	it('should get a headache from the server', function () {
				  

	  var data = Ext.create ('IH.core.FetchEntity',"/app/service/diary/f65762e9-11b1-4965-a3c5-913c13af078b?date=2011-07-31") 
	  expect(data.headache.user).toEqual('f65762e9-11b1-4965-a3c5-913c13af078b')

	  
	});
	
	it('should load a treatment store correctly', function () {
				  

	  var data = Ext.create ('IH.core.FetchEntity',"/app/service/diary/f65762e9-11b1-4965-a3c5-913c13af078b?date=2011-07-31") 
	  expect(data.headache.user).toEqual('f65762e9-11b1-4965-a3c5-913c13af078b');
	  
	  var hStore = Ext.create('IH.store.HeadacheTreatments', {data:data.headache.treatments });
	  expect(hStore.getCount()).toBeGreaterThan(1);
	  console.log(hStore)
	  

	});
	
	
   it('should load the daily summary', function () {
			  
   		var callback = jasmine.createSpy();
		var s = Ext.create('IH.store.DailyHeadaches', {autoLoad: {callback: callback,params: {date:'2011-7-31'}}});
		
		waitsFor(function() {
		        return callback.callCount > 0;
		    });
		    
		runs(function() {
	        expect(callback).toHaveBeenCalled();
	        expect(s.getCount()>1).toBeTruthy();
				
		});
	  

	});
	
	
	
	
});