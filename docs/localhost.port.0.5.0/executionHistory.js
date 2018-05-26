
var executionHistory = (function() {
	var v = localStorage.getItem('executionHistory');
	var array = (v != null) ? JSON.parse(v) : [];  

	return {
	    push : function push(code) {
			array.push({time: new Date().getTime(), code : code});
			localStorage.setItem('executionHistory', JSON.stringify(array));
		},
		
		count : function() { return array.length; },
		get :   function(i) { return array[i]; },
		remove : function(i) { 
			array.splice(i,1);
			localStorage.setItem('executionHistory', JSON.stringify(array));
		}
	};
})();

 

	
