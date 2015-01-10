Game.factory('$farmer', ['$http', 'State', function($http, State) {

    function swap(farmer) {
        if (farmer && farmer.hasOwnProperty('balans')) {
            State.set('farmer', farmer);
        }
    }

    function setStatus(status) {
        State.set('status', status);
        swap(status.farmer);
    }

    return {
        load: function() {
            var res = $http.get("/AuthController/farmer");
            res.success(function(data) {
                setStatus(data);
                swap(data.farmer);
                //        State.set("farmer",data);
                //        $day.load(data);
            });
            return res;
        },
        swap: swap,
        setStatus: setStatus
    };

}]);

Game.factory('$day', [
    '$rootScope',
    'State',
    '$http',
    '$weather',
    'Diseases',
    'BoughtItems',
    '$location',
    function($rootScope, State, $http, $weather, Diseases, BoughtItems,
        $location) {

        function onFarmer(farmer) {
            if (!$rootScope.farmer && farmer.field) {
                BoughtItems.load();
            }
            if (farmer.balans != null) {
                $rootScope.day = farmer;
                $rootScope.farmer = farmer;
            }

            $location.path(farmer.currentState || '/buy_tractor');
        }

        State.subscribe('farmer', '$farmer', onFarmer);

        return {
            get: function() {
                return State.getByField('farmer');
            },
            load: function(farmer) {
                State.set('farmer', farmer);
            },
            next: function() {
                var res = $http.get("/application/nextday");
                res.success(function(data) {
                    State.set('status', data);
                    if (!$rootScope.farmer && data.farmer.field) {
                        BoughtItems.load();
                    }
                    if (data.balans != null) {
                        $rootScope.day = data;
                        $rootScope.farmer = data;
                    }

                    $location.path(data.farmer.currentState || '/buy_tractor');
                    $weather.load();
                    Diseases.load();
                });
            },
            nextWeek: function() {
                var res = $http.get("/application/nextweek");
                res.success(function(data) {
                    State.set('status', data);
                    if (!$rootScope.farmer && data.field) {
                        BoughtItems.load();
                    }
                    if (data.balans != null) {
                        $rootScope.day = data;
                        $rootScope.farmer = data;
                    }
                    $location.path(data.farmer.currentState || '/buy_tractor');
                    $weather.load();
                    Diseases.load();
                });
            },
            nextMonth: function() {
                var res = $http.get("/application/nextmonth");
                res.success(function(data) {
                    State.set('status', data);
                    if (!$rootScope.farmer && data.field) {
                        BoughtItems.load();
                    }
                    if (data.balans != null) {
                        $rootScope.day = data;
                        $rootScope.farmer = data;
                    }
                    $location.path(data.farmer.currentState || '/buy_tractor');
                    $weather.load();
                    Diseases.load();
                });
            }
        };
    }
]);

Game.factory('$weather', [
    '$rootScope',
    '$http',
    function($rootScope, $http) {
        if (!$rootScope.weather) {
            $rootScope.weather = [];
        }
        return {
            load: function() {
                var res = $http
                    .get("/WeatherController/weatherforecast?fordays=5");
                res.success(function(data) {
                    $rootScope.weather = data;
                });
            }
        };
    }
]);
