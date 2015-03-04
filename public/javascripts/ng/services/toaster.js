Game.factory('Toaster', [ 'toaster', function(toaster) {
	toaster.options = {
		'closeButton' : true,
		'timeOut' : '3000'
	};
	return {
		success : function(message) {
			toaster.pop('success',message);
		},
		error : function(message) {
			toaster.pop('error',message);
		}

	};
} ]);