(function() {
	var tmxb = java.lang.management.ManagementFactory.getThreadMXBean();
	var threadIDs = tmxb.getAllThreadIds();
	var array = []; 
	var totalCPUTime = 0; 
	for (var i=0;i<threadIDs.length;i++) {
	   var id = threadIDs[i];
	   var cpuTime = tmxb.getThreadCpuTime(id); 
	   totalCPUTime+= cpuTime; 
	   array.push({
	     id : id,
	     cpuTime : cpuTime,
	     cpuTimePercent : 0,
	     name :    tmxb.getThreadInfo(id).getThreadName()
	   });
	}
	for (var i=0;i<array.length;i++) {
	  array[i].cpuTimePercent = (array[i].cpuTime / totalCPUTime * 100).toFixed(2);   
	}
	var json = JSON.stringify(array, null, 2);
	return json;
})();