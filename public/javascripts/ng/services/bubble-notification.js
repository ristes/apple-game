Game.factory("BubbleNotification", [ '$day',function( $day) {
	var messages = [];
	return {
		success : function(message) {
			message.type = 'success';
			messages.push(message);
		},
		warning: function() {
			message.type = 'warning';
			messages.push(message);
		},
		error:function(message) {
			message.type = error;
			messages.push(message);
		}
	
	}
}]);