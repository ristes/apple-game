Game.factory("BubbleNotification", function() {
	var subscribers = [];
	var notify= function(message) {
		angular.forEach(subscribers, function(subscriber){
			subscriber.callback(message);
		});
	}
	return {
		success : function(message) {
			message.type = 'success';
			messages.push(message);
		},
		warning: function(messages) {
			notify(messages);
		},
		error:function(message) {
			message.type = error;
			messages.push(message);
		},
		subscribe: function(name, callback) {
			subscribers.push({name:name, callback:callback});
		}
	
	}
});