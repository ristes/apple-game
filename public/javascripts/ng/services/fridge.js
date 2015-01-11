Game.factory('Fridge', [
    '$resource',
    'State',
    function($resource, State) {
        return {
            load: function() {
                var res = $resource('/FridgeController/farmerFridges', {});
                return res.query();
            },
            buyCapacity: function(type, capacity) {
                var res = $resource('/FridgeController/buycapacity?fridgetype=' + type + '&capacity=' + capacity, {});
                return res.query();
            },
            addtofridge: function(fridgeType, plantType, quantity) {
                var res = $resource('/FridgeController/addtofridge?type=' + fridgeType +
                    '&plant_type=' + plantType + '&quantity=' + quantity, {});
                return res.query();
            }
        };
    }
]);
