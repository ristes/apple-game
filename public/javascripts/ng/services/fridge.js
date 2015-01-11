Game.factory('Fridge', [
    '$resource',
    'State',
    function($resource, State) {
        return {
            load: function() {
                var res = $resource('/FridgeController/farmerFridges', {});
                return res.query();
            },
            buyCapacity: function(type, capacity, callback) {
                var res = $resource('/FridgeController/buycapacity?fridgetype=' + type + '&capacity=' + capacity, {});
                return res.query(callback);
            },
            addtofridge: function(fridgeType, plantType, quantity, callback) {
                var res = $resource('/FridgeController/addtofridge?type=' + fridgeType +
                    '&plant_type=' + plantType + '&quantity=' + quantity, {});
                return res.query(callback);
            },
            removeFromFridge: function(fridgeType, plantType, quantity, callback) {
                var res = $resource('/FridgeController/removeFromFridge?type=' + fridgeType +
                    '&plant_type=' + plantType + '&quantity=' + quantity, {});
                return res.query(callback);
            }
        };
    }
]);
