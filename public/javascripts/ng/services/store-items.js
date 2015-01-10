Game.factory('StoreItems', ['$rootScope', '$resource', function($rootScope, $resource) {
    var resource = $resource('/storecontroller/unboughtitems', {});
    var items = resource.get();
    return {
        load: function() {

        },
        getStoreItems: function() {
            return items;
        },
        getByName: function(name) {

        }
    };
}]);
