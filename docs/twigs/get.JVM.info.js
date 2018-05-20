(function() {
	var ManagementFactory =     java.lang.management.ManagementFactory;
	var compilationMXBean =     ManagementFactory.getCompilationMXBean();
	var runtimeMXBean =         ManagementFactory.getRuntimeMXBean();
	var operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
	
	var info = {
		os : {
			name :    operatingSystemMXBean.getName(),
			arch :    operatingSystemMXBean.getArch(),
			version : operatingSystemMXBean.getVersion(),
			availableProcessors: operatingSystemMXBean.getAvailableProcessors(),
			systemLoadAverage:   operatingSystemMXBean.getSystemLoadAverage()
		},
		compilation : {
			name :    compilationMXBean.getName(),
			totalCompilationTimeInMS : compilationMXBean.isCompilationTimeMonitoringSupported() ? compilationMXBean.getTotalCompilationTime() : -1
		}, 
		runtime : {
			bootClassPath :         runtimeMXBean.isBootClassPathSupported() ? runtimeMXBean.getBootClassPath() : null,
			classPath :             runtimeMXBean.getClassPath(),
			libraryPath :           runtimeMXBean.getLibraryPath(),
			// inputArgs
			managementSpecVersion : runtimeMXBean.getManagementSpecVersion(),
			name :                  runtimeMXBean.getName(),
			specName :              runtimeMXBean.getSpecName(),
			specVendor :            runtimeMXBean.getSpecVendor(), 
			specVersion :           runtimeMXBean.getSpecVersion(), 
			startTime :             runtimeMXBean.getStartTime(),
			uptime :                runtimeMXBean.getUptime(),
			vmName :                runtimeMXBean.getVmName(),
			vmVendor :              runtimeMXBean.getVmVendor(),
			vmVersion :             runtimeMXBean.getVmVersion(),
		}
	};
	
	var name = ManagementFactory.getRuntimeMXBean().getName();
	var json = JSON.stringify(info, null, 2);
	return json; 
})();




