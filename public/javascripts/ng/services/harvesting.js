Game.factory('Harvesting', ['$http', 'State', function($http, State) {
    return {
        shriber: function(callback) {
            var res = $http.get("/HarvestingController/harvestingPeriod");
            res.success(function(data) {
                if (typeof callback === 'function') {
                    callback(data);
                }
            });
        },
        getYield: function(callback) {
            var res = $http.get("/HarvestingController/getYield");
            res.success(function(data) {
                if (typeof callback === 'function') {
                    callback(data);
                }
            });
        },
        harvest: function(gameResult, callback) {

            var res = $http({
                method: 'POST',
                url: "/HarvestingController/harvest",
                data: $.param(gameResult),
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            });
            res.success(function(data) {
                if (data.status === true) {
                    State.set('farmer', data.farmer);
                    if (typeof callback === 'function') {
                        callback();
                    }
                }
            });
        },
        sell: function(plantTypeId, quantity, callback) {
            var res = $http({
                method: 'POST',
                url: "/SaleController/sell",
                data: $.param({
                    plant_id: plantTypeId,
                    quantity: quantity
                }),
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            });
            res.success(function(data) {
            	callback(quantity,data.additionalInfo);
            });
        }
    };
}]);
