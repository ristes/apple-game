Game.factory('StoreItems', ['$rootScope', '$resource', function($rootScope, $resource) {
	var resource = $resource('/storecontroller/unboughtitems', {});
    var items = resource.get();
    return {
        load: function() {

        },
        getStoreItems: function() {
        	return items;
            
        },
        getItemsFromOther: function(callback) {
        	var resource = $resource('/storecontroller/unboughtitems', {});
            var items = resource.get().$promise.then(function(data) {
            	callback(data['other']);
            });
        },
        getItemsFromSpraying: function(callback) {
        	
        },
        getByName: function(name) {

        }
    };
}]);
