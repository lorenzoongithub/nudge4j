(function() {
  var map = java.lang.Thread.getAllStackTraces();
  var threads = map.keySet().toArray();
  var array = [];
  for (var i = 0; i < threads.length; i++) {
    var t = threads[i];
    var stackArray = map.get(t);
    var stackJSArray = [];
    for (var j = 0; j < stackArray.length; j++) {
      var stk = stackArray[j];
      stackJSArray.push({
        stackElement: stk + ""
      });
    }
    array.push({
      id: t.getId(),
      name: t.getName() + "",
      priority: t.getPriority(),
      state: t.getState() + "",
      stackArray: stackJSArray
    });
  }

  var oj = {
    time: java.lang.System.currentTimeMillis(),
    dump: array
  };

  var json = JSON.stringify(oj, null, 2);
  return json;
})();
