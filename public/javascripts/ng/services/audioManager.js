Game.factory("audioManager",['ngAudio', function(ngAudio) {
	var filename;
	var callback = [];
	var status;
	return {
		subscribe: function(c){
			callback.push(c);
		},
		notify: function(f) {
			filename = f;
			angular.forEach(callback, function(c) {
				if (typeof c==='function') {
					c(f);
				}
			});
		}
		
	}
}])