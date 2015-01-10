Game.factory('State', [function() {
    var state = {
        /**
         * Najaveniot farmer
         */
        farmer: null,
        diseases: null
    };

    var subscribers = {};

    return {
        subscribe: function(field, id, callback) {
            var topic = subscribers[field] || {};
            subscribers[field] = topic;
            if (topic.hasOwnProperty(id)) {
                console.log('duplicate subscription id: ' + id);
                return;
            } else {
                topic[id] = callback;
                if (state[field]) {
                    callback(state[field]);
                }
            }
        },
        unsubscribe: function(field, id) {
            delete subscribers[field][id];
        },
        get: function() {
            return state;
        },
        getByField: function(field) {
            return state[field];
        },
        gameState: function() {
            return state["farmer"];
        },
        set: function(field, data) {
            state[field] = data;
            angular.forEach(subscribers[field], function(callback, key) {
                callback(data);
            });
        }
    };

}]);
